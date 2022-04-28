package ru.qveex.godudego.services

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import ru.qveex.godudego.utils.Constants.LOCATION_SERVICE_ACTION

// TODO сделаь сервис <<неубиваемым>>
// TODO пытаться собирать статистику при выключенном приложении
// TODO может работать как foreground service???
class LocationService: Service() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest

    val points = listOf(
        // Лесная
        LatLng(59.990810, 30.333247),
        LatLng(59.984872, 30.344079),

        // Спасская
        LatLng(59.926784, 30.319728),
        LatLng(59.929615, 30.296208),

        // Столовая
        LatLng(59.990810, 30.333247),
        LatLng(59.988531, 30.341049),
    )

    override fun onBind(intent: Intent?): IBinder? = null

    @SuppressLint("MissingPermission")
    override fun onCreate() {
        super.onCreate()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                locationResult.locations.forEach {
                    Log.i("CURRENT_LOCATION", "onLocationResult = $it")
                    val tick = Intent().apply {
                        action = (LOCATION_SERVICE_ACTION)
                        putExtra("current_latitude", it.latitude)
                        putExtra("current_longitude", it.longitude)
                    }

                    // Обновление локации не отправляется с выключенным экраном
                    sendBroadcast(tick)
                }
            }
        }
        locationRequest = LocationRequest.create().apply {
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

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return START_STICKY
    }

    /*override fun onDestroy() {
        val intent = Intent().apply {
            action = "restartservice"
            setClass(this@LocationService, Restarter::class.java)
        }
        Log.i("CURRENT_LOCATION", "destroy main")
        sendBroadcast(intent)
        super.onDestroy()
    }*/
}