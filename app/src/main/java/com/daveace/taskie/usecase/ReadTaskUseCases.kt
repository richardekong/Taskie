package com.daveace.taskie.usecase

import com.daveace.taskie.api.model.Task
import com.daveace.taskie.api.model.Tasks
import com.daveace.taskie.repository.TaskRepository
import javax.inject.Inject

class ReadTasksUseCase @Inject constructor(private val repo: TaskRepository) {

    suspend operator fun invoke(): Result<Tasks?> = repo.readTasks()

}

class ReadTasksByParamUseCase @Inject constructor(private val repo: TaskRepository){

    suspend operator fun invoke(params:Map<String,String>): Result<Tasks?> = repo.readTasks(params)

}

class ReadTasksByTitleUseCase @Inject constructor(private val repo: TaskRepository) {

    suspend operator fun invoke(title: String): Result<Tasks?> = repo.readTasksByTitle(title)

}

class ReadTasksByStatusUseCase @Inject constructor(private val repo: TaskRepository) {

    suspend operator fun invoke(status: String): Result<Tasks?> = repo.readTasksByStatus(status)

}

class ReadTaskByIdUseCase @Inject constructor(private val repo: TaskRepository) {

    suspend operator fun invoke(id: Long): Result<Task?> = repo.readTaskById(id)

}

