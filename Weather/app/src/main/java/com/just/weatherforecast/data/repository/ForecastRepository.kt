package com.just.weatherforecast.data.repository

import androidx.lifecycle.LiveData
import com.just.weatherforecast.data.db.entity.CurrentWeatherResponse

interface ForecastRepository {
    suspend fun getCurrentWeather(): LiveData<CurrentWeatherResponse>
    suspend fun setCustomLocation(customLocation: String)
    suspend fun setDeviceLocation()
}