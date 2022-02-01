package com.example.smartlocker.data.room

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE

@Dao
interface NodeDao {
    @Query("SELECT * FROM nodeDB")
    fun getAll(): List<NodeModel>

    @Query("SELECT * FROM nodeDB WHERE id == :id")
    fun get(id:Int) : NodeModel?

    @Insert(onConflict = REPLACE)
    fun insert(nodeModel: NodeModel)

    @Query("DELETE FROM nodeDB WHERE id == :id")
    fun delete(id:Int)

    @Query("DELETE FROM nodeDB")
    fun deleteAll()

}