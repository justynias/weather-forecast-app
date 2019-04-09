package com.just.weatherforecast.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.just.weatherforecast.data.Result
import com.just.weatherforecast.data.WeatherApiService
import com.just.weatherforecast.data.db.entity.CurrentWeatherResponse
import com.just.weatherforecast.internal.NoConnectivityException
import retrofit2.Response
import java.io.IOException
import java.lang.Exception

class WeatherNetworkDataSourceImpl(
    private val weatherApiService: WeatherApiService
) : WeatherNetworkDataSource {
    private val _downloadedCurrentWeather = MutableLiveData<CurrentWeatherResponse>()
    override val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>
        get() = _downloadedCurrentWeather

    private val _error = MutableLiveData<Exception>()
    override val error: LiveData<Exception>
        get() = _error

        override suspend fun fetchCurrentWeather(lat: String?, lon: String?, customLocation: String?) {
//       val fetchedCurrentWeather: CurrentWeatherResponse?=  try {
//           if (customLocation != null) {
//               weatherApiService.getCurrentWeatherByCityAsync(customLocation).await()
//           } else {
//               weatherApiService.getCurrentWeatherByCoordAsync(lat!!, lon!!).await() //shoulld be non null
//           }
//       }catch(e: NoConnectivityException){
//           null
//           }
//
//            fetchedCurrentWeather?.let { _downloadedCurrentWeather.postValue(it) }
            var response:Response<CurrentWeatherResponse>
            try{

                response = if (customLocation != null) {
                    weatherApiService.getCurrentWeatherByCityAsync(customLocation).await()
                } else {
                    weatherApiService.getCurrentWeatherByCoordAsync(lat!!, lon!!).await()
                }
                if(response.isSuccessful) {
                    _downloadedCurrentWeather.postValue(Result.Success(response.body()).data)
                }
                else{

                    _error.postValue(IOException("Sorry, no results for this location"))
                }

            }catch(e: NoConnectivityException){
                _error.postValue(IOException("Please check your internet connection"))

            }


        }

    }
