package mx.edu.utez.ciudadsecreta.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import mx.edu.utez.ciudadsecreta.R
import mx.edu.utez.ciudadsecreta.ui.theme.CiudadSecretaTheme
import mx.edu.utez.ciudadsecreta.viewmodel.MapViewModel
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker

@Composable
fun MapaScreen(viewModel: MapViewModel) {

    val puntos by viewModel.puntos.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    AndroidView(
        factory = { ctx ->
            MapView(ctx).apply {
                setMultiTouchControls(true)
                controller.setZoom(15.0)
            }
        },
        update = { map ->
            val eventsOverlay = MapEventsOverlay(object : MapEventsReceiver {

                override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                    if (p == null) return false

                    val markerTapped = puntos.firstOrNull { punto ->
                        val dist = GeoPoint(p.latitude, p.longitude)
                            .distanceToAsDouble(GeoPoint(punto.lat, punto.lon))

                        dist < 20
                    }

                    if (markerTapped != null) {
                        viewModel.abrirRumor(markerTapped)
                        return true
                    }

                    return false
                }

                override fun longPressHelper(p: GeoPoint?): Boolean {
                    if (p != null) {
                        viewModel.prepararNuevoRumor(
                            lat = p.latitude,
                            lon = p.longitude
                        )
                    }
                    return true
                }
            })

            map.overlays.clear()
            map.overlays.add(eventsOverlay)

            val icon = map.context.getDrawable(R.drawable.rumor_icon)

            puntos.forEach { punto ->
                val marker = Marker(map).apply {
                    position = GeoPoint(punto.lat, punto.lon)
                    title = punto.mensaje
                    icon?.let { this.icon = it }
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                }
                map.overlays.add(marker)
            }

            map.invalidate()
        }
    )

    // Dialogo de agregar rumor (solo texto)
    if (uiState.showAddDialog) {
        DialogAgregarRumor(
            onDismiss = { viewModel.cerrarDialogos() },
            onSave = { texto -> viewModel.guardarRumor(texto) }
        )
    }

    // Diálogo combinado (ver / editar / eliminar)
    if (uiState.showRumorDialog && uiState.puntoSeleccionado != null) {
        DialogRumorScreen(
            punto = uiState.puntoSeleccionado!!,
            mensajeActual = uiState.mensaje,
            usuarioActual = "Secreto",

            onGuardar = { nuevo ->
                viewModel.editarRumor(nuevo)
            },

            onEliminar = {
                viewModel.borrarRumor()
            },

            onDismiss = {
                viewModel.cerrarDialogos()
            }
        )
    }
}


/*@Preview(showBackground = true)
fun PreviewMap(){
    CiudadSecretaTheme {
        MapaScreen()
    }
}*/

