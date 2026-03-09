package org.russel.komandoandroid.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.russel.komandoandroid.data.auth.AuthState
import org.russel.komandoandroid.data.auth.SessionManager
import org.russel.komandoandroid.data.repository.AuthRepository
import org.russel.komandoandroid.data.repository.DeviceRepository
import org.russel.komandoandroid.fcmservice.FcmTopicManager

class AuthViewModel(
    private val repository: AuthRepository,
    private val deviceRepository: DeviceRepository,
    private val groupViewModel: GroupViewModel,
    private val sessionManager: SessionManager
) : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState
    //=========================================================================//
    private val _username = MutableStateFlow(sessionManager.getUsername())
    val username: StateFlow<String?> = _username
    //=========================================================================//
    private val _fullname = MutableStateFlow(sessionManager.getFullName())
    val fullName: StateFlow<String?> = _fullname
    //=========================================================================//
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    //=========================================================================//
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    //=========================================================================//

    init {
        refreshAuthState()
    }
    //=========================================================================//

    private fun refreshAuthState() {
        if (sessionManager.isLoggedIn()) {
            _authState.value = AuthState.LoggedIn
            _username.value = sessionManager.getUsername()
            _fullname.value = sessionManager.getFullName()
        } else {
            _authState.value = AuthState.LoggedOut
            _username.value = null
            _fullname.value = null
        }
    }
    //=========================================================================//
//    fun login(userId: Int, fullname: String, username: String, password: String) {
//        sessionManager.saveCredentials(userId, fullname, username, password)
//        refreshAuthState()
//    }
    fun login(username: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val result = repository.login(username, password)

            result.onSuccess {
//               // Register FCM token after login
//                registerDevice()
//
//               // Fetch groups for this user
//                val groupIds = groupViewModel.getUserGroupIds()
//
//               // Subscribe to each group's FCM topic
//               FcmTopicManager.subscribeToGroups(groupIds)

                // 1️⃣ Register device token
                registerDevice()

                // 2️⃣ Fetch groups from backend
                val groups = groupViewModel.fetchGroupsByUser()

                // 3️⃣ Wait for fetch to complete (or observe stateFlow)
                val groupIds = groups.mapNotNull { it.id }

                // 4️⃣ Sync FCM topics with server groups
                FcmTopicManager.syncSubscriptions(sessionManager, groupIds)


                _isLoading.value = false
                refreshAuthState()
            }

            result.onFailure { e ->
                _isLoading.value = false
                _error.value = e.message
            }
        }
    }
    //=========================================================================//
    fun logout() {
        viewModelScope.launch {
            unregisterDevice()
            sessionManager.clearSession()
            refreshAuthState()
        }
    }
    //=========================================================================//
    fun loginSuccess() {
        if (sessionManager.isLoggedIn()) {
            _authState.value = AuthState.LoggedIn
        } else {
            _authState.value = AuthState.LoggedOut
        }
    }

    private suspend fun registerDevice() {
        try {
            val token = FirebaseMessaging.getInstance().token.await()
            deviceRepository.registerToken(token)
        } catch (e: Exception) {
            Log.e("FCM", "Failed to register token after login: ${e.message}")
        }
    }

    private suspend fun unregisterDevice() {
        try {
            val token = FirebaseMessaging.getInstance().token.await()
            deviceRepository.unregisterToken(token)
        } catch (e: Exception) {
            Log.e("FCM", "Failed to unregister token on logout: ${e.message}")
        }
    }

}