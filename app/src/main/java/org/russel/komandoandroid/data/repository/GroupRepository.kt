package org.russel.komandoandroid.data.repository

import org.russel.komandoandroid.data.model.Group
import org.russel.komandoandroid.data.model.request.CreateGroupRequest
import org.russel.komandoandroid.data.remote.GroupApi

class GroupRepository(private val api: GroupApi) {
    //--------------------------------------------------------------------------------------//
    suspend fun getGroups(): List<Group> {
        return api.getGroups()
    }
    //--------------------------------------------------------------------------------------//
    suspend fun getGroupsByUser(userId: Int): List<Group> {
        return api.getGroupsByUser(userId)
    }
    //--------------------------------------------------------------------------------------//
    suspend fun getGroupById(id: Int): Group {
        return api.getGroupById(id)
    }
    //--------------------------------------------------------------------------------------//
    suspend fun addGroup(request: CreateGroupRequest): Group {
        return api.createGroup(request)
    }
    //--------------------------------------------------------------------------------------//
    suspend fun updateGroup(task: Group): Group {
        return api.updateGroup(task.id, task)
    }
    //--------------------------------------------------------------------------------------//
    suspend fun deleteGroup(taskId: Int) {
        api.deleteGroup(taskId)
    }
    //--------------------------------------------------------------------------------------//
    suspend fun updateGroupStatus(taskId: Int) {
        api.updateGroupStatus(taskId)
    }
}