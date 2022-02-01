package com.example.smartlocker.data.room

import androidx.room.*

@Dao
interface StaticTimeDao {

    @Query("SELECT * FROM staticTimeDB ORDER BY time")
    fun getAll(): List<StaticTimeModel>

    @Query("SELECT * FROM staticTimeDB WHERE (:time) == time")
    fun get(time: Int): StaticTimeModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(staticTimeModel: StaticTimeModel )

    @Update
    fun update(staticTimeModel: StaticTimeModel)

    @Query("DELETE FROM staticTimeDB WHERE time == :time")
    fun delete(time:Int)
}