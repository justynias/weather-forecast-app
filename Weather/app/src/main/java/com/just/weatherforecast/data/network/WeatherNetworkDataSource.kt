package com.just.weatherforecast.data.network

import androidx.lifecycle.LiveData
import com.just.weatherforecast.data.db.entity.CurrentWeatherResponse
import java.lang.Exception

interface WeatherNetworkDataSource {
    val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>
    val error: LiveData<Exception>

suspend fun fetchCurrentWeather(
    lan: String?,
    lon: String?,
    customLocation: String?
    )
}