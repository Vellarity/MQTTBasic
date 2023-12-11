package com.example.mqttbasic.di

import android.app.Application
import androidx.room.Room
import com.example.mqttbasic.data.model.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MainModule {

    @Provides
    @Singleton
    fun provideAppDatabase(app: Application):AppDatabase {
        return Room.databaseBuilder(app.applicationContext, AppDatabase::class.java, "app-database").build()
    }
}