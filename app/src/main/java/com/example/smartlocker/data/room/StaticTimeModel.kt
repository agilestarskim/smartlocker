package com.example.smartlocker.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "staticTime")
data class StaticTimeModel(
    @PrimaryKey val time : Int,
    @ColumnInfo(name = "max_amount") val maxAmount : Int,
    @ColumnInfo(name = "state") val state : Int
    )
