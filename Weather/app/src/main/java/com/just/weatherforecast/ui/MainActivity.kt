package com.just.weatherforecast.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.github.pwittchen.weathericonview.WeatherIconView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.just.weatherforecast.R
import com.just.weatherforecast.databinding.ActivityMainBinding
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
    private lateinit var errorDialog:AlertDialog
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
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
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




    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (hasLocationPermission()) {
            startLocationUpdates()
            launch{
                content.visibility = View.GONE
                loader.visibility = View.VISIBLE
                mainViewModel.setDeviceLocation()
            }
        }

    }



    private fun loadUI() = launch {
        val currentWeather = mainViewModel.weather.await()
        val error = mainViewModel.error.await()

        error.observe(this@MainActivity, Observer {
            it?.also {
                Log.d("ERROR", it.message)
                errorDialog.setMessage(it.message)
                errorDialog.show()
                loader.visibility = View.GONE
                content.visibility = View.VISIBLE
            }
        })
        currentWeather.observe(this@MainActivity, Observer {
            it?.also {
                mainViewModel.setWeather(currentWeather)
                applyIcons(mainViewModel.weatherIconId.value.toString())
                loader.visibility = View.GONE
                content.visibility = View.VISIBLE

            }
        })

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar menu items
        when (item.itemId) {
            R.id.location -> {
                if(hasLocationPermission()){
                    startLocationUpdates()
                    launch{
                        content.visibility = View.GONE
                        loader.visibility = View.VISIBLE
                        mainViewModel.setDeviceLocation()
                    }

                }
                else{

                    requestLocationPermission()
                }


                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)

        val searchItem = menu.findItem(R.id.search)
        val searchView = searchItem.actionView as SearchView
        searchView.setQueryHint("type localization")

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {

                launch {
                    content.visibility = View.GONE
                    loader.visibility = View.VISIBLE
                    mainViewModel.setCustomLocalization(query)}
                return false
            }

        })

        return true
    }

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setPositiveButton("ok"){dialog, which ->
        }
        errorDialog = builder.create()

        content.visibility = View.GONE
        mainViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)

        DataBindingUtil.setContentView<ActivityMainBinding>(
            this, R.layout.activity_main
        ).apply {
            this.setLifecycleOwner(this@MainActivity)
            this.viewModel = mainViewModel
        }
        job = Job()
        loadUI()
        requestLocationPermission()

//        //need to bind icons, no context
//        val weatherIconView = findViewById<WeatherIconView>(R.id.imageView_condition_icon)
//        weatherIconView.setIconResource(getString(R.string.wi_cloud))

    }

    private fun applyIcons(icon: String){
        val weatherIconView = findViewById<WeatherIconView>(R.id.imageView_condition_icon)
        Log.d("ICON", icon)
        when(icon){
            "01d"->weatherIconView.setIconResource(getString(R.string.wi_day_sunny))
            "01n"->weatherIconView.setIconResource(getString(R.string.wi_night_clear))
            "02d"->weatherIconView.setIconResource(getString(R.string.wi_day_sunny_overcast))
            "02n"->weatherIconView.setIconResource(getString(R.string.wi_night_alt_cloudy))
            "03d", "03n"->weatherIconView.setIconResource(getString(R.string.wi_cloud))
            "04d", "04n"->weatherIconView.setIconResource(getString(R.string.wi_cloudy))
            "09d", "09n"->weatherIconView.setIconResource(getString(R.string.wi_showers))
            "10d"->weatherIconView.setIconResource(getString(R.string.wi_day_rain))
            "10n"->weatherIconView.setIconResource(getString(R.string.wi_night_rain))
            "11d", "11n"->weatherIconView.setIconResource(getString(R.string.wi_storm_showers))
            "13d", "13n"->weatherIconView.setIconResource(getString(R.string.wi_snow))
            "50d", "50n"->weatherIconView.setIconResource(getString(R.string.wi_fog))

        }
    }



}


