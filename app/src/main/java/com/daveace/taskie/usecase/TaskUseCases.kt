package com.daveace.taskie.usecase

data class TaskUseCases(
    val createTaskUseCase: CreateTaskUseCase,
    val readTaskByIdUseCase: ReadTaskByIdUseCase,
    val readTasksUseCase: ReadTasksUseCase,
    val readTasksByParamUseCase: ReadTasksByParamUseCase,
    val readTasksByTitleUseCase: ReadTasksByTitleUseCase,
    val readTasksByStatusUseCase: ReadTasksByStatusUseCase,
    val updateTaskUseCase: UpdateTaskUseCase,
    val deleteTaskUseCase: DeleteTaskUseCase
)

