package com.just.weatherforecast.data.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.just.weatherforecast.data.WeatherApiService
import com.just.weatherforecast.data.db.entity.CurrentWeatherResponse
import com.just.weatherforecast.internal.NoConnectivityException
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
            try{

               val response= if (customLocation != null) {
                    weatherApiService.getCurrentWeatherByCityAsync(customLocation).await()
                } else {
                    weatherApiService.getCurrentWeatherByCoordAsync(lat!!, lon!!).await()
                }
                if(response.isSuccessful) {
                    _downloadedCurrentWeather.postValue(response.body())

                } else{ _error.postValue(IOException(response.message())) }

            }catch(e: NoConnectivityException) {
                _error.postValue(IOException("Please check your internet connection"))
            }
            catch (e: Throwable) {
                _error.postValue(IOException("Ooops: Something else went wrong"))
            }
        }

    }
