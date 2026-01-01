package com.daveace.taskie.api

import retrofit2.Response
import retrofit2.http.*



interface TaskService {

    @POST("/api/tasks")
    suspend fun createTask(@Body task:Task)

    @GET("/api/tasks")
    suspend fun readTasks(): Response<Tasks>

    @GET("/api/tasks")
    suspend fun readTasks(@QueryMap params: Map<String,String>):Response<Tasks>

    @GET("/api/tasks/{id}")
    suspend fun readTask(@Path("id") id:Long): Response<Task>

    @GET("/api/tasks/search-title")
    suspend fun readTaskByTitle(@Query("title")title:String):Response<Tasks>

    @GET("/api/tasks/search-status")
    suspend fun readTaskByStatus(@Query("status")status:String):Response<Tasks>

    @PUT("/api/tasks/{id}")
    suspend fun updateTask(@Path("id")id:Long, @Body task: Task)

    @DELETE("/api/tasks/{id}")
    suspend fun deleteTask(@Path("id") id:Long)

}

