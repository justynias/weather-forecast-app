package com.just.weatherforecast.data

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.just.weatherforecast.data.db.entity.CurrentWeatherResponse
import com.just.weatherforecast.data.network.ConnectivityInterceptor
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
const val API_KEY = "2273110d6b60d3be9145b6038442f8af"

interface WeatherApiService {

    @GET("data/2.5/weather")
    fun getCurrentWeatherByCoord(
        //
        //@Query("q") location: String
        @Query("lat") lat: String,
        @Query("lon") lon: String

    ): Deferred<CurrentWeatherResponse>
    @GET("data/2.5/weather")
    fun getCurrentWeatherByCity(
        //
        @Query("q") location: String


    ): Deferred<CurrentWeatherResponse>
    //http://api.openweathermap.org/data/2.5/weather?lat=35&lon=139&appid=2273110d6b60d3be9145b6038442f8af
    //http://api.openweathermap.org/data/2.5/weather?q=London,uk&appid=2273110d6b60d3be9145b6038442f8af
    companion object {
        operator fun invoke(connectivityInterceptor: ConnectivityInterceptor): WeatherApiService {
            val requestInterceptor = Interceptor { chain ->

                val url = chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("APPID", API_KEY)
                    .build()
                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()

                return@Interceptor chain.proceed(request)
            }

            val okHttpClient = OkHttpClient.Builder().addInterceptor(connectivityInterceptor)
                .addInterceptor(requestInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://api.openweathermap.org/")
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WeatherApiService::class.java)
        }
    }
}