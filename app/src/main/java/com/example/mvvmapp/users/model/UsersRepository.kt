package com.example.mvvmapp.users.model

import com.example.mvvmapp.users.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UsersRepository @Inject constructor(
    private val dao: UsersDao,
    @IoDispatcher private val defaultDispatcher: CoroutineDispatcher
) : IUsersRepository {
    override suspend fun createUser(
        user: User
    ) = withContext(defaultDispatcher) {
        dao.createUser(user)
    }


    override suspend fun deleteUser(user: User) = withContext(defaultDispatcher) {
        dao.deleteUser(user)
    }

    override suspend fun getUserById(id: Int): Flow<User> =
        dao.getUserById(id)

    override suspend fun isUserAuthorised(name: String, surname: String) =
        withContext(defaultDispatcher) {
            dao.isUserAuthorised(name, surname)
        }

    override fun getAllUsers(): Flow<List<User>> =
        dao.getAllUsers()

}