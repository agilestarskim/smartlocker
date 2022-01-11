package com.example.smartlocker.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.smartlocker.data.state.NodeState

@Entity(tableName = "nodeDB")
data class NodeModel(
    @PrimaryKey val id:Int,
    @ColumnInfo(name = "password") val password:String,
    @ColumnInfo(name = "enabled") val enabled:Boolean
)