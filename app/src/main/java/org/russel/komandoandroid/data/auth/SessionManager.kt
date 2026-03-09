package org.russel.komandoandroid.data.auth

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.json.JSONArray

/**
 * Handles local session persistence using SharedPreferences.
 * Stores:
 * - User credentials
 * - Logged-in state
 * - User group IDs (for FCM topic restoration)
 */
class SessionManager(context: Context) {
    private val prefs = context.getSharedPreferences("app_session", Context.MODE_PRIVATE)

    private val _userId = MutableStateFlow(getUserId())
    val userIdFlow: StateFlow<Int?> = _userId
    private val _isLoggedIn = MutableStateFlow(isLoggedIn())
    val isLoggedInFlow: StateFlow<Boolean> = _isLoggedIn

    // Saves user credentials after successful login.
    fun saveCredentials(userId: Int, fullname: String, username: String, password: String) {
        prefs.edit()
            .putInt("userId", userId)
            .putString("fullname", fullname)
            .putString("username", username)
            .putString("password", password)
            .apply()
        _userId.value = userId
        _isLoggedIn.value = true
    }

    // Clears all stored session data
    fun getUserId(): Int? {
        val id = prefs.getInt("userId", -1)
        return if (id != -1) id else null
    }
    fun getFullName(): String? = prefs.getString("fullname", null)
    fun getUsername(): String? = prefs.getString("username", null)
    fun getPassword(): String? = prefs.getString("password", null)
    fun clearSession() {
        prefs.edit().clear().apply()
        _userId.value = null
        _isLoggedIn.value = false
    }

    fun isLoggedIn(): Boolean = !getUsername().isNullOrEmpty() && !getPassword().isNullOrEmpty()

    // User Groups ========================================================================== //
    /**
     * Saves group IDs locally for FCM topic restoration.
     *  - Needed because topic subscriptions must be restored when token refreshes or app restarts.
     *  - Used after fetching groups from backend.
     */
    fun saveUserGroups(groupIds: List<Int>) {
        val json = JSONArray(groupIds).toString()  // Convert list to JSON string
        prefs.edit().putString("userGroups", json).apply()
    }

    /** Returns stored group IDs */
    fun getUserGroups(): List<Int> {
        val json = prefs.getString("userGroups", "[]") ?: "[]"
        val jsonArray = JSONArray(json)
        val list = mutableListOf<Int>()
        for (i in 0 until jsonArray.length()) {
            list.add(jsonArray.getInt(i))
        }
        return list
    }
}