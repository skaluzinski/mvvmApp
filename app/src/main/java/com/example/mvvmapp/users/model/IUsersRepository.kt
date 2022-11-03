package com.example.mvvmapp.users.model

import kotlinx.coroutines.flow.Flow

interface IUsersRepository {
    suspend fun createUser(user: User)

    suspend fun deleteUser(user: User)

    suspend fun getUserById(id: Int): Flow<User>

    suspend fun isUserAuthorised(name: String, surname: String): Flow<Boolean>

    fun getAllUsers(): Flow<List<User>>
}