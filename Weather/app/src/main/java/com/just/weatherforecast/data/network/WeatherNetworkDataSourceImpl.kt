package com.just.weatherforecast.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.just.weatherforecast.data.WeatherApiService
import com.just.weatherforecast.data.db.entity.CurrentWeatherResponse
import com.just.weatherforecast.internal.NoConnectivityException
import java.io.NotActiveException

class WeatherNetworkDataSourceImpl(
    private val weatherApiService: WeatherApiService
) : WeatherNetworkDataSource {
    private val _downloadedCurrentWeather = MutableLiveData<CurrentWeatherResponse>()
    override val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>
        get() = _downloadedCurrentWeather

    override suspend fun fetchCurrentWeather(location: String) {
        try{
            val fetchedCurrentWeather= weatherApiService.getCurrentWeather("London").await()
            _downloadedCurrentWeather.postValue(fetchedCurrentWeather)
        }
        catch(e: NoConnectivityException){
            Log.d("CONNECTION", "NO INTERNET")
        }
    }
}