package org.russel.komandoandroid.data.model.enums

enum class TaskStatus(val displayName: String) {
    PENDING("Pending"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed");

    override fun toString(): String = displayName

}