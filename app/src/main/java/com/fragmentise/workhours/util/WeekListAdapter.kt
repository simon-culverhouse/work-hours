package com.fragmentise.workhours.util

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fragmentise.workhours.database.WorkDay
import com.fragmentise.workhours.databinding.ListItemWeekBinding
import java.text.SimpleDateFormat
import java.util.*

class WeekListAdaptor: ListAdapter<WorkDay, WeekListAdaptor.ViewHolder>(WeekWorkDayDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(binding: ListItemWeekBinding) : RecyclerView.ViewHolder(binding.root){
        private val weekNumber: TextView = binding.weekNumber
        private val totalHoursWeek: TextView = binding.totalHours
        private val weekStartDate: TextView = binding.weekStartDate

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemWeekBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

        fun bind(item: WorkDay) {

            val holderNext = item.dayId
            val test = item.dayId
            val res = itemView.context.resources
            val timeStart = item.startTimeMilli
            val totalHours = convertToHoursMinutes(item.startTimeMilli,item.endTimeMilli,res)
            val weekNumber = SimpleDateFormat("w", Locale.ENGLISH).format(timeStart)
            val weekDate = SimpleDateFormat("dd MMMM", Locale.ENGLISH).format(timeStart)
            val todaysDate = SimpleDateFormat("d", Locale.ENGLISH).format(Date())
            val databaseDate = SimpleDateFormat("d", Locale.ENGLISH).format(timeStart)
            val currentWeekNumber = weekNumber

            this.weekStartDate.text = weekNumber
            this.weekNumber.text = holderNext.toString()
            this.totalHoursWeek.text = totalHours



            }
    }


}

class WeekWorkDayDiffCallback : DiffUtil.ItemCallback<WorkDay>() {
    override fun areItemsTheSame(oldItem: WorkDay, newItem: WorkDay): Boolean {
        return oldItem.dayId == newItem.dayId
    }

    override fun areContentsTheSame(oldItem: WorkDay, newItem: WorkDay): Boolean {
        return oldItem == newItem
    }

}