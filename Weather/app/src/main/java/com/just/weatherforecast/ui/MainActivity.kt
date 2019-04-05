package com.just.weatherforecast.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.just.weatherforecast.R
import com.just.weatherforecast.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), KodeinAware, CoroutineScope {
    override val kodein by closestKodein()
    private val viewModelFactory: MainViewModelFactory by instance()
    private lateinit var mainViewModel: MainViewModel

    protected lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        content.visibility= View.GONE
        //loader.visibility=View.VISIBLE
        mainViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)

        DataBindingUtil.setContentView<ActivityMainBinding>(
            this, R.layout.activity_main
        ).apply {
            this.setLifecycleOwner(this@MainActivity)
            this.viewModel = mainViewModel
        }

        job = Job()
        loadUI()


    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }



    private fun loadUI() = launch {
        val currentWeather = mainViewModel.weather.await()
        currentWeather.observe(this@MainActivity, Observer {
            it?.also{
                mainViewModel.setWeather(currentWeather)
               Log.d("CHANGE", currentWeather.value!![0].toString())
                //loader.playAnimation()
                loader.visibility=View.GONE
                content.visibility= View.VISIBLE


            }
        })

    }

}
