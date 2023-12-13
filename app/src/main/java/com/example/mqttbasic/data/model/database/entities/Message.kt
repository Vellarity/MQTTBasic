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
    val id:Long? = null,
    val payload:String = "",
    val topic:String,
    val timestamp:Long,
    @ColumnInfo(name = "connection_id")
    val connectionId:Int,
)
