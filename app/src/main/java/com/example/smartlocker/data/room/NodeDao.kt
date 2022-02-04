package com.example.smartlocker.data.room

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE

@Dao
interface NodeDao {
    @Query("SELECT * FROM node")
    fun getAll(): List<NodeModel>

    @Query("SELECT * FROM node WHERE id == :id")
    fun get(id:Int) : NodeModel?

    @Insert(onConflict = REPLACE)
    fun insert(nodeModel: NodeModel)

    @Query("DELETE FROM node WHERE id == :id")
    fun delete(id:Int)

    @Query("DELETE FROM node")
    fun deleteAll()

}