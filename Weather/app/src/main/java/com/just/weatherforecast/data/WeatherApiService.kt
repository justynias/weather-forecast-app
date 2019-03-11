package com.just.weatherforecast.data

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.just.weatherforecast.data.network.response.CurrentWeatherResponse
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val API_KEY = "2273110d6b60d3be9145b6038442f8af"
//http://api.openweathermap.org/data/2.5/weather?q=London,uk&appid=2273110d6b60d3be9145b6038442f8af
interface WeatherApiService {

        @GET("data/2.5/weather")
        fun getCurrentWeather(
            @Query("q") location: String
        ): Deferred<CurrentWeatherResponse>




        companion object {
            operator fun invoke(): WeatherApiService {
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

                val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(requestInterceptor)
                    .build()

                return Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("http://api.openweathermap.org/")
                    .addCallAdapterFactory(CoroutineCallAdapterFactory())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(WeatherApiService::class.java)
            }
        }
}