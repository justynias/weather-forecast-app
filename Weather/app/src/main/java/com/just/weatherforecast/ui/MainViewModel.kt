package com.just.weatherforecast.ui

import android.icu.util.TimeZone
import android.provider.Settings.Global.getString
import android.util.Log

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.just.weatherforecast.R
import com.just.weatherforecast.data.db.entity.CurrentWeatherResponse
import com.just.weatherforecast.data.repository.ForecastRepository
import com.just.weatherforecast.internal.lazyDeffered
import kotlinx.coroutines.Deferred
import java.util.*
import kotlin.math.roundToInt
import kotlin.math.roundToLong

class MainViewModel(private val forecastRepository: ForecastRepository): ViewModel(){

    val weather by lazyDeffered {
        forecastRepository.getCurrentWeather()
    }

    suspend fun setCustomLocalization(localization: String){
        forecastRepository.setCustomLocation(localization)
    }

    suspend fun setDeviceLocation(){
        forecastRepository.setDeviceLocation()
    }

    private fun convertTimestampToTime(date: Long): String{
        val sdf = java.text.SimpleDateFormat("HH:mm")
        sdf.timeZone = java.util.TimeZone.getDefault()
        val converted = date?.times(1000)?.let { java.util.Date(it) }
         return sdf.format(converted)

    }


    fun setWeather(weather: LiveData<CurrentWeatherResponse>){
//unit = metric
        _weatherDescription.value= weather.value?.weathers?.get(0)?.description
        _weatherLocation.value= weather.value?.localization
        _weatherTemp.value= weather.value?.mainWeatherParameters?.temperatureC?.roundToInt().toString() //+  &#x2103;
        _weatherTempMin.value= weather.value?.mainWeatherParameters?.temperatureMin?.roundToInt().toString()
        _weatherTempMax.value= weather.value?.mainWeatherParameters?.temperatureMax?.roundToInt().toString()
        _weatherSunrise.value= weather.value?.sun?.sunrise?.toLong()?.let { convertTimestampToTime(it) }
        _weatherSunset.value= weather.value?.sun?.sunset?.toLong()?.let { convertTimestampToTime(it) }

        _weatherHumidity.value= weather.value?.mainWeatherParameters?.humidity?.toInt().toString()
        _weatherPressure.value= weather.value?.mainWeatherParameters?.pressure?.toInt().toString()

        _weatherWindDirection.value= weather.value?.wind?.direction?.toInt().toString()
        _weatherWindSpeed.value=String.format("%.1f",  weather.value?.wind?.speed)
        _weatherIconId.value= "wi_day_rain"
        //Log.d("ICON", getString(R.string.app_name))
        //icon weather.value?.weathers?.get(0)?.weatherIcon
    }

    private val _weatherIconId: MutableLiveData<String> = MutableLiveData()
    val weatherIconId:LiveData<String>
        get() = _weatherIconId


    private val _weatherTemp: MutableLiveData<String> = MutableLiveData()
    val weatherTemp:LiveData<String>
        get() = _weatherTemp

    private val _weatherTempMin: MutableLiveData<String> = MutableLiveData()
    val weatherTempMin:LiveData<String>
        get() = _weatherTempMin

    private val _weatherTempMax: MutableLiveData<String> = MutableLiveData()
    val weatherTempMax:LiveData<String>
        get() = _weatherTempMax

    private val _weatherWindSpeed: MutableLiveData<String> = MutableLiveData()
    val weatherWindSpeed:LiveData<String>
        get() = _weatherWindSpeed

    private val _weatherWindDirection: MutableLiveData<String> = MutableLiveData()
    val weatherWindDirection:LiveData<String>
        get() = _weatherWindDirection

    private val _weatherPressure: MutableLiveData<String> = MutableLiveData()
    val weatherPressure:LiveData<String>
        get() = _weatherPressure

    private val _weatherHumidity: MutableLiveData<String> = MutableLiveData()
    val weatherHumidity:LiveData<String>
        get() = _weatherHumidity
    private val _weatherSunrise: MutableLiveData<String> = MutableLiveData()
    val weatherSunrise:LiveData<String>
        get() = _weatherSunrise
    private val _weatherSunset: MutableLiveData<String> = MutableLiveData()
    val weatherSunset:LiveData<String>
        get() = _weatherSunset

    private val _weatherDescription: MutableLiveData<String> = MutableLiveData()
    val weatherDescription:LiveData<String>
        get() = _weatherDescription

    private val _weatherLocation: MutableLiveData<String> = MutableLiveData()
    val weatherLocation:LiveData<String>
        get() = _weatherLocation
}