package com.just.weatherforecast.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.just.weatherforecast.data.Result
import com.just.weatherforecast.data.db.CurrentWeatherDao
import com.just.weatherforecast.data.db.entity.CurrentWeatherResponse
import com.just.weatherforecast.data.network.WeatherNetworkDataSource
import com.just.weatherforecast.data.provider.LocationProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

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

//    fun setLocationPreferences(customLocation: String){
//        //locationProvider.setLocationPreferences(cutomLocation)
//    }
    override suspend fun getCurrentWeather():LiveData<CurrentWeatherResponse>{
        return withContext(Dispatchers.IO){
            initWeatherData(null)
            return@withContext currentWeatherDao.getCurrentWeather()
        }
    }
    override suspend fun getError():LiveData<Exception>{
        return weatherNetworkDataSource.error
    }
    override suspend fun setCustomLocation(customLocation: String){
        initWeatherData(customLocation)
    }

    private fun persistFetchedCurrentWeather(fetchedWeather: CurrentWeatherResponse){
        GlobalScope.launch(Dispatchers.IO){
            fetchedWeather.id=CURRENT_WEATHER_ID
            currentWeatherDao.upsert(fetchedWeather)
        }
    }

    private suspend fun initWeatherData(customLocation: String?){
//temporary

        if(customLocation!=null){
            fetchCurrentWeatherByLocation(customLocation)
        }
        else{
           fetchCurrentWeatherByCoordinates()
        }

      //  val lastWeather = currentWeatherDao?.getCurrentWeatherListNonLive()

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



    private suspend fun fetchCurrentWeatherByLocation(customLocation: String) {

        weatherNetworkDataSource.fetchCurrentWeather(
            null,
            null,
            customLocation
        )

    }
    private suspend fun fetchCurrentWeatherByCoordinates() {
        val lat=locationProvider.getPreferredLocationString()?.substringBefore(",")
        val lon=locationProvider.getPreferredLocationString()?.substringAfter(",")

        if(lat!=null && lon!=null){
            Log.d("FETCH", "fetch in repo")

            weatherNetworkDataSource.fetchCurrentWeather(
                lat, lon, null
            )
        }
    }

    override suspend fun setDeviceLocation(){
        fetchCurrentWeatherByCoordinates()
    }
    //need to implement fetching after 0 minutes
//    private fun isFetchCurrentNeeded(lastFetchTime: ZonedDateTime): Boolean {
//        val thirtyMinutesAgo = ZonedDateTime.now().minusMinutes(30)
//        return lastFetchTime.isBefore(thirtyMinutesAgo)
//    }

}