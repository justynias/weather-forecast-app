package com.just.weatherforecast.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.just.weatherforecast.data.repository.ForecastRepository

class MainViewModelFactory(
    private val forecastRepository: ForecastRepository
): ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MainViewModel(forecastRepository) as T
        }
}
