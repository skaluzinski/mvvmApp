package com.example.mvvmapp.users.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvmapp.users.model.User
import com.example.mvvmapp.users.model.UsersRepository
import com.example.mvvmapp.users.util.Routes
import com.example.mvvmapp.users.util.UiEvent
import com.example.mvvmapp.users.util.UsersListEvent
import com.example.mvvmapp.users.util.UsersListEvent.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(private val usersRepository: UsersRepository) :
    ViewModel() {

    var users: Flow<List<User>> = usersRepository.getAllUsers()
    private var user: User? = null

    private var _uiEvent: MutableStateFlow<UiEvent> = MutableStateFlow(UiEvent.isLoading(true))
    val uiEvent: StateFlow<UiEvent> = _uiEvent


    fun onEvent(event: UsersListEvent) {
        when (event) {
            is AddUser -> addUser(event.user)
            is UserClicked -> modifyUser(event.user.id)
            is DeleteUser -> deleteUser(event.user)
            is OnUserChangeDone -> showSnackbar("Save changed")
            is OnUndoDeleteUser -> addUser(user!!)
            is Error -> handleError(event.exception)
            is ShowUsers -> showSnackbar("Loaded users")
        }
    }


    private fun showSnackbar(message: String, action: String? = null) {
        if (action == null) {
            setUiEvent(UiEvent.ShowSnackbar(message))
        } else {
            setUiEvent(UiEvent.ShowSnackbar(message, action))
        }
    }

    suspend fun getUserById(userId: String): Flow<User> = usersRepository.getUserById(userId.toInt())


    private fun handleError(exception: Exception) {
        showSnackbar("Error occured : ${exception.message}")
    }

    private fun setUiEvent(event: UiEvent) {
        _uiEvent.value = event
    }

    private fun addUser(user: User) {
        viewModelScope.launch {
            usersRepository.createUser(user)
        }
        setUiEvent(UiEvent.isLoading(true))
    }

    private fun modifyUser(id: Int) {
        setUiEvent(UiEvent.Navigate(Routes.ADD_EDIT_USER + "?userId=$id"))
        setUiEvent(UiEvent.isLoading(true))
    }

    private fun deleteUser(user: User) {
        this.user = user
        viewModelScope.launch {
            usersRepository.deleteUser(user)
        }
        setUiEvent(UiEvent.ShowSnackbar("User deleted", "undo"))
    }

}