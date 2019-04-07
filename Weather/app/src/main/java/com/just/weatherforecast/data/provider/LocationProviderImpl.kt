package com.just.weatherforecast.data.provider

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.just.weatherforecast.data.db.entity.CurrentWeatherResponse
import com.just.weatherforecast.internal.LocationPermissionNotGrantedException
import com.just.weatherforecast.internal.asDeferred
import kotlinx.coroutines.Deferred

//const val USE_DEVICE_LOCATION = "USE_DEVICE_LOCATION"
//const val CUSTOM_LOCATION = "CUSTOM_lOCATION"  //

class LocationProviderImpl(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    context: Context
) : PreferenceProvider(context), LocationProvider {


    private val appContext = context.applicationContext
//not using if has changed!!
    override suspend fun hasLocationChanged(currentWeatherResponse: CurrentWeatherResponse): Boolean {
        val deviceLocationChanged = try {
            hasDeviceLocationChanged(currentWeatherResponse)
        } catch (e: LocationPermissionNotGrantedException) {
            false
        }
        Log.d("FETCH", deviceLocationChanged.toString())

        return deviceLocationChanged
    }

    override suspend fun getPreferredLocationString(): String? {

            try {
                val deviceLocation = getLastDeviceLocation().await()
                    ?:
                    return null
                return "${deviceLocation.latitude},${deviceLocation.longitude}"
            } catch (e: LocationPermissionNotGrantedException) {
                Log.d("FETCH", "no permission")
                return "52.23,21.01" // default value before permission granted/no granted
                //return null

            }

    }

    private suspend fun hasDeviceLocationChanged(currentWeatherResponse: CurrentWeatherResponse): Boolean {

        val deviceLocation = getLastDeviceLocation().await()
            ?: return false

        // Comparing doubles cannot be done with "=="
        val comparisonThreshold = 0.03
        return Math.abs(deviceLocation.latitude - currentWeatherResponse.coordinates.lat) > comparisonThreshold &&
                Math.abs(deviceLocation.longitude - currentWeatherResponse.coordinates.lon) > comparisonThreshold
    }



    @SuppressLint("MissingPermission")
    private fun getLastDeviceLocation(): Deferred<Location?> {
        return if (hasLocationPermission())
            fusedLocationProviderClient.lastLocation.asDeferred()
        else
            throw LocationPermissionNotGrantedException()
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(appContext,
            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }
}