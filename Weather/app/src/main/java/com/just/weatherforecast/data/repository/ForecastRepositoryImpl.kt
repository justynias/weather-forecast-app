package com.just.weatherforecast.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.just.weatherforecast.data.db.CurrentWeatherDao
import com.just.weatherforecast.data.db.entity.CurrentWeatherResponse
import com.just.weatherforecast.data.network.WeatherNetworkDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.ZonedDateTime

class ForecastRepositoryImpl(
    private val currentWeatherDao:CurrentWeatherDao,
    private val weatherNetworkDataSource:WeatherNetworkDataSource
) : ForecastRepository {

    init{
        weatherNetworkDataSource.downloadedCurrentWeather.observeForever{newCurrentWeather->
            persistFetchedCurrentWeather(newCurrentWeather)
        }
    }

    override suspend fun getCurrentWeather():LiveData<List<CurrentWeatherResponse>>{
        return withContext(Dispatchers.IO){
            initWeatherData()
            Log.d("DUPA", "new get")
            return@withContext currentWeatherDao.getCurrentWeatherList()
        }
    }

    private fun persistFetchedCurrentWeather(fetchedWeather:CurrentWeatherResponse){
        GlobalScope.launch(Dispatchers.IO){
            currentWeatherDao.upsert(fetchedWeather)
        }
    }

    private suspend fun initWeatherData(){
        if(isFetchCurrentNeeded(ZonedDateTime.now().minusHours(1)) || currentWeatherDao==null)
            fetchCurrentWeather()
    }



    private suspend fun fetchCurrentWeather() {
        weatherNetworkDataSource.fetchCurrentWeather(
            "London"
        )

    }

    private fun isFetchCurrentNeeded(lastFetchTime: ZonedDateTime): Boolean {
        val thirtyMinutesAgo = ZonedDateTime.now().minusMinutes(30)
        return lastFetchTime.isBefore(thirtyMinutesAgo)
    }

}