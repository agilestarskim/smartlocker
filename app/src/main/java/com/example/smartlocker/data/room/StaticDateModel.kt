package com.example.smartlocker.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "StaticDate")
data class StaticDateModel(
    @PrimaryKey val date:String,
    @ColumnInfo(name = "state") val state: Int
)
