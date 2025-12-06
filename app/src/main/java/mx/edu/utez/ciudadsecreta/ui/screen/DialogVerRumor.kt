package mx.edu.utez.ciudadsecreta.ui.screen
//ESTE YA NO SE USA PORQUE LO UNIFIQUÉ CON EDITARRUMOR EN LA SCREEN DialogRumorSreen
//Lo dejo aquí comentado por cualquier cosa

/*

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import mx.edu.utez.ciudadsecreta.data.model.PuntoMarcado

@Composable
fun DialogVerRumor(
    punto: PuntoMarcado,
    usuarioActual: String,
    onEditar: () -> Unit,
    onEliminar: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Rumor publicado por ${punto.autor}") },
        text = { Text(punto.mensaje) },
        confirmButton = {
            if (punto.autor == usuarioActual) {
                Button(onClick = onEditar) {
                    Text("Editar")
                }
            }
        },
        dismissButton = {
            if (punto.autor == usuarioActual) {
                Button(onClick = onEliminar) {
                    Text("Eliminar")
                }
            } else {
                Button(onClick = onDismiss) {
                    Text("Cerrar")
                }
            }
        }
    )
}


 */
