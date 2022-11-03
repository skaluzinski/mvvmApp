package com.example.mvvmapp.users.util

sealed class UiEvent {
    data class Navigate(val route: String) : UiEvent()
    data class ShowSnackbar(
        val message: String,
        val action: String? = null
    ) : UiEvent()
    data class isLoading(var state: Boolean) : UiEvent()

}
