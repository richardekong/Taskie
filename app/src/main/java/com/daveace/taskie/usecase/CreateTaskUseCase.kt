package com.daveace.taskie.usecase

import com.daveace.taskie.api.model.Task
import com.daveace.taskie.repository.TaskRepository
import javax.inject.Inject

class CreateTaskUseCase @Inject constructor(private val repo: TaskRepository){

    suspend operator fun invoke(task: Task): Result<String>{
        return repo.createTask(task)
    }

}

