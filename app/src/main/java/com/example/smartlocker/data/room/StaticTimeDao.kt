package com.example.smartlocker.data.room

import androidx.room.*

@Dao
interface StaticTimeDao {

    @Query("SELECT * FROM staticTimeDB")
    fun getAll(): List<StaticTimeModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(staticTimeModel: StaticTimeModel )

    @Update
    fun update(staticTimeModel: StaticTimeModel)
}