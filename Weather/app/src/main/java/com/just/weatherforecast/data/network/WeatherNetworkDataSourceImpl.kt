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

   // override suspend fun fetchCurrentWeather(location: String) {
        override suspend fun fetchCurrentWeather(lat: String, lon:String) {

            Log.d("CITY LAT", lat)
            Log.d("CITY LON", lon)

       try{
            //val fetchedCurrentWeather= weatherApiService.getCurrentWeather(location).await()
            val fetchedCurrentWeather= weatherApiService.getCurrentWeatherByCoord(lat, lon).await()
           //val fetchedCurrentWeather= weatherApiService.getCurrentWeatherByCity("Katowice").await()

           Log.d("CITY", fetchedCurrentWeather.toString())
            _downloadedCurrentWeather.postValue(fetchedCurrentWeather)
        }
        catch(e: NoConnectivityException){
            Log.d("CONNECTION", "NO INTERNET")
        }
    }
}