package org.russel.komandoandroid.data.auth

sealed class AuthState {
    object LoggedOut : AuthState()
    object LoggedIn : AuthState()
    object Loading : AuthState()
}