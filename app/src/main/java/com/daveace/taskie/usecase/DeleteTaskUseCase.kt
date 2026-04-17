package com.daveace.taskie.usecase

import com.daveace.taskie.repository.TaskRepository
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(private val repo: TaskRepository){

    suspend operator fun invoke(id:Long): Result<String> = repo.deleteTask(id)

}

