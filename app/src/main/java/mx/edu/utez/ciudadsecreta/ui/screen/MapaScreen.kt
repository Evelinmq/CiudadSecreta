package mx.edu.utez.ciudadsecreta.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.viewinterop.AndroidView
import mx.edu.utez.ciudadsecreta.R
import mx.edu.utez.ciudadsecreta.viewmodel.MapViewModel
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker

@Composable
fun MapaScreen(viewModel: MapViewModel) {

    val puntos by viewModel.puntos.collectAsState()
    val iuState by viewModel.uiState.collectAsState()

    AndroidView(
        factory = { ctx ->
            MapView(ctx).apply {
                setMultiTouchControls(true)
                controller.setZoom(15.0)
            }
        },
        update = { map ->
            val eventsOverlay = MapEventsOverlay(object : MapEventsReceiver {
                //Abre el rumor si toca algún marcador(icono) en el mapa
                override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                    if (p == null) return false

                    val markerTapped = puntos.firstOrNull { punto ->
                        val dist = GeoPoint(p.lat, p.lon).distanceToAsDouble(
                            GeoPoint(punto.lat, punto.lon)
                        )
                        dist < 20
                    }

                    if (markerTapped != null) {
                        viewModel.abrirRumor(markerTapped)
                        return true
                    }

                    return false
                }

                //Prepara un nuevo rumor
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

            //Icono para los rumores
            val icon =  map.context.getDrawable(R.drawable.rumor_icon)

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

    //Dialogo de agregar rumor
    if (iuState.showAddDialog) {
        DialogAgregarRumor(
          onDismiss = { viewModel.cerrarDialogos()},
            onSave = {texto -> viewModel.guardarRumor(texto)}
        )
    }

    // Ver rumor
    if (iuState.showViewDialog && iuState.puntoSeleccionado != null) {
        DialogVerRumor(
            punto = iuState.puntoSeleccionado!!,
            usuarioActual = "Secreto",
            onEditar = { viewModel.prepararEdicion() },
            onEliminar = { viewModel.borrarRumor() },
            onDismiss = { viewModel.cerrarDialogos() }
        )
    }

    // Editar rumor
    if (iuState.showEditDialog) {
        DialogEditarRumor(
            mensajeInicial = iuState.mensaje,
            onGuardar = { nuevo -> viewModel.editarRumor(nuevo) },
            onDismiss = { viewModel.cerrarDialogos() }
        )
    }

}

