package org.russel.komandoandroid.data.model

data class Group(
    val id: Int? = null,
    val name: String,
    val tasks: List<Task> = emptyList(),
    val users: List<User> = emptyList(),
    val taskCount: Int,
    val userCount: Int,
    val createdBy: User
)
