package com.just.weatherforecast.data.response

data class Sys(

    val country: String,
    val sunrise: Int,
    val sunset: Int,
    //internal parameters
    val type: Int,
    val id: Int,
    val message: Double

)