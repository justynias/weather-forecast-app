package com.just.weatherforecast.data.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.just.weatherforecast.data.db.entity.Weather
import java.util.*


class WeatherClassConverter {
    private val gson = Gson()
    @TypeConverter
    fun stringToList(data: String?): List<Weather> {
        if (data == null) {
            return Collections.emptyList()
        }

        val listType = object : TypeToken<List<Weather>>() {

        }.type
        return gson.fromJson<List<Weather>>(data, listType)
    }

    @TypeConverter
    fun listToString(someObjects: List<Weather>): String {
        return gson.toJson(someObjects)
    }


}