package org.russel.komandoandroid.data.repository

import org.russel.komandoandroid.data.model.Group
import org.russel.komandoandroid.data.model.request.CreateGroupRequest
import org.russel.komandoandroid.data.model.request.UpdateGroupRequest
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
    suspend fun updateGroup(groupId: Int, request: UpdateGroupRequest): Group {
        return api.updateGroup(groupId, request)
    }
    //--------------------------------------------------------------------------------------//
    suspend fun deleteGroup(taskId: Int) {
        api.deleteGroup(taskId)
    }
    //--------------------------------------------------------------------------------------//
    suspend fun addMembers(groupId: Int, userIds: List<Int>) {
        api.addGroupMembers(groupId, userIds)
    }
    //--------------------------------------------------------------------------------------//
    suspend fun removeMembers(groupId: Int, userIds: List<Int>) {
        api.removeGroupMembers(groupId, userIds)
    }

}