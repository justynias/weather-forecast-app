package com.just.weatherforecast.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.just.weatherforecast.data.db.entity.CurrentWeatherResponse

@Dao
interface CurrentWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(currentWeatherResponse: CurrentWeatherResponse)
}