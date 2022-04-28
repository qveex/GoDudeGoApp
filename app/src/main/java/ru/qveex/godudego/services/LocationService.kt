package ru.qveex.godudego.services

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.location.Location
import android.os.IBinder
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import ru.qveex.godudego.utils.Constants.LOCATION_SERVICE_ACTION

// TODO сделаь сервис <<неубиваемым>>
// TODO пытаться собирать статистику при выключенном приложении
// TODO может работать как foreground service???
class LocationService : Service() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest

    private val points = listOf(
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
    private var isOMW = false
    private var start: LatLng? = null
    private var end: LatLng? = null


    private fun isInside(location: Location): Boolean {
        val radius = 50 // meters
        points.forEach {
            val dist = distance(LatLng(location.latitude, location.longitude), it)
            val inside = dist < radius
            if (inside) return true
        }
        return false
    }

    private fun nearestPoint(location: Location): LatLng {
        var point = LatLng(0.0, 0.0)
        var lastDist = Float.MAX_VALUE
        points.forEach {
            val dist = distance(LatLng(location.latitude, location.longitude), it)
            if (dist < lastDist) {
                point = it
            }
            lastDist = dist
        }
        return point
    }

    private fun distance(start: LatLng, end: LatLng): Float {
        val dist = FloatArray(1)
        Location.distanceBetween(
            start.latitude,
            start.longitude,
            end.latitude,
            end.longitude,
            dist
        )
        return dist[0]
    }

    override fun onBind(intent: Intent?): IBinder? = null

    @SuppressLint("MissingPermission")
    override fun onCreate() {
        super.onCreate()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                locationResult.locations.forEach { curLocation ->
                    Log.i("CURRENT_LOCATION", "onLocationResult = $curLocation")
                    val tick = Intent().apply {
                        action = (LOCATION_SERVICE_ACTION)
                        putExtra("current_latitude", curLocation.latitude)
                        putExtra("current_longitude", curLocation.longitude)
                    }

                    val inside = isInside(curLocation)
                    val near = nearestPoint(curLocation)


                    // в пути
                    if (isOMW) {

                        if (inside && distance(LatLng(curLocation.latitude, curLocation.longitude), start!!) > 50
                        ) {
                            // пришли в конечный пункт
                            end = near
                            isOMW = false
                            // stop timer
                            // calc stats
                            // start = null
                            // end = null
                        } else if (inside && near == start) {
                            // вернулись обратно в исходный пункт
                            // clear timer
                            isOMW = false
                        }

                    } else {

                        // мы находимся в какой-то точке?
                        if (inside) {
                            // только зашли
                            if (start == null) {
                                start = near
                            } else { // мы находимся там уже какое-то время

                            }
                        } else { // вышли из точки, но еще не в пути
                            isOMW = true
                            // start timer
                        }

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