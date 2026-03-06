package org.russel.komandoandroid.ui.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import org.russel.komandoandroid.data.auth.SessionManager

class ProfileViewModel(private val sessionManager: SessionManager) : ViewModel() {

    val name: String
        get() = sessionManager.getFullName() ?: "Unknown"

    val username: String
        get() = sessionManager.getUsername() ?: "Unknown"

    fun logout() {
        sessionManager.clearSession() // clear saved credentials
    }
}