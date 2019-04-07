package com.just.weatherforecast.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.just.weatherforecast.data.WeatherApiService
import com.just.weatherforecast.data.db.entity.CurrentWeatherResponse
import com.just.weatherforecast.internal.NoConnectivityException

class WeatherNetworkDataSourceImpl(
    private val weatherApiService: WeatherApiService
) : WeatherNetworkDataSource {
    private val _downloadedCurrentWeather = MutableLiveData<CurrentWeatherResponse>()
    override val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>
        get() = _downloadedCurrentWeather

        override suspend fun fetchCurrentWeather(lat: String?, lon: String?, customLocation: String?) {
       val fetchedCurrentWeather: CurrentWeatherResponse?=  try {
           if (customLocation != null) {
               weatherApiService.getCurrentWeatherByCityAsync(customLocation).await()
           } else {
               weatherApiService.getCurrentWeatherByCoordAsync(lat!!, lon!!).await() //shoulld be non null
           }
       }catch(e: NoConnectivityException){
           null
           }
            fetchedCurrentWeather?.let { _downloadedCurrentWeather.postValue(it) }
        }

    }
