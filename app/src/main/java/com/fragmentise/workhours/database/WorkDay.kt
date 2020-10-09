package com.fragmentise.workhours.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_work_hours")
data class WorkDay(
    @PrimaryKey(autoGenerate = true)
    var dayId: Long = 0L,

    @ColumnInfo(name = "start_time_milli")
    val startTimeMilli: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "end_time_milli")
    var endTimeMilli: Long = startTimeMilli,

    @ColumnInfo(name = "employer_name")
    var employer:String = "Lachiever"

)