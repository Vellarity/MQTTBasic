package com.example.mqttbasic.data.model.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mqttbasic.data.model.database.entities.Connection

@Dao
interface ConnectionDao {
    @Query("SELECT * from connection")
    suspend fun getConnections(): List<Connection>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConnections(vararg connections:Connection)

    @Query("SELECT * FROM connection WHERE id = :id")
    suspend fun getConnectionById(id:Int): Connection

    @Query("DELETE FROM connection WHERE id = :id")
    suspend fun deleteConnection(id:Int)
}