package com.daveace.taskie.api.error

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

class ErrorConverter @Inject constructor(private val retrofit: Retrofit) {

    fun <T> convert(response: Response<*>, clazz:Class<T>):T?{
        val body = response.errorBody()?: return null
        val converter: Converter<ResponseBody, T> =
            retrofit.responseBodyConverter(clazz, arrayOfNulls(0))
        return try{
            converter.convert(body)
        }catch (_:Exception){
            null
        }

    }
}