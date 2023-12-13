package com.example.mqttbasic.data.model.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "connection")
data class Connection(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val name: String,
    val address:String,
    val port:Int,
    @ColumnInfo(name = "user_name")
    val userName:String?,
    @ColumnInfo(name = "user_password")
    val userPassword:String?,
    val establishConnection:Boolean,
    @ColumnInfo(name = "actual_topic", defaultValue = "#")
    val actualTopic:String? = "#",
    @ColumnInfo(name="image_source")
    val imageSource:String? = null
)
