package com.example.mvvmapp.users.util

import com.example.mvvmapp.users.model.User


sealed class UsersListEvent {
    data class ShowUsers(val users: List<User>) : UsersListEvent()
    data class Error(val exception: Exception) : UsersListEvent()
    data class AddUser(val user: User) : UsersListEvent()
    data class UserClicked(val user: User) : UsersListEvent()
    data class DeleteUser(val user: User) : UsersListEvent()
    object OnUndoDeleteUser : UsersListEvent()
    data class OnUserChangeDone(val user: User, val isDone: Boolean) : UsersListEvent()
}
