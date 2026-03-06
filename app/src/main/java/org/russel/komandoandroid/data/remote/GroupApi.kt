package org.russel.komandoandroid.data.remote

import org.russel.komandoandroid.data.model.Group
import org.russel.komandoandroid.data.model.Task
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
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
    suspend fun createGroup(@Body group: Group): Group

    @PUT("/api/group/{id}")
    suspend fun updateGroup(@Path("id") id: Int?, @Body group: Group): Group

    @DELETE("/api/group/{id}")
    suspend fun deleteGroup(@Path("id") id: Int)

    @PATCH("/api/group/{id}/status")
    suspend fun updateGroupStatus(@Path("id") id: Int)
}