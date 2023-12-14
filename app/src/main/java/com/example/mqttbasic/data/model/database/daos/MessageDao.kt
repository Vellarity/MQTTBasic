package com.example.mqttbasic.data.model.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.mqttbasic.data.model.database.entities.Message

@Dao
interface MessageDao {

    @Query("select * from message where connection_id = :brokerId order by id DESC")
    suspend fun getMessagesByBrokerId(brokerId:Int): MutableList<Message>

    @Insert
    suspend fun insertMessage(message:Message):Long

}