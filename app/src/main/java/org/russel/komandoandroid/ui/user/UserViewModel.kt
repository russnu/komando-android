package org.russel.komandoandroid.ui.user

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.russel.komandoandroid.data.auth.SessionManager
import org.russel.komandoandroid.data.model.User
import org.russel.komandoandroid.data.repository.UserRepository

class UserViewModel(private val repository: UserRepository,
                    private val sessionManager: SessionManager
) : ViewModel() {
//    private val _users = MutableStateFlow<List<User>>(emptyList())
//    //--------------------------------------------------------------------------------------//
//    val users: StateFlow<List<User>> get() = _users
//    //--------------------------------------------------------------------------------------//
//    fun fetchAllUsers() {
//        viewModelScope.launch {
//            try {
//                val response = repository.getAllUsers()
//                _users.value = response
//            } catch (e: Exception) {
//                Log.e("UserVM", "Failed to fetch users", e)
//            }
//        }
//    }
}