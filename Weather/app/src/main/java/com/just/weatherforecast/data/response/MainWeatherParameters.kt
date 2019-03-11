package com.just.weatherforecast.data.response

import com.google.gson.annotations.SerializedName

data class MainWeatherParameters(
    @SerializedName("temp")
    val temperatureC: Double,
    val pressure: Int,
    val humidity: Int,
    @SerializedName("temp_min")
    val temperatureMin: Double,
    @SerializedName("temp_max")
    val temperatureMax: Double
)