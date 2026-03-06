package org.russel.komandoandroid.data.model.request

import org.russel.komandoandroid.data.model.enums.TaskStatus

data class UpdateStatusRequest(
    val status: TaskStatus
)