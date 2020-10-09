package com.fragmentise.workhours.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


@Dao
interface WorkDatabaseDao {

    @Insert
    fun insert(day: WorkDay)

    @Update
    fun update(day: WorkDay)

    @Query("DELETE FROM daily_work_hours")
    fun clear()

    @Query("SELECT * FROM daily_work_hours ORDER BY dayId DESC")
    fun getAllDays(): LiveData<List<WorkDay>>

    @Query("SELECT * FROM daily_work_hours ORDER BY dayId DESC LIMIT 1")
    fun getToday(): WorkDay?

/*    @Query("SELECT dayId FROM daily_work_hours WHERE end_time_milli != null" )
    fun getFirst(): WorkDay*/
}