package com.just.weatherforecast.data.response

import com.google.gson.annotations.SerializedName

data class Rain(
    @SerializedName("3h")
    val volume: Int
)