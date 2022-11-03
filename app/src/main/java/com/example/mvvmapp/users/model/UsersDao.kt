package com.example.mvvmapp.users.model

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UsersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("SELECT * FROM users WHERE id = :id")
    fun getUserById(id: Int): Flow<User>

    @Query("SELECT EXISTS(SELECT * FROM users WHERE name LIKE :name AND surname LIKE :surname)")
    fun isUserAuthorised(name: String, surname: String): Flow<Boolean>

    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<User>>
}