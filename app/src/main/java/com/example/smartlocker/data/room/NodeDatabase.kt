package com.example.smartlocker.data.room



import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [NodeModel::class], version = 3)
abstract class NodeDatabase : RoomDatabase() {
    abstract fun getNodeDao(): NodeDao
}