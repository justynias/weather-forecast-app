package com.just.weatherforecast.data

import com.just.weatherforecast.data.db.entity.CurrentWeatherResponse
import java.lang.Exception

sealed class Result<out T: Any> {
    data class Success<out T : CurrentWeatherResponse?>(val data: T) : Result<Any>()
    //data class Error(val exception: Exception) : Result<Nothing>()
    data class Error<out T : Exception?>(val data: T) : Result<Any>()

}