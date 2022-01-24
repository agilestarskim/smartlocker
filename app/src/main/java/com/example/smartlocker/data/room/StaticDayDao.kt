package com.example.smartlocker.data.room

import androidx.room.*

@Dao
interface StaticDayDao {
    @Query("SELECT * FROM staticDayDB")
    fun getAll(): List<StaticDayModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(staticDayModel: StaticDayModel )

}