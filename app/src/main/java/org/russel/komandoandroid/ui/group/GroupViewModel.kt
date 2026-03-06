package org.russel.komandoandroid.ui.group

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
import org.russel.komandoandroid.data.repository.GroupRepository

class GroupViewModel(private val repository: GroupRepository,
                     private val sessionManager: SessionManager) : ViewModel() {
    private val _groups = MutableStateFlow<List<Group>>(emptyList())
    private val _selectedGroup = MutableStateFlow<Group?>(null)
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    private val _users = MutableStateFlow<List<User>>(emptyList())
    private val _currentUserId = MutableStateFlow(sessionManager.getUserId())

    //--------------------------------------------------------------------------------------//
    val groups: StateFlow<List<Group>> get() = _groups
    val selectedGroup: StateFlow<Group?> = _selectedGroup
    val tasks: StateFlow<List<Task>> get() = _tasks
    val users: StateFlow<List<User>> get() = _users
    val currentUserId: StateFlow<Int?> = _currentUserId
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

//    fun fetchGroupsByUser() {
//        if (!sessionManager.isLoggedIn()) return
//        val userId = sessionManager.getUserId() ?: return
//        viewModelScope.launch {
//            try {
//                val response = repository.getGroupByUser(userId)
//                _groups.value = response
//
//                // Save the group IDs in session for topic subscriptions
//                val groupIds = response.mapNotNull { it.id }  // Assuming Group has an 'id' property
//                sessionManager.saveUserGroups(groupIds)
//
//            } catch (e: Exception) {
//                Log.e("GroupVM", "Failed to fetch groups", e)
//            }
//        }
//    }

    fun fetchGroupsByUser() {
        if (!sessionManager.isLoggedIn()) return
        val userId = sessionManager.getUserId() ?: return

        viewModelScope.launch {
            try {
                val response = repository.getGroupsByUser(userId)
                _groups.value = response

                // Save the group IDs for FCM topic subscriptions
                val groupIds = response.mapNotNull { it.id }
                sessionManager.saveUserGroups(groupIds)

            } catch (e: Exception) {
                Log.e("GroupVM", "Failed to fetch groups of user.", e)
            }
        }
    }

    //--------------------------------------------------------------------------------------//

    fun fetchGroupIds(): List<Int> {
        return _groups.value.mapNotNull { it.id }
    }

//    suspend fun fetchGroupIdsByUser(): List<Int> {
//        if (!sessionManager.isLoggedIn()) return emptyList()
//        val userId = sessionManager.getUserId() ?: return emptyList()
//
//        return try {
//            val response = repository.getGroupsByUser(userId)
//            _groups.value = response
//
//            val groupIds = response.mapNotNull { it.id }
//            sessionManager.saveUserGroups(groupIds)
//
//            groupIds
//        } catch (e: Exception) {
//            Log.e("GroupVM", "Failed to fetch groups", e)
//            emptyList()
//        }
//    }

    //--------------------------------------------------------------------------------------//

    fun fetchGroupById(id: Int) {
        viewModelScope.launch {
            try {
                val group = repository.getGroupById(id)
                _selectedGroup.value = group
                _tasks.value = group.tasks
                _users.value = group.users
            } catch (e: Exception) {
                Log.e("GroupVM", "Failed to fetch group by id", e)
            }
        }
    }

//    //--------------------------------------------------------------------------------------//
//    fun addGroup(name: String) {
//        viewModelScope.launch {
//            try {
//                val newGroup = Group( name = name)
//
//                val createdGroup = repository.addGroup(newGroup)
//
//                _groups.value += createdGroup
//            } catch (e: Exception) {
//                Log.e("GroupVM", "Failed to create group.", e)
//            }
//        }
//    }
//    //--------------------------------------------------------------------------------------//
//    fun updateGroup(group: Group) {
//        viewModelScope.launch {
//            try {
//                val updatedGroup = repository.updateGroup(group)
//                _groups.value = _groups.value.map { if (it.id == updatedGroup.id) updatedGroup else it }
//            } catch (e: Exception) {
//                Log.e("GroupVM", "Failed to update group", e)
//            }
//        }
//    }
//    //--------------------------------------------------------------------------------------//
//    fun updateGroupStatus(groupId: Int) {
//        viewModelScope.launch {
//            try {
//                repository.updateGroupStatus(groupId)
//                _groups.value = _groups.value.map {
//                    if (it.id == groupId) it.copy(isDone = !it.isDone) else it
//                }
//            } catch (e: Exception) {
//                Log.e("GroupVM", "Failed to update group status", e)
//            }
//        }
//    }
//    //--------------------------------------------------------------------------------------//
//    fun deleteGroup(groupId: Int) {
//        viewModelScope.launch {
//            try {
//                repository.deleteGroup(groupId)
//                _groups.value = _groups.value.filter { it.id != groupId }
//            } catch (e: Exception) {
//                Log.e("GroupVM", "Failed to delete group", e)
//            }
//        }
//    }
}