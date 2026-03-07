package org.russel.komandoandroid.data.model;

import org.russel.komandoandroid.data.model.enums.TaskStatus
import org.russel.komandoandroid.data.model.request.GroupRef

data class Task (
    val id: Int? = null,
    val title: String,
    val description: String,
    val status: TaskStatus = TaskStatus.PENDING,
    val assignedUsers: List<User> = emptyList(),
    val group: GroupRef,
    val createdBy: User? = null
    )
