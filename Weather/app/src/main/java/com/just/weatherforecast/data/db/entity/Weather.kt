package com.just.weatherforecast.data.db.entity

import com.google.gson.annotations.SerializedName


data class Weather(
    val description: String,
    @SerializedName("icon")
    val weatherIcon: String
)
