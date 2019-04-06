package com.just.weatherforecast.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.just.weatherforecast.R
import com.just.weatherforecast.databinding.ActivityMainBinding
import com.just.weatherforecast.internal.asDeferred
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import kotlin.coroutines.CoroutineContext

private const val MY_PERMISSION_ACCESS_COARSE_LOCATION = 1

class MainActivity : AppCompatActivity(), KodeinAware, CoroutineScope {

    //new code

    private val fusedLocationProviderClient: FusedLocationProviderClient by instance()

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult?) {
            super.onLocationResult(p0)
        }
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
            MY_PERMISSION_ACCESS_COARSE_LOCATION
        )
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }
    private val locationRequest = LocationRequest().apply {
        interval = 10000
        fastestInterval = 10000
        priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
    }

    @SuppressLint("MissingPermission")
    fun startLocationUpdates() {
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    //
    override val kodein by closestKodein()
    private val viewModelFactory: MainViewModelFactory by instance()
    private lateinit var mainViewModel: MainViewModel

    protected lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main



    @SuppressLint("MissingPermission")
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
        //fusedLocationProviderClient.
        requestLocationPermission()
        if (hasLocationPermission()) {
            Log.d("LOCATION", "has permission")
            startLocationUpdates()
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
               Log.d("CITY UI", currentWeather.value.toString())
                //loader.playAnimation()
                loader.visibility=View.GONE
                content.visibility= View.VISIBLE


            }
        })

    }

}
