package com.just.weatherforecast.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.just.weatherforecast.data.db.entity.CURRENT_WEATHER_ID
import com.just.weatherforecast.data.db.entity.CurrentWeatherResponse

@Dao
interface CurrentWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
   fun upsert(currentWeatherResponse: CurrentWeatherResponse):Long

    @Query("select * from current_weather")
    fun getCurrentWeatherList(): LiveData<List<CurrentWeatherResponse>>


//    @Query("select * from current_weather where id =$CURRENT_WEATHER_ID")
//    fun getCurrentWeather(): LiveData<CurrentWeatherResponse>
//
//    @Query("select * from current_weather")
//    fun getCurrentWeatherListNotLive(): List<CurrentWeatherResponse>


}