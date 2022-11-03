package com.example.mvvmapp.users.di

import android.content.Context
import androidx.room.Room
import com.example.mvvmapp.users.model.IUsersRepository
import com.example.mvvmapp.users.model.UsersDao
import com.example.mvvmapp.users.model.UsersDatabase
import com.example.mvvmapp.users.model.UsersRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UsersModule {

    @Provides
    @Singleton
    fun provideUsersDatabase(@ApplicationContext applicationContext: Context): UsersDatabase {
        return Room.databaseBuilder(
            applicationContext,
            UsersDatabase::class.java,
            "usersDatabase"
        ).build()
    }

    @Provides
    @Singleton
    fun provideUsersDao(appdatabase: UsersDatabase): UsersDao {
        return appdatabase.usersDao()
    }

}

@Module
@InstallIn(SingletonComponent::class)
abstract class AbstractUsersModule {

    @Binds
    @Singleton
    abstract fun bindRepository(usersRepository: UsersRepository): IUsersRepository
}
