package org.russel.komandoandroid.data.model.request

import org.russel.komandoandroid.data.model.enums.TaskStatus

data class CreateGroupRequest(
    val name: String,
    val users: List<UserRef> = emptyList()
)
