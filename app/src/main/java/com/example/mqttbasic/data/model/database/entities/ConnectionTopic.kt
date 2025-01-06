package com.example.mqttbasic.data.model.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "connection_topic",
    foreignKeys = [androidx.room.ForeignKey(
        entity = Connection::class,
        parentColumns = ["id"],
        childColumns = ["connection_id"]
    )]
)
data class ConnectionTopic(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    @ColumnInfo(name = "connection_id")
    val connectionId: Int
)

data class NewConnectionTopic(
    val name: String,
    @ColumnInfo(name = "connection_id")
    val connectionId: Int
)