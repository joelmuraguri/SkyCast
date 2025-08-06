package com.muraguri.design

sealed class SkyCastEvents {
//    data class Navigate(val route : Destinations) : SkyCastEvents()
    data object PopBackStack : SkyCastEvents()
    data class ShowSnackbar(
        val message: String,
        val actionLabel: String? = null,
        val onActionClick: (() -> Unit)? = null ) : SkyCastEvents()

}