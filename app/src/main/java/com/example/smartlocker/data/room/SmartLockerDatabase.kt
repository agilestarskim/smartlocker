package com.example.smartlocker.data.room



import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [NodeModel::class, StaticTimeModel::class, StaticDateModel::class], version = 8)
abstract class SmartLockerDatabase : RoomDatabase() {
    abstract fun getNodeDao(): NodeDao
    abstract fun getStaticTimeDao() : StaticTimeDao
    abstract fun getStaticDateDao() : StaticDateDao

    //데이터베이스를 싱클턴으로 구현
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