package org.d3if3071.assessment2mobpro1.ui.screen

import android.content.res.Configuration
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.d3if3071.assessment2mobpro1.R
import org.d3if3071.assessment2mobpro1.database.SampahDb
import org.d3if3071.assessment2mobpro1.ui.theme.Assessment2Mobpro1Theme
import org.d3if3071.assessment2mobpro1.util.ViewModelFactory
import java.time.LocalDate

const val KEY_ID_SAMPAH = "idCatatan"

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavHostController, id: Long? = null) {
    val context = LocalContext.current
    val db = SampahDb.getInstance(context)
    val factory = ViewModelFactory(db.dao)
    val viewModel: DetailViewModel = viewModel(factory = factory)

    var namaPengguna by remember { mutableStateOf("") }
    var selectedKategoriSampah by remember { mutableStateOf("") }
    var berat by remember { mutableStateOf("") }
    val hargaPerKg = "5000" // Harga per kg yang sudah ditentukan
    var tanggalPenjemputan by remember { mutableStateOf("") }
    var alamat by remember { mutableStateOf("") }

    var showDialog by remember { mutableStateOf(false) }

    // Set tanggal penjemputan ke hari ini saat pertama kali komposisi diluncurkan
    LaunchedEffect(true) {
        // Jika id tidak null, artinya sedang dalam mode edit, jadi tidak perlu mengubah tanggal penjemputan
        if (id != null) return@LaunchedEffect
        // Dapatkan tanggal hari ini
        tanggalPenjemputan = LocalDate.now().toString()
    }

    LaunchedEffect(true) {
        if (id == null) return@LaunchedEffect
        val data = viewModel.getSampah(id) ?: return@LaunchedEffect
        namaPengguna = data.namaPengguna
        selectedKategoriSampah = data.kategoriSampah
        berat = data.berat
        tanggalPenjemputan = data.tanggalPenjemputan
        alamat = data.alamat
    }
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.kembali),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                title = {
                    if (id == null)
                        Text(text = stringResource(id = R.string.tambah_catatan))
                    else
                        Text(text = stringResource(id = R.string.edit_catatan))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                actions = {
                    IconButton(onClick = {
                        if (namaPengguna.isEmpty() || selectedKategoriSampah.isEmpty() || berat.isEmpty() ||
                            tanggalPenjemputan.isEmpty() || alamat.isEmpty()) {
                            Toast.makeText(context, R.string.invalid, Toast.LENGTH_LONG).show()
                            return@IconButton
                        }
                        if (id == null) {
                            viewModel.insert(namaPengguna, selectedKategoriSampah, berat, hargaPerKg, tanggalPenjemputan, alamat)
                        } else {
                            viewModel.update(id, namaPengguna, selectedKategoriSampah, berat, hargaPerKg, tanggalPenjemputan, alamat)
                        }
                        navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Outlined.Check,
                            contentDescription = stringResource(id = R.string.simpan),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    if (id != null) {
                        DeleteAction { showDialog = true }
                        DisplayAlertDialog(
                            openDialog = showDialog,
                            onDismissRequest = { showDialog = false }) {
                            showDialog = false
                            viewModel.delete(id)
                            navController.popBackStack()
                        }
                    }
                }
            )
        }
    ) { padding ->
        FormSampah(
            namaPengguna = namaPengguna,
            onNamaPenggunaChange = { namaPengguna = it },
            selectedKategoriSampah = selectedKategoriSampah,
            onKategoriSampahChange = { selectedKategoriSampah = it },
            berat = berat,
            onBeratChange = { berat = it },
            tanggalPenjemputan = tanggalPenjemputan,
            onTanggalPenjemputanChange = { tanggalPenjemputan = it },
            alamat = alamat,
            onAlamatChange = { alamat = it },
            modifier = Modifier.padding(padding)
        )
    }
}

@Composable
fun DeleteAction(delete: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    IconButton(onClick = {expanded = true}) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = stringResource(R. string.lainnya),
            tint = MaterialTheme.colorScheme.primary
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = {
                    Text(text = stringResource(id = R.string.hapus))
                },
                onClick = {
                    expanded = false
                    delete()
                }
            )
        }
    }
}

@Composable
fun FormSampah(
    namaPengguna: String, onNamaPenggunaChange: (String) -> Unit,
    selectedKategoriSampah: String, onKategoriSampahChange: (String) -> Unit,
    berat: String, onBeratChange: (String) -> Unit,
    tanggalPenjemputan: String, onTanggalPenjemputanChange: (String) -> Unit,
    alamat: String, onAlamatChange: (String) -> Unit,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = namaPengguna,
            onValueChange = { onNamaPenggunaChange(it) },
            label = { Text(text = stringResource(R.string.nama_pengguna)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Column {
            Text(
                text = stringResource(R.string.kategori_sampah),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                RadioButton(
                    selected = selectedKategoriSampah == "Organik",
                    onClick = { onKategoriSampahChange("Organik") },
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = stringResource(R.string.organik),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f).align(Alignment.CenterVertically)
                )
                RadioButton(
                    selected = selectedKategoriSampah == "Anorganik",
                    onClick = { onKategoriSampahChange("Anorganik") },
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = stringResource(R.string.anorganik),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f).align(Alignment.CenterVertically)
                )
            }

        }
        OutlinedTextField(
            value = berat,
            onValueChange = { onBeratChange(it) },
            label = { Text(text = stringResource(R.string.berat_kg)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = tanggalPenjemputan,
            onValueChange = { onTanggalPenjemputanChange(it) },
            label = { Text(text = stringResource(R.string.tanggal_penjemputan)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = alamat,
            onValueChange = { onAlamatChange(it) },
            label = { Text(text = stringResource(R.string.alamat)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun DetailScreenPreview() {
    Assessment2Mobpro1Theme{
        DetailScreen(rememberNavController())
    }
}
