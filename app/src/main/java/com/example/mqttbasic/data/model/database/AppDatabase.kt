package com.example.mqttbasic.data.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.mqttbasic.data.model.database.daos.ConnectionDao
import com.example.mqttbasic.data.model.database.entities.Connection
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executors

@Database(entities = [Connection::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun connectionDao(): ConnectionDao

    companion object {
        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also {instance = it}
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, "app-database").build()
        }
    }
}