package ru.qveex.godudego

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.*
import ru.qveex.godudego.ui.theme.GoDudeGoTheme

// https://youtu.be/0rc75uR0CNs
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GoDudeGoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val singapore = LatLng(1.35, 103.87)
                    val cameraPositionState = rememberCameraPositionState {
                        position = CameraPosition.fromLatLngZoom(singapore, 10f)
                    }


                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        properties = MapProperties(mapStyleOptions = MapStyleOptions.loadRawResourceStyle(LocalContext.current, R.raw.map_dark_theme)),
                        cameraPositionState = cameraPositionState,
                        uiSettings = MapUiSettings(),

                    ) {
                        Marker(
                            position = singapore,
                            title = "Singapore",
                            snippet = "Marker in Singapore"
                        )

                    }
                }
            }
        }
    }
}