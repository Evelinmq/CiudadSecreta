package mx.edu.utez.ciudadsecreta.ui.screen
//ESTE YA NO SE USA PORQUE LO UNIFIQUÉ CON VERRUMOR EN LA SCREEN DialogRumorSreen
//Lo dejo aquí comentado por cualquier cosa
/*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun DialogEditarRumor(
    mensajeInicial: String,
    onGuardar: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var texto by remember { mutableStateOf(mensajeInicial) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar rumor") },
        text = {
            OutlinedTextField(
                value = texto,
                onValueChange = { texto = it },
                label = { Text("Nuevo texto") }
            )
        },
        confirmButton = {
            Button(onClick = { onGuardar(texto) }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

 */
