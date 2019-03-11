package com.just.weatherforecast.data.network.response

import com.google.gson.annotations.SerializedName
import com.just.weatherforecast.data.db.entity.*

data class CurrentWeatherResponse(

    val weather: List<Weather>,
    @SerializedName("main")
    val mainWeatherParameters: MainWeatherParameters,
    val wind: Wind,
    val rain: Rain,
    @SerializedName("sys")
    val sun: Sun
)