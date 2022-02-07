package com.example.smartlocker.data.room

import androidx.room.*

@Dao
interface StaticDateDao {
    @Query("SELECT * FROM staticDate ORDER BY date DESC ")
    fun getAll(): List<StaticDateModel>

    @Query("SELECT * FROM staticDate WHERE date == :day")
    fun get(day: String) : StaticDateModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(staticDateModel: StaticDateModel )

    @Query("DELETE FROM staticDate")
    fun delete()

    @Query("UPDATE staticDate SET state = :state WHERE state == 0")
    fun update(state : Int)
}