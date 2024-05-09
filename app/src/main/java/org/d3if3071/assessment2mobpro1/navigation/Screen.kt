package org.d3if3071.assessment2mobpro1.navigation

import org.d3if3071.assessment2mobpro1.ui.screen.KEY_ID_SAMPAH

sealed class Screen (val route: String) {
    data object Home: Screen ("mainScreen")
    data object FormBaru: Screen("detailScreen")
    data object FormUbah: Screen("detailScreen/{$KEY_ID_SAMPAH}") {
        fun withId(id: Long) = "detailScreen/$id"
    }
}