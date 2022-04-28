package ru.qveex.godudego.presentation.screens.main

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import ru.qveex.godudego.navigation.SetupNavGraph
import ru.qveex.godudego.presentation.components.AlertDialog
import ru.qveex.godudego.presentation.components.BottomNav

@SuppressLint("MissingPermission")
@ExperimentalPermissionsApi
@ExperimentalAnimationApi
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    PermissionGetter()

    Scaffold(
        bottomBar = { BottomNav(navController = navController) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            SetupNavGraph(navController = navController)
        }
    }
}


@ExperimentalPermissionsApi
@Composable
fun PermissionGetter() {

    val context = LocalContext.current
    val locationPermissionState = rememberMultiplePermissionsState(
        mutableListOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ).apply {
            if (SDK_INT > Build.VERSION_CODES.Q)
                this.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }
    )

    // TODO возможно придется переделать в MainActivity обычными реквестами
    // TODO запросы на права идут до тех пор, пока состояние прав как-либо ИЗМЕНЯЕТСЯ
    // TODO + тост будет появляется каждый раз при заходе, а не при выдаче

    if (locationPermissionState.allPermissionsGranted) {
        //val serviceIntent = Intent(context, LocationService::class.java)
        //context.startService(serviceIntent)
    } else {
        if (locationPermissionState.permissionRequested) {
            if (locationPermissionState.shouldShowRationale) {
                LaunchedEffect(true) {
                    locationPermissionState.launchMultiplePermissionRequest()
                }
            } else {
                AlertDialog(
                    title = "Permissions",
                    text = "Please give the app permission to access your location",
                    onConfirm = {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            val uri = Uri.fromParts("package", context.packageName, null)
                            data = uri
                        }
                        context.startActivity(intent)
                    },
                    onDismiss = { (context as? Activity)?.finish() }
                )
            }
        } else {
            LaunchedEffect(Unit) {
                locationPermissionState.launchMultiplePermissionRequest()
            }
        }
    }
}


@SuppressLint("MissingPermission")
@Composable
fun StartTrackLocation() {
    val context = LocalContext.current
    val end = LatLng(59.990810, 30.333247)
    val radius = 50 // meters
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            locationResult.locations.forEach {
                Log.i("CURRENT_LOCATION", "onLocationResult = $it")

                val dist = FloatArray(1)
                Location.distanceBetween(
                    it.latitude,
                    it.longitude,
                    end.latitude,
                    end.longitude,
                    dist
                )
                val inside = dist[0] < radius
                if (dist[0] > radius) Toast.makeText(context, "outside", Toast.LENGTH_LONG).show()
                else Toast.makeText(context, "inside", Toast.LENGTH_LONG).show()
                Log.i("CURRENT_LOCATION", "diff = ${dist[0]}")
            }
        }
    }
    val locationRequest = LocationRequest.create().apply {
        this.interval = 20_000
        this.fastestInterval = 10_000
        this.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
    }
    fusedLocationClient.requestLocationUpdates(
        locationRequest,
        locationCallback,
        Looper.getMainLooper()
    )
}