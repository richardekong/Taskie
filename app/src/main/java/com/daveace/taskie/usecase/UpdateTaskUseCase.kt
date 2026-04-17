package com.daveace.taskie.usecase

import com.daveace.taskie.api.model.Task
import com.daveace.taskie.repository.TaskRepository
import javax.inject.Inject

class UpdateTaskUseCase @Inject constructor(private val repo: TaskRepository){

    suspend operator fun invoke(id:Long, task: Task):Result<String> = repo.updateTask(id, task)

}