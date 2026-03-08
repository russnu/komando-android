package org.russel.komandoandroid.ui.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.russel.komandoandroid.ui.viewmodel.AuthViewModel
import org.russel.komandoandroid.data.auth.SessionManager
import org.russel.komandoandroid.data.repository.AuthRepository
import org.russel.komandoandroid.data.repository.DeviceRepository
import org.russel.komandoandroid.data.repository.GroupRepository
import org.russel.komandoandroid.data.repository.TaskRepository
import org.russel.komandoandroid.data.repository.UserRepository
import org.russel.komandoandroid.ui.viewmodel.GroupViewModel
import org.russel.komandoandroid.ui.viewmodel.TaskViewModel
import org.russel.komandoandroid.ui.viewmodel.UserViewModel
class TaskViewModelFactory(private val taskRepository: TaskRepository,
                           private val userRepository: UserRepository,
                           private val sessionManager: SessionManager) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TaskViewModel(taskRepository, userRepository, sessionManager) as T
    }
}

class GroupViewModelFactory(private val groupRepository: GroupRepository,
                            private val sessionManager: SessionManager) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GroupViewModel(groupRepository, sessionManager) as T
    }
}

class UserViewModelFactory(private val userRepository: UserRepository,
                            private val sessionManager: SessionManager) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UserViewModel(userRepository, sessionManager) as T
    }
}

class AuthViewModelFactory(private val authRepository: AuthRepository,
                           private val deviceRepository: DeviceRepository,
                           private val groupViewModel: GroupViewModel,
                           private val sessionManager: SessionManager,
                           ) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AuthViewModel(authRepository, deviceRepository, groupViewModel, sessionManager) as T
    }
}

