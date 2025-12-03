package mx.edu.utez.ciudadsecreta.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import mx.edu.utez.ciudadsecreta.data.model.PuntoMarcado

@Dao
interface PuntoDao {

    @Insert
    suspend fun insertar(punto: PuntoMarcado)

    @Query("SELECT * FROM PuntoMarcado")
    fun obtenerPuntos(): Flow<List<PuntoMarcado>>
    @Update
    suspend fun actualizar(punto: PuntoMarcado)

    @Delete
    suspend fun eliminar(punto: PuntoMarcado)

}
