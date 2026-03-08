package org.russel.komandoandroid.data.remote

import org.russel.komandoandroid.data.model.Group
import org.russel.komandoandroid.data.model.Task
import org.russel.komandoandroid.data.model.request.CreateGroupRequest
import org.russel.komandoandroid.data.model.request.UpdateGroupRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface GroupApi {
    @GET("/api/group")
    suspend fun getGroups(): List<Group>

    @GET("/api/group/user/{userId}")
    suspend fun getGroupsByUser(@Path("userId") userId: Int): List<Group>

    @GET("/api/group/{id}")
    suspend fun getGroupById(@Path("id") id: Int): Group

    @POST("/api/group")
    suspend fun createGroup(@Body request: CreateGroupRequest): Group

    @PUT("/api/group/{id}")
    suspend fun updateGroup(@Path("id") id: Int, @Body request: UpdateGroupRequest): Group

    @DELETE("/api/group/{id}")
    suspend fun deleteGroup(@Path("id") id: Int)

    @PUT("/api/group/{groupId}/users")
    suspend fun addGroupMembers(@Path("groupId") id: Int, @Body users: List<Int>)

    @HTTP(method = "DELETE", path = "/api/group/{groupId}/users", hasBody = true)
    suspend fun removeGroupMembers(@Path("groupId") id: Int, @Body users: List<Int>)
}