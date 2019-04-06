package com.just.weatherforecast.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.just.weatherforecast.data.db.CurrentWeatherDao
import com.just.weatherforecast.data.db.entity.CurrentWeatherResponse
import com.just.weatherforecast.data.network.WeatherNetworkDataSource
import com.just.weatherforecast.data.provider.LocationProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val CURRENT_WEATHER_ID  = 0 //it was autogenerating without that

class ForecastRepositoryImpl(
    private val currentWeatherDao:CurrentWeatherDao,
    private val weatherNetworkDataSource:WeatherNetworkDataSource,
    private val locationProvider: LocationProvider
) : ForecastRepository {



    init{
        weatherNetworkDataSource.downloadedCurrentWeather.observeForever{newCurrentWeather->
            persistFetchedCurrentWeather(newCurrentWeather)
        }
    }

    override suspend fun getCurrentWeather():LiveData<List<CurrentWeatherResponse>>{
        return withContext(Dispatchers.IO){
            initWeatherData()
            return@withContext currentWeatherDao.getCurrentWeatherList()
        }
    }

    private fun persistFetchedCurrentWeather(fetchedWeather: CurrentWeatherResponse){
        GlobalScope.launch(Dispatchers.IO){
            fetchedWeather.id=CURRENT_WEATHER_ID

        }
    }

    private suspend fun initWeatherData(){
//temporary
        fetchCurrentWeather()
        return

        val lastWeather = currentWeatherDao?.getCurrentWeatherListNonLive()

//            if (lastWeather == null|| locationProvider.hasLocationChanged(lastWeather))
//                {
//                fetchCurrentWeather()
//                return
//            }

//        if (isFetchCurrentNeeded(lastWeatherLocation.zonedDateTime))
//            fetchCurrentWeather()

//
//        if(isFetchCurrentNeeded(ZonedDateTime.now().minusHours(1)) || currentWeatherDao==null)
//            fetchCurrentWeather()
    }



    private suspend fun fetchCurrentWeather() {
        val lat=locationProvider.getPreferredLocationString().substringBefore(",")
        val lon=locationProvider.getPreferredLocationString().substringAfter(",")

        weatherNetworkDataSource.fetchCurrentWeather(
            lat, lon
        )

    }

    //need to implement fetching after 0 minutes
//    private fun isFetchCurrentNeeded(lastFetchTime: ZonedDateTime): Boolean {
//        val thirtyMinutesAgo = ZonedDateTime.now().minusMinutes(30)
//        return lastFetchTime.isBefore(thirtyMinutesAgo)
//    }

}