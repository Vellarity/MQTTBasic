package com.example.mqttbasic.data.model.database

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.mqttbasic.data.model.database.daos.ConnectionDao
import com.example.mqttbasic.data.model.database.daos.ConnectionTopicDao
import com.example.mqttbasic.data.model.database.daos.MessageDao
import com.example.mqttbasic.data.model.database.entities.Connection
import com.example.mqttbasic.data.model.database.entities.ConnectionTopic
import com.example.mqttbasic.data.model.database.entities.Message
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executors
/*
@Database(entities = [Connection::class, Message::class], version = 1, exportSchema = true)
abstract class AppDatabase: RoomDatabase() {
    abstract fun connectionDao(): ConnectionDao
    abstract fun messageDao(): MessageDao

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
}*/
@Database(
    entities = [Connection::class, Message::class, ConnectionTopic::class],
    version = 4,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3),
        AutoMigration(from = 3, to = 4),
    ],
    exportSchema = true,
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun connectionDao(): ConnectionDao
    abstract fun messageDao(): MessageDao
    abstract fun connectionTopicDao(): ConnectionTopicDao

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