package com.example.mqttbasic.data.model.database.tuples

import androidx.room.Embedded
import androidx.room.Relation
import com.example.mqttbasic.data.model.database.entities.Connection
import com.example.mqttbasic.data.model.database.entities.ConnectionTopic

data class ConnectionWithTopics(
    @Embedded val connection: Connection,

    @Relation(
        parentColumn = "id",
        entityColumn = "connection_id"
    )
    val topics: MutableList<ConnectionTopic>
)