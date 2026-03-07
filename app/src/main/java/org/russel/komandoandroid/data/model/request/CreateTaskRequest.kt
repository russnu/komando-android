package org.russel.komandoandroid.data.model.request

import org.russel.komandoandroid.data.model.enums.TaskStatus

data class CreateTaskRequest(
    val title: String,
    val description: String,
    val status: TaskStatus = TaskStatus.PENDING,
    val group: GroupRef,
    val assignedUsers: List<UserRef> = emptyList()
)
