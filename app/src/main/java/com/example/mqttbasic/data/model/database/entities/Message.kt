package com.example.mqttbasic.data.model.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "message")
data class Message(
    @PrimaryKey(autoGenerate = true)
    val id:Long? = null
)
