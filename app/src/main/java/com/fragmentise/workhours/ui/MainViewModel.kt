package com.fragmentise.workhours.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.fragmentise.workhours.database.WorkDatabaseDao
import com.fragmentise.workhours.database.WorkDay
import com.fragmentise.workhours.util.WeekListAdaptor
import kotlinx.coroutines.*
import java.text.DateFormat.SHORT
import java.text.DateFormat.getTimeInstance
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class MainViewModel (
    val database: WorkDatabaseDao,
    application: Application) : AndroidViewModel(application) {

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var today = MutableLiveData<WorkDay?>()

    private val _dateToday = MutableLiveData<String>()
    val dateToday: LiveData<String>
        get () = _dateToday

    private val _startTime = MutableLiveData<String>()
    val startTime: LiveData<String>
        get () = _startTime

    private val _stopTime = MutableLiveData<String>()
    val stopTime: LiveData<String>
        get () = _stopTime

    private val _navigateToHistory = MutableLiveData<Boolean>(false)
    val navigateToHistory: LiveData<Boolean>
        get() = _navigateToHistory

    private val currentDate = SimpleDateFormat("EEEE MMM d yyyy", Locale.UK)

    val days = database.getAllDays()

   val startButtonVisible = Transformations.map(today) {
        it == null
    }

    val stopButtonVisible = Transformations.map(today) {
        it != null
    }


    init {
        initializeToday()
        _dateToday.value = currentDate.format(Date())
        loadWeeks()

    }

    private fun initializeToday() {

        uiScope.launch {
            today.value = getTodayFromDatabase()
        }
    }

    private suspend fun getTodayFromDatabase(): WorkDay? {
        return withContext(Dispatchers.IO) {
            var today = database.getToday()
            if (today?.endTimeMilli != today?.startTimeMilli) {
                today = null
            }
            today
        }
    }

    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            database.clear()
        }
    }

    private suspend fun update(today: WorkDay) {
        withContext(Dispatchers.IO) {
            database.update(today)
        }
    }

    private suspend fun insert(today: WorkDay) {
        withContext(Dispatchers.IO) {
            database.insert(today)
        }
    }

    fun onStartRecording() {
        _startTime.value = null
        _stopTime.value = null

        uiScope.launch {
            val newDay = WorkDay()
            insert(newDay)
            today.value = getTodayFromDatabase()
            _startTime.value = getTimeInstance(SHORT).format(today.value?.startTimeMilli)
        }
    }

    fun onStopRecording() {
        uiScope.launch {
            val newDay = today.value ?: return@launch

            // Update today in the database to add the end time.
            newDay.endTimeMilli = System.currentTimeMillis()

            update(newDay)
            _stopTime.value = getTimeInstance(SHORT).format(newDay.endTimeMilli)
            // reset today
            initializeToday()
        }
    }

    fun loadWeeks() {

        println("ppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppp")

        if (days.value?.first() != null) {

            var totalTime = 0
            val firstEntry = days.value?.last()
            val lastEntry = days.value?.first()
            var weekNum = (SimpleDateFormat("w", Locale.ENGLISH).format(firstEntry)).toInt()
            var oldWeekNum = weekNum
            val entry = lastEntry?.dayId!!.toInt()
            for (item in 1 until entry) {

                val lineEntry = days.value?.get(item)
                val startTime = lineEntry!!.startTimeMilli
                val endTime = lineEntry.endTimeMilli
                weekNum = (SimpleDateFormat("w", Locale.ENGLISH).format(startTime)).toInt()
                if (weekNum == oldWeekNum) {

                    val durationMilli = (endTime - startTime)
                    val totalMinutes = (TimeUnit.MINUTES.convert(durationMilli, TimeUnit.MILLISECONDS)).toInt()
                    totalTime += totalMinutes

                }else {
                    oldWeekNum++


                }

            }

            println("$totalTime minutes is the total for week no: $weekNum")
        }
    }

    fun hoursByMonth() {

        if (days.value?.first() != null) {
            var firstMonth = 1
            var printMonth = ""
            var totalTime = 0
            val weekList = days
            // val firstEntry = days.value?.last()
            val lastEntry = days.value?.first()
            val entry = lastEntry?.dayId!!.toInt()
            for (item in 1 until entry) {

                val lineEntry = weekList.value?.get(item)
                val startTime = lineEntry!!.startTimeMilli
                val endTime = lineEntry.endTimeMilli
                printMonth = (SimpleDateFormat("MMMM", Locale.ENGLISH).format(startTime)).toString()
                val monthNum = (SimpleDateFormat("MM", Locale.ENGLISH).format(startTime)).toInt()
                if (monthNum == firstMonth) {
                    val durationMilli = (endTime - startTime)
                    val totalMinutes = (TimeUnit.MINUTES.convert(durationMilli, TimeUnit.MILLISECONDS)).toInt()
                    totalTime += totalMinutes
                }else {
                    // monthList.value(printMonth) = (totalTime.toString())
                    firstMonth++
                }

            }
            println("$totalTime minutes is the total for month: $printMonth")
        }
    }

    fun onClear() {
        uiScope.launch {
            // Clear the database table.
            clear()

            // And clear tonight since it's no longer in the database
            today.value = null
        }

        // Show a snackbar message, because it's friendly.
        // _showSnackbarEvent.value = true
    }

    fun navigateHistory() {
        _navigateToHistory.value = true
    }
    fun doneNavigating() {
        _navigateToHistory.value = false
    }







    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


}
