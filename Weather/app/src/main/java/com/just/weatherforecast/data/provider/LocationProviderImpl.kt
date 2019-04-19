package com.just.weatherforecast.data.provider

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.just.weatherforecast.internal.LocationPermissionNotGrantedException
import com.just.weatherforecast.internal.asDeferred
import kotlinx.coroutines.Deferred


class LocationProviderImpl(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    context: Context
) : PreferenceProvider(context), LocationProvider {


    private val appContext = context.applicationContext


    override suspend fun getPreferredLocationString(): String? {
        try {
                val deviceLocation = getLastDeviceLocation().await()
                    ?:
                    return null
                return "${deviceLocation.latitude},${deviceLocation.longitude}"
            } catch (e: LocationPermissionNotGrantedException) {
                return "52.23,21.01" // default value before permission granted/no granted

            }
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