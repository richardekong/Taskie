package com.daveace.taskie.model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daveace.taskie.api.model.Task
import com.daveace.taskie.api.model.Tasks
import com.daveace.taskie.repository.TaskRepository
import com.daveace.taskie.state.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(private val taskRepository: TaskRepository, context: Context) : ViewModel() {
    private val _createdTaskState = MutableStateFlow<UIState<String>>(UIState.Idle)
    val createdTaskState: StateFlow<UIState<String>> = _createdTaskState.asStateFlow()
    private val _fetchedTaskState = MutableStateFlow<UIState<Task?>>(UIState.Idle)
    val fetchedTaskState: StateFlow<UIState<Task?>> = _fetchedTaskState.asStateFlow()
    private val _fetchedTasksState = MutableStateFlow<UIState<Tasks?>>(UIState.Loading)
    val fetchedTasksState: StateFlow<UIState<Tasks?>> = _fetchedTasksState.asStateFlow()

    private val _updatedTaskState = MutableStateFlow<UIState<String>>(UIState.Idle)
    val updatedTaskState : StateFlow<UIState<String>> = _updatedTaskState.asStateFlow()

    private val _deletedTaskState = MutableStateFlow<UIState<String>>(UIState.Idle)
    val deletedTaskState : StateFlow<UIState<String>> = _deletedTaskState.asStateFlow()

    init {
        readTasks()
    }

    fun createTask(task: Task) {
        viewModelScope.launch {
            _createdTaskState.value = UIState.Idle
            taskRepository.createTask(task)
                .onSuccess {
                    _createdTaskState.value = UIState.Success(it)
                    readTasks()
                }
                .onFailure {
                    _createdTaskState.value = UIState.Error(it.message ?: "Failed to create task!")
                }
        }
    }

    fun readTasks(params: Map<String, String> = mapOf()) {
        viewModelScope.launch {
            _fetchedTasksState.value = UIState.Loading
            val results: Result<Tasks?> = if (params.isEmpty()) {
                taskRepository.readTasks()
            } else {
                taskRepository.readTasks(params)
            }
            results.onSuccess { tasks ->
                _fetchedTasksState.value = UIState.Success(tasks)
            }.onFailure { error ->
                _fetchedTasksState.value = UIState.Error(error.message ?: "Failed to retrieve task!")
            }
        }
    }

    fun readTaskById(id: Long){
        viewModelScope.launch {
            _fetchedTaskState.value = UIState.Loading
            taskRepository.readTaskById(id)
                .onSuccess { task ->
                    _fetchedTaskState.value = UIState.Success(task)
                }
                .onFailure { error ->
                    _fetchedTaskState.value = UIState.Error(error.message ?: "Failed to retrieve Task!")
                }
        }
    }

    fun readTasksByTitle(title:String){
        viewModelScope.launch {
            _fetchedTasksState.value = UIState.Loading
            taskRepository.readTasksByTitle(title)
                .onSuccess { tasks ->
                    _fetchedTasksState.value = UIState.Success(tasks)
                }
                .onFailure {  error ->
                    _fetchedTasksState.value = UIState.Error(error.message?:"Failed to retrieve Tasks!")
                }
        }
    }

    fun readTasksByStatus(status:String){
        viewModelScope.launch {
            _fetchedTasksState.value = UIState.Loading
            taskRepository.readTasksByStatus(status)
                .onSuccess { tasks ->
                    _fetchedTasksState.value = UIState.Success(tasks)
                }
                .onFailure {  error ->
                    _fetchedTasksState.value = UIState.Error(error.message?:"Failed to retrieve Tasks!")
                }
        }
    }

    fun updateTask(id:Long, task:Task){
        viewModelScope.launch {
            _updatedTaskState.value = UIState.Idle
            taskRepository.updateTask(id, task)
                .onSuccess {
                    _updatedTaskState.value = UIState.Success(it)
                    readTasks()
                }
                .onFailure {
                    _updatedTaskState.value = UIState.Error(it.message?:"Failed to update task!")
                }
        }
    }

    fun deleteTask(id:Long){
        viewModelScope.launch {
            _deletedTaskState.value = UIState.Idle
            taskRepository.deleteTask(id)
                .onSuccess {
                    _deletedTaskState.value = UIState.Success(it)
                    readTasks()
                }
                .onFailure{
                    _deletedTaskState.value = UIState.Error(it.message?:"Failed to delete task!")
                }
        }
    }
}

