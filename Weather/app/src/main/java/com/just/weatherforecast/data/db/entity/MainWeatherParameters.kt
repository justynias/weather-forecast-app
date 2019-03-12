package com.just.weatherforecast.data.db.entity

import com.google.gson.annotations.SerializedName

data class MainWeatherParameters(
    @SerializedName("temp")
    val temperatureC: Double,
    val pressure: Double,
    val humidity: Double,
    @SerializedName("temp_min")
    val temperatureMin: Double,
    @SerializedName("temp_max")
    val temperatureMax: Double

)