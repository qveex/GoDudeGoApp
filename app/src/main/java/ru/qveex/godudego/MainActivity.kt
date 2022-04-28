package ru.qveex.godudego

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import dagger.hilt.android.AndroidEntryPoint
import ru.qveex.godudego.presentation.screens.main.MainScreen
import ru.qveex.godudego.presentation.screens.main.MainViewModel
import ru.qveex.godudego.services.LocationService
import ru.qveex.godudego.ui.theme.GoDudeGoTheme
import ru.qveex.godudego.utils.Constants.LOCATION_SERVICE_ACTION

// https://youtu.be/0rc75uR0CNs
@ExperimentalPermissionsApi
@ExperimentalAnimationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()
    private val locationReceiver: LocationReceiver by lazy { LocationReceiver() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GoDudeGoTheme {
                MainScreen()
            }
        }

        registerReceiver(locationReceiver, IntentFilter(LOCATION_SERVICE_ACTION))
        val serviceIntent = Intent(this, LocationService::class.java)
        startService(serviceIntent)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(LocationReceiver())
    }

    inner class LocationReceiver: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == LOCATION_SERVICE_ACTION) {
                val latitude = intent.getDoubleExtra("current_latitude", 0.0)
                val longitude = intent.getDoubleExtra("current_longitude", 0.0)
                Log.i("CURRENT_LOCATION", "lt = $latitude lng = $longitude")

                mainViewModel.updateCurrentLocation(latitude, longitude)
            }
        }

    }
}