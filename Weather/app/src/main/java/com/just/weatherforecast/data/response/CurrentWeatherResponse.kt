package com.just.weatherforecast.data.response

import com.google.gson.annotations.SerializedName

data class CurrentWeatherResponse(

    @SerializedName("coord")
    val coordinates: Coordinates,
    val weather: List<Weather>,
    @SerializedName("main")
    val mainWeatherParameters: MainWeatherParameters,
    val wind: Wind,
    val clouds: Clouds,
    val rain: Rain,
    @SerializedName("dt")
    val dataTime: Int,
    val sys: Sys,
    @SerializedName("id")
    val cityId: Int,
    @SerializedName("name")
    val cityName: String,

    //internal parameters
    val cod: Int,
    val base: String

)