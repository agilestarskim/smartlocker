package com.example.smartlocker.data.room



import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [NodeModel::class, StaticTimeModel::class, StaticDayModel::class], version = 4)
@TypeConverters(Converters::class)
abstract class Database : RoomDatabase() {
    abstract fun getNodeDao(): NodeDao
    abstract fun getStaticTimeDao() : StaticTimeDao
    abstract fun getStaticDayDao() : StaticDayDao

}