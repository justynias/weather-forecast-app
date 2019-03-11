package com.just.weatherforecast.data.response

import com.google.gson.annotations.SerializedName

data class Weather(
    val id: Int,
    @SerializedName("main")
    val mainDescription: String,
    val description: String,
    @SerializedName("icon")
    val weatherIcon: String
)