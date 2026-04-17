package com.daveace.taskie.usecase

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object TaskieUseCaseModules {

    @Provides
    @ViewModelScoped
    fun provideTaskUseCases(
        createTaskUseCase: CreateTaskUseCase,
        readTaskByIdUseCase: ReadTaskByIdUseCase,
        readTasksUseCase: ReadTasksUseCase,
        readTasksByParamUseCase: ReadTasksByParamUseCase,
        readTasksByTitleUseCase: ReadTasksByTitleUseCase,
        readTasksByStatusUseCase: ReadTasksByStatusUseCase,
        updateTaskUseCase: UpdateTaskUseCase,
        deleteTaskUseCase: DeleteTaskUseCase
    ): TaskUseCases = TaskUseCases(
        createTaskUseCase = createTaskUseCase,
        readTaskByIdUseCase = readTaskByIdUseCase,
        readTasksUseCase = readTasksUseCase,
        readTasksByParamUseCase = readTasksByParamUseCase,
        readTasksByTitleUseCase = readTasksByTitleUseCase,
        readTasksByStatusUseCase = readTasksByStatusUseCase,
        updateTaskUseCase = updateTaskUseCase,
        deleteTaskUseCase = deleteTaskUseCase
    )

}

