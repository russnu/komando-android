package org.russel.komandoandroid.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.russel.komandoandroid.data.auth.SessionManager
import org.russel.komandoandroid.data.model.LoginUiState
import org.russel.komandoandroid.data.repository.AuthRepository
import org.russel.komandoandroid.data.repository.DeviceRepository
import org.russel.komandoandroid.fcmservice.FcmTopicManager
import org.russel.komandoandroid.ui.group.GroupViewModel

class LoginViewModel(
    private val repository: AuthRepository,
    private val deviceRepository: DeviceRepository,
    private val groupViewModel: GroupViewModel,
    private val sessionManager: SessionManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _uiState.value = LoginUiState(isLoading = true)

            val result = repository.login(username, password)

            result.onSuccess {
                // Register FCM token after login
                registerDevice()

                // Update login state
                _uiState.value = LoginUiState(isSuccess = true)

                // Fetch groups for this user
                val groupIds = groupViewModel.fetchGroupIds()

                // Subscribe to each group's FCM topic
                FcmTopicManager.subscribeToGroups(groupIds)
//                groupIds.forEach { groupId ->
//                    FcmTopicManager.getInstance()
//                        .subscribeToGroup(groupId.toString())
//                }

//                val groupIds = groupViewModel.fetchGroupsByUser()
            }

            result.onFailure {
                _uiState.value = LoginUiState(
                    isLoading = false,
                    error = it.message
                )
            }
        }
    }

    // ======================================================================== //

    private suspend fun registerDevice() {
        try {
            val token = FirebaseMessaging.getInstance().token.await()
            deviceRepository.registerToken(token)
        } catch (e: Exception) {
            Log.e("FCM", "Failed to register token after login: ${e.message}")
        }
    }
}