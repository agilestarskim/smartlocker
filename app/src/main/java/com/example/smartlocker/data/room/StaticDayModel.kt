package com.example.smartlocker.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "StaticDayDB")
data class StaticDayModel(
    @PrimaryKey val day:Date,
    @ColumnInfo(name = "state") val state: Int
)
