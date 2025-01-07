package com.example.mqttbasic.data.model.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mqttbasic.data.model.database.entities.ConnectionTopic
import com.example.mqttbasic.data.model.database.entities.NewConnectionTopic

@Dao
interface ConnectionTopicDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = ConnectionTopic::class)
    suspend fun createTopic(newTopic: NewConnectionTopic):Long

    @Query("Select * from connection_topic where connection_id = :brokerId")
    suspend fun getTopicsByBrokerId(brokerId: Int): MutableList<ConnectionTopic>

    @Query("Delete from connection_topic where id = :id")
    suspend fun deleteTopicById(id: Int)

    @Delete
    suspend fun deleteTopic(connectionTopic: ConnectionTopic)
}