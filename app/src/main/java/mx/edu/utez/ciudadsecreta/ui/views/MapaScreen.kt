package mx.edu.utez.ciudadsecreta.ui.views

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.viewinterop.AndroidView
import mx.edu.utez.ciudadsecreta.viewmodel.MapViewModel
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun MapaScreen(viewModel: MapViewModel) {

    val puntos by viewModel.puntos.collectAsState()

    AndroidView(
        factory = { ctx ->
            MapView(ctx).apply {
                setMultiTouchControls(true)
                controller.setZoom(15.0)
            }
        },
        update = { map ->

            map.overlays.clear()

            puntos.forEach { punto ->
                val marker = Marker(map)
                marker.position = GeoPoint(punto.lat, punto.lon)
                marker.title = punto.mensaje
                map.overlays.add(marker)
            }
        }
    )
}
