package com.just.weatherforecast.data.db.entity

import com.google.gson.annotations.SerializedName

data class Wind(
    val speed: Double,
    @SerializedName("deg")
    val direction: Int
)