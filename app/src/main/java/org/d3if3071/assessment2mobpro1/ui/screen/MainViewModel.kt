package org.d3if3071.assessment2mobpro1.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import org.d3if3071.assessment2mobpro1.database.SampahDao
import org.d3if3071.assessment2mobpro1.model.Sampah

class MainViewModel(dao: SampahDao) : ViewModel() {
    val  data: StateFlow<List<Sampah>> = dao.getSampah().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )
}