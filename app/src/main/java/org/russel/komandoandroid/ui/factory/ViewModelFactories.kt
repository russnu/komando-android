package org.russel.komandoandroid.ui.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.russel.komandoandroid.data.auth.SessionManager
import org.russel.komandoandroid.data.repository.AuthRepository
import org.russel.komandoandroid.data.repository.DeviceRepository
import org.russel.komandoandroid.data.repository.GroupRepository
import org.russel.komandoandroid.data.repository.TaskRepository
import org.russel.komandoandroid.data.repository.UserRepository
import org.russel.komandoandroid.ui.group.GroupViewModel
import org.russel.komandoandroid.ui.login.LoginViewModel
import org.russel.komandoandroid.ui.profile.ProfileViewModel
import org.russel.komandoandroid.ui.task.TaskViewModel

class LoginViewModelFactory(private val authRepository: AuthRepository,
                            private val deviceRepository: DeviceRepository,
                            private val groupViewModel: GroupViewModel,
                            private val sessionManager: SessionManager
                            ) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(authRepository,
            deviceRepository,
            groupViewModel,
            sessionManager) as T
    }
}

class TaskViewModelFactory(private val taskRepository: TaskRepository,
                           private val userRepository: UserRepository,
                           private val sessionManager: SessionManager) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TaskViewModel(taskRepository, userRepository, sessionManager) as T
    }
}

class ProfileViewModelFactory(private val sessionManager: SessionManager) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProfileViewModel(sessionManager) as T
    }
}


class GroupViewModelFactory(private val groupRepository: GroupRepository,
                            private val sessionManager: SessionManager) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GroupViewModel(groupRepository, sessionManager) as T
    }
}

