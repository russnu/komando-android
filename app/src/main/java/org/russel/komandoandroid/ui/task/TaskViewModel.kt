package org.russel.komandoandroid.ui.task

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.russel.komandoandroid.data.auth.SessionManager
import org.russel.komandoandroid.data.model.Task
import org.russel.komandoandroid.data.model.enums.TaskStatus
import org.russel.komandoandroid.data.model.User
import org.russel.komandoandroid.data.model.request.CreateTaskRequest
import org.russel.komandoandroid.data.model.request.GroupRef
import org.russel.komandoandroid.data.model.request.UserRef
import org.russel.komandoandroid.data.repository.TaskRepository
import org.russel.komandoandroid.data.repository.UserRepository

class TaskViewModel(private val repository: TaskRepository,
                    private val userRepository: UserRepository,
                    private val sessionManager: SessionManager
) : ViewModel() {
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    private val _selectedTask = MutableStateFlow<Task?>(null)
    private val _assignedUsers = MutableStateFlow<List<User>>(emptyList())
    private val _currentUserId = MutableStateFlow(sessionManager.getUserId())
    private val _users = MutableStateFlow<List<User>>(emptyList())
    //--------------------------------------------------------------------------------------//
    val tasks: StateFlow<List<Task>> get() = _tasks
    val selectedTask: StateFlow<Task?> = _selectedTask
    val assignedUsers: StateFlow<List<User>> get() = _assignedUsers
    val currentUserId: StateFlow<Int?> get() = _currentUserId
    val users: StateFlow<List<User>> get() = _users
    //--------------------------------------------------------------------------------------//
    fun fetchAllUsers() {
        viewModelScope.launch {
            try {
                val response = userRepository.getAllUsers()
                val currentUserId = sessionManager.getUserId()
                _users.value = if (currentUserId != null) {
                    response.filter { it.id != currentUserId }
                } else {
                    response
                }
            } catch (e: Exception) {
                Log.e("UserVM", "Failed to fetch users", e)
            }
        }
    }
    //--------------------------------------------------------------------------------------//
    fun fetchTasks() {
        viewModelScope.launch {
            try {
                val response = repository.getTasks()
                _tasks.value = response
            } catch (e: Exception) {
                Log.e("TaskVM", "Failed to fetch tasks", e)
            }
        }
    }
    //--------------------------------------------------------------------------------------//
    fun fetchTasksByUser() {
        if (!sessionManager.isLoggedIn()) return
        val userId = sessionManager.getUserId() ?: return
        viewModelScope.launch {
            try {
                val response = repository.getTaskByUser(userId)
                _tasks.value = response
            } catch (e: Exception) {
                Log.e("TaskVM", "Failed to fetch tasks", e)
            }
        }
    }
    //--------------------------------------------------------------------------------------//
    fun fetchTaskById(id: Int) {
        viewModelScope.launch {
            try {
                val task = repository.getTaskById(id)
                _selectedTask.value = task
                _assignedUsers.value = task.assignedUsers
            } catch (e: Exception) {
                Log.e("TaskVM", "Failed to fetch task by id", e)
            }
        }
    }
//
    //--------------------------------------------------------------------------------------//
    fun addTask(title: String, description: String, groupId: Int) {
        viewModelScope.launch {
            try {
                val request = CreateTaskRequest(
                    title = title,
                    description = description,
                    group = GroupRef(groupId),
                    assignedUsers = assignedUsers.value.map { user ->
                        UserRef(user.id)
                    }
                )

                val createdTask = repository.addTask(request)

                _tasks.value += createdTask
            } catch (e: Exception) {
                Log.e("TaskVM", "Failed to create task.", e)
            }
        }
    }
//    //--------------------------------------------------------------------------------------//
//    fun updateTask(task: Task) {
//        viewModelScope.launch {
//            try {
//                val updatedTask = repository.updateTask(task)
//                _tasks.value = _tasks.value.map { if (it.id == updatedTask.id) updatedTask else it }
//            } catch (e: Exception) {
//                Log.e("TaskVM", "Failed to update task", e)
//            }
//        }
//    }
    //--------------------------------------------------------------------------------------//
    fun updateTaskStatus(taskId: Int, status: TaskStatus) {
        viewModelScope.launch {
            try {
                repository.updateTaskStatus(taskId, status)
                fetchTaskById(taskId)
            } catch (e: Exception) {
                Log.e("TaskVM", "Failed to update task status", e)
            }
        }
    }
//    //--------------------------------------------------------------------------------------//
//    fun deleteTask(taskId: Int) {
//        viewModelScope.launch {
//            try {
//                repository.deleteTask(taskId)
//                _tasks.value = _tasks.value.filter { it.id != taskId }
//            } catch (e: Exception) {
//                Log.e("TaskVM", "Failed to delete task", e)
//            }
//        }
//    }

    fun toggleAssignedUser(user: User) {
        _assignedUsers.value = if (_assignedUsers.value.contains(user)) {
            _assignedUsers.value - user
        } else {
            _assignedUsers.value + user
        }
    }


}