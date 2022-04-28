package ru.qveex.godudego.presentation.screens.main

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng

class MainViewModel: ViewModel() {

    private val _currentLocation = mutableStateOf(LatLng(0.0, 0.0))
    val currentLocation get() = _currentLocation

    fun updateCurrentLocation(latitude: Double, longitude: Double): Boolean {
        if (latitude != 0.0 && longitude != 0.0) return false
        val newLocation = LatLng(latitude, longitude)
        _currentLocation.value = newLocation
        return true
    }

}