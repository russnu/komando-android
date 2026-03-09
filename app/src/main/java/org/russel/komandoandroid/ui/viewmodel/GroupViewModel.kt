package org.russel.komandoandroid.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.russel.komandoandroid.data.auth.SessionManager
import org.russel.komandoandroid.data.model.Group
import org.russel.komandoandroid.data.model.Task
import org.russel.komandoandroid.data.model.User
import org.russel.komandoandroid.data.model.request.CreateGroupRequest
import org.russel.komandoandroid.data.model.request.UpdateGroupRequest
import org.russel.komandoandroid.data.model.request.UserRef
import org.russel.komandoandroid.data.repository.GroupRepository
import org.russel.komandoandroid.fcmservice.FcmTopicManager

class GroupViewModel(private val repository: GroupRepository,
                     private val sessionManager: SessionManager
) : ViewModel() {
    private val _groups = MutableStateFlow<List<Group>>(emptyList())
    private val _selectedGroup = MutableStateFlow<Group?>(null)
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    private val _members = MutableStateFlow<List<User>>(emptyList())
    private val _addedMembers = MutableStateFlow<Set<User>>(emptySet())
    private val _originalMembers = MutableStateFlow<Set<User>>(emptySet())
    //--------------------------------------------------------------------------------------//
    val groups: StateFlow<List<Group>> get() = _groups
    val selectedGroup: StateFlow<Group?> = _selectedGroup
    val tasks: StateFlow<List<Task>> get() = _tasks
    val members: StateFlow<List<User>> get() = _members
    val currentUserId: StateFlow<Int?> = sessionManager.userIdFlow
    val addedMembers: StateFlow<Set<User>> get() = _addedMembers
    //--------------------------------------------------------------------------------------//

    fun fetchGroups() {
        viewModelScope.launch {
            try {
                val response = repository.getGroups()
                _groups.value = response
            } catch (e: Exception) {
                Log.e("GroupVM", "Failed to fetch groups", e)
            }
        }
    }

    //--------------------------------------------------------------------------------------//

    suspend fun fetchGroupsByUser(): List<Group> {
        if (!sessionManager.isLoggedIn()) return emptyList()
        val userId = sessionManager.getUserId() ?: return emptyList()

        return try {
            val response = repository.getGroupsByUser(userId)
            _groups.value = response

            // Save the group IDs for FCM topic subscriptions
            val groupIds = response.mapNotNull { it.id }
            sessionManager.saveUserGroups(groupIds)

            response

        } catch (e: Exception) {
            Log.e("GroupVM", "Failed to fetch groups of user.", e)
            emptyList()
        }
    }

    //--------------------------------------------------------------------------------------//

    fun getUserGroupIds(): List<Int> {
        return _groups.value.mapNotNull { it.id }
    }

    //--------------------------------------------------------------------------------------//

    fun fetchGroupById(id: Int) {
        viewModelScope.launch {
            try {
                val group = repository.getGroupById(id)
                _selectedGroup.value = group
                _tasks.value = group.tasks
                _members.value = group.users

                _originalMembers.value = group.users.toSet()
                _addedMembers.value = group.users.toSet()
            } catch (e: Exception) {
                Log.e("GroupVM", "Failed to fetch group by id", e)
            }
        }
    }

//    //--------------------------------------------------------------------------------------//
    fun addGroup(name: String) {
        viewModelScope.launch {
            try {
                val request = CreateGroupRequest(
                    name = name,
                    users = addedMembers.value.map { user ->
                        UserRef(user.id)
                    })

                val createdGroup = repository.addGroup(request)

                _groups.value += createdGroup
            } catch (e: Exception) {
                Log.e("GroupVM", "Failed to create group.", e)
            }
        }
    }
//    //--------------------------------------------------------------------------------------//
    fun updateGroup(groupId: Int, name: String) {
        viewModelScope.launch {
            try {
                val request = UpdateGroupRequest(name)
                val updatedGroup = repository.updateGroup(groupId, request)

                _selectedGroup.value = updatedGroup

                _groups.value = _groups.value.map {
                    if (it.id == updatedGroup.id) updatedGroup else it
                }
            } catch (e: Exception) {
                Log.e("GroupVM", "Failed to update group", e)
            }
        }
    }
    //--------------------------------------------------------------------------------------//
    fun updateGroupMembers() {
        val groupId = _selectedGroup.value?.id ?: return

        viewModelScope.launch {
            try {

                val originalIds = _originalMembers.value.mapNotNull { it.id }.toSet()
                val currentIds = _addedMembers.value.mapNotNull { it.id }.toSet()

                val addedIds = currentIds - originalIds
                val removedIds = originalIds - currentIds

                if (addedIds.isNotEmpty()) {
                    repository.addMembers(groupId, addedIds.toList())
                }

                if (removedIds.isNotEmpty()) {
                    repository.removeMembers(groupId, removedIds.toList())
                }

                // Unsubscribe current user from group if removed
                currentUserId.value?.let { userId ->
                    if (userId in removedIds) {
                        FcmTopicManager.unsubscribeFromGroup(groupId.toString())
                    }
                }

                _originalMembers.value = _addedMembers.value

            } catch (e: Exception) {
                Log.e("TaskVM", "Failed to update group members", e)
            }
        }
    }

//    //--------------------------------------------------------------------------------------//
    fun deleteGroup(groupId: Int) {
        viewModelScope.launch {
            try {
                repository.deleteGroup(groupId)
                _groups.value = _groups.value.filter { it.id != groupId }
            } catch (e: Exception) {
                Log.e("GroupVM", "Failed to delete group", e)
            }
        }
    }

    //--------------------------------------------------------------------------------------//
    fun toggleAddedMember(user: User) {
        _addedMembers.value = if (_addedMembers.value.contains(user)) {
            _addedMembers.value - user
        } else {
            _addedMembers.value + user
        }
    }
    //--------------------------------------------------------------------------------------//
    fun clearAddedMembers() {
        _addedMembers.value = emptySet()
    }

}