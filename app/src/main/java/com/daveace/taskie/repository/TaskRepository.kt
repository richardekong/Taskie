package com.daveace.taskie.repository

import com.daveace.taskie.api.Task
import com.daveace.taskie.api.TaskService
import com.daveace.taskie.api.Tasks

class TaskRepository(private val service: TaskService) {

    suspend fun createTask(task: Task): Result<Boolean> {
        return try {
            val response = service.createTask(task)
            if (response.isSuccessful)
                Result.success(true)
            else
                Result.failure(Exception("${response.code()}: ${response.message()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun readTasks(params: Map<String, String>): Result<Tasks?> {
        return try {
            val response = service.readTasks(params)
            if (response.isSuccessful && response.body() != null)
                Result.success(response.body())
            else
                Result.failure(Exception("${response.code()}: ${response.message()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun readTasks(): Result<Tasks?> {
        return try {
            val response = service.readTasks()
            if (response.isSuccessful && response.body() != null)
                Result.success(response.body())
            else
                Result.failure(Exception("${response.code()}: ${response.message()}"))

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun readTasksByTitle(title: String): Result<Tasks?> {
        return try {
            val response = service.readTasksByTitle(title)
            if (response.isSuccessful && response.body() != null)
                Result.success(response.body())
            else
                Result.failure(Exception("${response.code()}: ${response.message()}"))

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun readTasksByStatus(status: String): Result<Tasks?> {
        return try {
            val response = service.readTasksByStatus(status)
            if (response.isSuccessful && response.body() != null)
                Result.success(response.body())
            else
                Result.failure(Exception("${response.code()}: ${response.message()}"))

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun readTaskById(id: Long): Result<Task?> {
        return try {
            val response = service.readTask(id)
            if (response.isSuccessful && response.body() != null)
                Result.success(response.body())
            else
                Result.failure(Exception("${response.code()}: ${response.message()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateTask(id: Long, task: Task): Result<Boolean> {
        return try {
            val response = service.updateTask(id, task)
            if (response.isSuccessful)
                Result.success(true)
            else
                Result.failure(Exception("${response.code()}: ${response.message()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteTask(id: Long): Result<Boolean> {
        return try {
            val response = service.deleteTask(id)
            if (response.isSuccessful)
                Result.success(true)
            else
                Result.failure(Exception("${response.code()}: ${response.message()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}

