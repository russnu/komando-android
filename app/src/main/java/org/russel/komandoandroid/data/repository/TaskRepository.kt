package org.russel.komandoandroid.data.repository

import android.app.Application
import android.content.Context
import org.russel.komandoandroid.data.model.Task
import org.russel.komandoandroid.data.model.enums.TaskStatus
import org.russel.komandoandroid.data.model.request.CreateTaskRequest
import org.russel.komandoandroid.data.model.request.UpdateStatusRequest
import org.russel.komandoandroid.data.model.request.UpdateTaskRequest
import org.russel.komandoandroid.data.remote.RetrofitClient
import org.russel.komandoandroid.data.remote.TaskApi

class TaskRepository(private val api: TaskApi) {
    //--------------------------------------------------------------------------------------//
    suspend fun getTasks(): List<Task> {
        return api.getTasks()
    }
    //--------------------------------------------------------------------------------------//
    suspend fun getTaskByUser(userId: Int): List<Task> {
        return api.getTasksByUser(userId)
    }
    //--------------------------------------------------------------------------------------//
    suspend fun getTaskById(id: Int): Task {
        return api.getTaskById(id)
    }
    //--------------------------------------------------------------------------------------//
    suspend fun addTask(request: CreateTaskRequest): Task {
        return api.createTask(request)
    }
    //--------------------------------------------------------------------------------------//
    suspend fun updateTask(request: UpdateTaskRequest, taskId: Int): Task {
        return api.updateTask(taskId, request)
    }
    //--------------------------------------------------------------------------------------//
    suspend fun deleteTask(taskId: Int) {
        api.deleteTask(taskId)
    }
    //--------------------------------------------------------------------------------------//
    suspend fun updateTaskStatus(taskId: Int, status: TaskStatus) {
        val request = UpdateStatusRequest(status)
        api.updateTaskStatus(taskId, request)
    }
    //--------------------------------------------------------------------------------------//
    suspend fun assignUsers(taskId: Int, userIds: List<Int>) {
        api.assignUsers(taskId, userIds)
    }
    //--------------------------------------------------------------------------------------//
    suspend fun removeUsers(taskId: Int, userIds: List<Int>) {
        api.removeUsers(taskId, userIds)
    }

}