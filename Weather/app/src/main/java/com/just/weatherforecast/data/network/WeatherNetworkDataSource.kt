package com.just.weatherforecast.data.network

import androidx.lifecycle.LiveData
import com.just.weatherforecast.data.db.entity.CurrentWeatherResponse

interface WeatherNetworkDataSource {
    val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>
//    suspend fun fetchCurrentWeather(
////        location: String
////    )
suspend fun fetchCurrentWeather(
    lan: String,
    lon: String
    )
}