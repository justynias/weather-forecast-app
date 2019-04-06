package com.just.weatherforecast.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.just.weatherforecast.data.db.entity.CurrentWeatherResponse

@Dao
interface CurrentWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
   fun upsert(currentWeatherResponse: CurrentWeatherResponse):Long

    @Query("select * from current_weather")
    fun getCurrentWeatherList(): LiveData<List<CurrentWeatherResponse>>

    @Query("select * from current_weather")
    fun getCurrentWeatherListNonLive(): List<CurrentWeatherResponse>


}