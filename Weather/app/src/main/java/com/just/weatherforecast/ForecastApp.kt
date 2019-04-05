package com.just.weatherforecast

import android.app.Application
import android.preference.PreferenceManager
import com.just.weatherforecast.data.WeatherApiService
import com.just.weatherforecast.data.db.entity.CurrentWeatherResponse
import com.just.weatherforecast.data.db.entity.ForecastDatabase
import com.just.weatherforecast.data.network.ConnectivityInterceptor
import com.just.weatherforecast.data.network.ConnectivityInterceptorImpl
import com.just.weatherforecast.data.network.WeatherNetworkDataSource
import com.just.weatherforecast.data.network.WeatherNetworkDataSourceImpl
import com.just.weatherforecast.data.repository.ForecastRepository
import com.just.weatherforecast.data.repository.ForecastRepositoryImpl
import com.just.weatherforecast.ui.MainViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class ForecastApp: Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@ForecastApp))

        bind() from singleton { ForecastDatabase(instance()) }
        bind() from singleton { instance<ForecastDatabase>().currentWeatherDao() }
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
        bind() from singleton { WeatherApiService(instance()) }
        bind<WeatherNetworkDataSource>() with singleton { WeatherNetworkDataSourceImpl(instance())}
        bind () from provider {MainViewModelFactory(instance())}
        bind<ForecastRepository>() with singleton {ForecastRepositoryImpl(instance(), instance())}
    }

    override fun onCreate() {
        super.onCreate()
        //AndroidThreeTen.init(this)
    }

}