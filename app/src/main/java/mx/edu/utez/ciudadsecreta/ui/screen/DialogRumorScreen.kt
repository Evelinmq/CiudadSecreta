package mx.edu.utez.ciudadsecreta.ui.screen

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*

import mx.edu.utez.ciudadsecreta.data.model.PuntoMarcado

@Composable
fun DialogRumorScreen(
    punto: PuntoMarcado,
    usuarioActual: String,
    onGuardar: (String) -> Unit,
    onEliminar: () -> Unit,
    onDismiss: () -> Unit,
    mensajeActual: String
) {
    val esAutor = punto.autor == usuarioActual

    var texto by remember { mutableStateOf(punto.mensaje) }
    var editando by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                if (editando) "Editar secreto"
                else "Publicado por ${punto.autor}"
            )
        },
        text = {
            if (editando) {
                OutlinedTextField(
                    value = texto,
                    onValueChange = { texto = it },
                    label = { Text("Nuevo texto") }
                )
            } else {
                Text(punto.mensaje)
            }
        },
        confirmButton = {
            if (esAutor) {
                if (editando) {
                    Button(onClick = { onGuardar(texto); onDismiss() }) {
                        Text("Guardar")
                    }
                } else {
                    Button(onClick = { editando = true }) {
                        Text("Editar")
                    }
                }
            }
        },
        dismissButton = {
            if (esAutor) {
                Button(onClick = {
                    if (editando) {
                        editando = false
                    } else {
                        onEliminar()
                    }
                }) {
                    Text(if (editando) "Cancelar" else "Eliminar")
                }
            } else {
                Button(onClick = onDismiss) {
                    Text("Cerrar")
                }
            }
        }
    )
}
