package com.example.mqttbasic.data.model.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "message",
    foreignKeys = [ForeignKey(
        entity = Connection::class,
        parentColumns = ["id"],
        childColumns = ["connection_id"]
    )]
)
data class Message(
    @PrimaryKey(autoGenerate = true)
    val id:Long = 0,
    val payload:String = "",
    val topic:String,
    val timestamp:String,
    val qos:Int,
    @ColumnInfo(name = "connection_id")
    val connectionId:Int,
)

data class NewMessage(
    val payload:String = "",
    val topic:String,
    val timestamp:String,
    val qos:Int,
    @ColumnInfo(name = "connection_id")
    val connectionId:Int,
)
