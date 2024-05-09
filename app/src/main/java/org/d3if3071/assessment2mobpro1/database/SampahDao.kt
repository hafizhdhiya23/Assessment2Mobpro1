package org.d3if3071.assessment2mobpro1.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import org.d3if3071.assessment2mobpro1.model.Sampah

@Dao
interface SampahDao {

    @Insert
    suspend fun insert(sampah: Sampah)

    @Update
    suspend fun update(sampah: Sampah)

    @Query("SELECT * FROM sampah ORDER BY namaPengguna ASC")
    fun getSampah(): Flow<List<Sampah>>

    @Query("SELECT * FROM sampah WHERE id = :id")
    suspend fun getSampahById(id: Long): Sampah?

    @Query("DELETE FROM sampah WHERE id = :id")
    suspend fun deleteById(id: Long)

}