package com.just.weatherforecast.data.repository

import androidx.lifecycle.LiveData
import com.just.weatherforecast.data.db.entity.CurrentWeatherResponse
import java.lang.Exception

interface ForecastRepository {
    suspend fun getCurrentWeather(): LiveData<CurrentWeatherResponse>
    suspend fun getError(): LiveData<Exception>
    suspend fun setCustomLocation(customLocation: String)
    suspend fun setDeviceLocation()
}