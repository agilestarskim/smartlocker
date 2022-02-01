package com.example.smartlocker.data.room



import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [NodeModel::class, StaticTimeModel::class, StaticDayModel::class], version = 6)
abstract class SmartLockerDatabase : RoomDatabase() {
    abstract fun getNodeDao(): NodeDao
    abstract fun getStaticTimeDao() : StaticTimeDao
    abstract fun getStaticDayDao() : StaticDayDao

    companion object {
        private var instance: SmartLockerDatabase? = null

        @Synchronized
        fun getInstance(context: Context): SmartLockerDatabase? {
            if (instance == null) {
                instance = Room.databaseBuilder(context.applicationContext, SmartLockerDatabase::class.java, "SmartLockerDB")
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instance
        }
    }
}