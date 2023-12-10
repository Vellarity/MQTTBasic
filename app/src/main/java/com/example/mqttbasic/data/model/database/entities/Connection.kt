package com.example.mqttbasic.data.model.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "connection")
data class Connection(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val address:String,
    val port:Int,
    @ColumnInfo(name = "user_name")
    val userName:String?,
    @ColumnInfo(name = "user_password")
    val userPassword:String?,
    val establishConnection:Boolean,
    @ColumnInfo(name = "actual_topic", defaultValue = "#")
    val actualTopic:String?,
)

/*
data class Connection(
    val name:String,
    val address:String,
    val port:Int,
    val userName:String?,
    val password: String?,
    val establishConnection:Boolean = false
)
 */