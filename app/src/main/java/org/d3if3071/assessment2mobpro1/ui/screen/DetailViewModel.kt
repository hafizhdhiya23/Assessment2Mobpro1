package org.d3if3071.assessment2mobpro1.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3if3071.assessment2mobpro1.database.SampahDao
import org.d3if3071.assessment2mobpro1.model.Sampah


class DetailViewModel(private val dao: SampahDao): ViewModel() {

    fun insert(
        namaPengguna: String,
        kategoriSampah: String,
        berat: String,
        hargaPerKg: String,
        tanggalPenjemputan: String,
        alamat: String
    ) {
        val sampah = Sampah(
            namaPengguna = namaPengguna,
            kategoriSampah = kategoriSampah,
            berat = berat,
            hargaPerKg = hargaPerKg,
            tanggalPenjemputan = tanggalPenjemputan,
            alamat = alamat
        )

        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(sampah)
        }
    }

    suspend fun getSampah(id: Long): Sampah? {
        return dao.getSampahById(id)
    }

    fun update(
        id: Long,
        namaPengguna: String,
        kategoriSampah: String,
        berat: String,
        hargaPerKg: String,
        tanggalPenjemputan: String,
        alamat: String
    ) {
        val sampah = Sampah(
            id = id,
            namaPengguna = namaPengguna,
            kategoriSampah = kategoriSampah,
            berat = berat,
            hargaPerKg = hargaPerKg,
            tanggalPenjemputan = tanggalPenjemputan,
            alamat = alamat
        )

        viewModelScope.launch(Dispatchers.IO) {
            dao.update(sampah)
        }
    }

    fun delete(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteById(id)
        }
    }
}