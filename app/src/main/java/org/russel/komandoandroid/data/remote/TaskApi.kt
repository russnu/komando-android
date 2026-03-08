package org.russel.komandoandroid.data.remote;

import org.russel.komandoandroid.data.model.Task
import org.russel.komandoandroid.data.model.request.CreateTaskRequest
import org.russel.komandoandroid.data.model.request.UpdateStatusRequest
import org.russel.komandoandroid.data.model.request.UpdateTaskRequest
import retrofit2.http.Body
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path
import retrofit2.http.HTTP

interface TaskApi {
    @GET("/api/task")
    suspend fun getTasks(): List<Task>

    @GET("/api/task/user/{userId}")
    suspend fun getTasksByUser(@Path("userId") userId: Int): List<Task>

    @GET("/api/task/{id}")
    suspend fun getTaskById(@Path("id") id: Int): Task

    @POST("/api/task")
    suspend fun createTask(@Body request: CreateTaskRequest): Task

    @PUT("/api/task/{id}")
    suspend fun updateTask(@Path("id") id: Int?, @Body request: UpdateTaskRequest): Task

    @DELETE("/api/task/{id}")
    suspend fun deleteTask(@Path("id") id: Int)

    @PATCH("/api/task/{id}/status")
    suspend fun updateTaskStatus(@Path("id") id: Int, @Body status: UpdateStatusRequest)

    @PUT("/api/task/{taskId}/users")
    suspend fun assignUsers(@Path("taskId") taskId: Int, @Body userIds: List<Int>)

    @HTTP(method = "DELETE", path = "/api/task/{taskId}/users", hasBody = true)
    suspend fun removeUsers(
        @Path("taskId") taskId: Int,
        @Body userIds: List<Int>
    )

}
