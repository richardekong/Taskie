package com.daveace.taskie.repository


import com.daveace.taskie.api.model.Task
import com.daveace.taskie.api.model.Tasks

interface TaskRepository{

    suspend fun createTask(task:Task):Result<String>

    suspend fun readTasks(params: Map<String, String>): Result<Tasks?>

    suspend fun readTasks(): Result<Tasks?>

    suspend fun readTasksByTitle(title: String): Result<Tasks?>

    suspend fun readTasksByStatus(status: String): Result<Tasks?>

    suspend fun readTaskById(id: Long): Result<Task?>

    suspend fun updateTask(id: Long, task: Task): Result<String>

    suspend fun deleteTask(id: Long): Result<String>

}

