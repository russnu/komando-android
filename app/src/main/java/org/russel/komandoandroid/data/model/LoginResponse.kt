package org.russel.komandoandroid.data.model

data class LoginResponse (
    val userId: Int,
    val fullname: String,
    val message: String,
    val username: String
)