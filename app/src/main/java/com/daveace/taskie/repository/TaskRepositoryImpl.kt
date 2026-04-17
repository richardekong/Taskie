package com.daveace.taskie.repository

import com.daveace.taskie.api.error.ErrorConverter
import com.daveace.taskie.api.model.Task
import com.daveace.taskie.api.model.TaskErrorResponse
import com.daveace.taskie.api.model.Tasks
import com.daveace.taskie.api.service.TaskService
import javax.inject.Inject

open class TaskRepositoryImpl @Inject constructor(
    private val service: TaskService,
    private val errorConverter: ErrorConverter
):TaskRepository {

    override suspend fun createTask(task: Task): Result<String> {
        return try {
            val response = service.createTask(task)
            if (response.isSuccessful)
                Result.success("${response.code()}: Task created!")
            else {
                val error = errorConverter.convert(response, TaskErrorResponse::class.java)
                return Result.failure(Exception("Error (${error?.status}): ${error?.message}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun readTasks(params: Map<String, String>): Result<Tasks?> {
        return try {
            val response = service.readTasks(params)
            if (response.isSuccessful && response.body() != null)
                Result.success(response.body())
            else {
                val error = errorConverter.convert(response, TaskErrorResponse::class.java)
                return Result.failure(Exception("Error (${error?.status}): ${error?.message}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

   override suspend fun readTasks(): Result<Tasks?> {
        return try {
            val response = service.readTasks()
            if (response.isSuccessful && response.body() != null)
                Result.success(response.body())
            else {
                val error = errorConverter.convert(response, TaskErrorResponse::class.java)
                return Result.failure(Exception("Error (${error?.status}): ${error?.message}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun readTasksByTitle(title: String): Result<Tasks?> {
        return try {
            val response = service.readTasksByTitle(title)
            if (response.isSuccessful && response.body() != null)
                Result.success(response.body())
            else {
                val error = errorConverter.convert(response, TaskErrorResponse::class.java)
                return Result.failure(Exception("Error (${error?.status}): ${error?.message}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun readTasksByStatus(status: String): Result<Tasks?> {
        return try {
            val response = service.readTasksByStatus(status)
            if (response.isSuccessful && response.body() != null)
                Result.success(response.body())
            else {
                val error = errorConverter.convert(response, TaskErrorResponse::class.java)
                return Result.failure(Exception("Error (${error?.status}): ${error?.message}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun readTaskById(id: Long): Result<Task?> {
        return try {
            val response = service.readTask(id)
            if (response.isSuccessful && response.body() != null)
                Result.success(response.body())
            else {
                val error = errorConverter.convert(response, TaskErrorResponse::class.java)
                return Result.failure(Exception("Error (${error?.status}): ${error?.message}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateTask(id: Long, task: Task): Result<String> {
        return try {
            val response = service.updateTask(id, task)
            if (response.isSuccessful)
                Result.success("${response.code()}: Task updated!")
            else {
                val error = errorConverter.convert(response, TaskErrorResponse::class.java)
                return Result.failure(Exception("Error (${error?.status}): ${error?.message}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteTask(id: Long): Result<String> {
        return try {
            val response = service.deleteTask(id)
            if (response.isSuccessful)
                Result.success("${response.code()}: Task deleted!")
            else {
                val error = errorConverter.convert(response, TaskErrorResponse::class.java)
                return Result.failure(Exception("Error (${error?.status}): ${error?.message}"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

