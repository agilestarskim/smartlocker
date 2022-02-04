package com.example.smartlocker.data.room

import androidx.room.*

@Dao
interface StaticTimeDao {

    @Query("SELECT * FROM staticTime ORDER BY time")
    fun getAll(): List<StaticTimeModel>

    @Query("SELECT * FROM staticTime WHERE time == :time")
    fun get(time: Int): StaticTimeModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(staticTimeModel: StaticTimeModel )

    @Update
    fun update(staticTimeModel: StaticTimeModel)

    @Query("DELETE FROM staticTime ")
    fun delete()
}