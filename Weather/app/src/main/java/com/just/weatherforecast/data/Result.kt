package com.just.weatherforecast.data

import com.just.weatherforecast.data.db.entity.CurrentWeatherResponse

sealed class Result<out T: Any> {
    data class Success<out T : CurrentWeatherResponse?>(val data: T) : Result<Any>()
    data class Error(val exception: Exception) : Result<Nothing>()
}