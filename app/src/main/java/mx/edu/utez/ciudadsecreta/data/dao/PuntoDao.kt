package mx.edu.utez.ciudadsecreta.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import mx.edu.utez.ciudadsecreta.data.model.PuntoMarcado

@Dao
interface PuntoDao {

    @Insert
    suspend fun insertar(punto: PuntoMarcado)

    @Query("SELECT * FROM PuntoMarcado")
    fun obtenerPuntos(): Flow<List<PuntoMarcado>>
}
