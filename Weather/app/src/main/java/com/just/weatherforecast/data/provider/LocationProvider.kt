package com.just.weatherforecast.data.provider


interface LocationProvider {
    suspend fun getPreferredLocationString(): String?
}