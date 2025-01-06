package com.example.mqttbasic.data.model.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.mqttbasic.data.model.database.entities.Connection
import com.example.mqttbasic.data.model.database.tuples.ConnectionWithTopics
import kotlinx.coroutines.flow.Flow

@Dao
interface ConnectionDao {
    @Query("SELECT * from connection")
    fun getConnections(): Flow<List<Connection>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConnections(vararg connections:Connection):LongArray

    @Query("SELECT * FROM connection WHERE id = :id")
    suspend fun getConnectionById(id:Int): Connection

    @Query("DELETE FROM connection WHERE id = :id")
    suspend fun deleteConnection(id:Int)

    @Transaction
    @Query("SELECT * FROM connection where id = :id")
    suspend fun getConnectionByIdWithTopics(id:Int): ConnectionWithTopics

    @Transaction
    @Query("SELECT * FROM connection where id = :id")
    fun getConnectionByIdWithTopicsFlow(id:Int): Flow<ConnectionWithTopics>
}