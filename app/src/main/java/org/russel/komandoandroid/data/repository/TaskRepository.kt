package org.russel.komandoandroid.data.repository

import android.app.Application
import android.content.Context
import org.russel.komandoandroid.data.model.Task
import org.russel.komandoandroid.data.model.enums.TaskStatus
import org.russel.komandoandroid.data.model.request.UpdateStatusRequest
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
    suspend fun addTask(task: Task): Task {
        return api.createTask(task)
    }
    //--------------------------------------------------------------------------------------//
    suspend fun updateTask(task: Task): Task {
        return api.updateTask(task.id, task)
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
}