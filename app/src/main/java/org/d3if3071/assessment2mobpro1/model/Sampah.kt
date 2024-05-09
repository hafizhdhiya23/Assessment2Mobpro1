package org.d3if3071.assessment2mobpro1.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sampah")
data class Sampah(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val namaPengguna: String,
    val kategoriSampah: String,
    val berat: String,
    val hargaPerKg: String,
    val tanggalPenjemputan: String,
    val alamat: String
)
