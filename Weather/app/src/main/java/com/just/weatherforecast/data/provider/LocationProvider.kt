package com.just.weatherforecast.data.provider

import com.just.weatherforecast.data.db.entity.CurrentWeatherResponse

interface LocationProvider {
    suspend fun hasLocationChanged(currentWeatherResponse: CurrentWeatherResponse): Boolean
    suspend fun getPreferredLocationString(): String?

}