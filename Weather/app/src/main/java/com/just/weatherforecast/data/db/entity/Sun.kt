package com.just.weatherforecast.data.db.entity


import java.sql.Date
import java.sql.Timestamp

data class Sun(
//   // @Embedded(prefix = "sunrise_")
//    val sunrise: Date,
//   // @Embedded(prefix = "sunset_")
//    val sunset: Date
    val sunrise: Int,
    val sunset: Int
)