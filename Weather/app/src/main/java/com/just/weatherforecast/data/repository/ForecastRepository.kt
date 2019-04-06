package com.just.weatherforecast.data.repository

import androidx.lifecycle.LiveData
import com.just.weatherforecast.data.db.entity.CurrentWeatherResponse

interface ForecastRepository {
    suspend fun getCurrentWeather(): LiveData<List<CurrentWeatherResponse>>
   // suspend fun getWeatherLocation(): LiveData<WeatherLocation>
}