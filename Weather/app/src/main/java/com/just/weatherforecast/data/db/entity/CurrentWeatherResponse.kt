package com.just.weatherforecast.data.db.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.just.weatherforecast.data.converter.WeatherClassConverter

const val CURRENT_WEATHER_ID  = 0

@Entity(tableName = "current_weather")
data class CurrentWeatherResponse(
    @TypeConverters(WeatherClassConverter:: class)
    @SerializedName("weather")
    val weathers: List<Weather>,
    @SerializedName("main")
    @Embedded(prefix = "mainWeatherParameters_")
    val mainWeatherParameters: MainWeatherParameters,
    @Embedded(prefix = "wind_")
    val wind: Wind,
//    @Embedded(prefix = "rain_")
//    val rain: Rain,
    @SerializedName("sys")
    @Embedded(prefix = "sun_")
    val sun: Sun
)
{
    @PrimaryKey(autoGenerate = false)
    var id: Int = CURRENT_WEATHER_ID
}