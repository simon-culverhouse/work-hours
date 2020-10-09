package com.fragmentise.workhours.util

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fragmentise.workhours.database.WorkDay
import com.fragmentise.workhours.databinding.ListItemWorkDayBinding
import java.text.DateFormat.*
import java.text.SimpleDateFormat
import java.util.*

class HoursListAdaptor: ListAdapter<WorkDay, HoursListAdaptor.ViewHolder>(WorkDayDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(binding: ListItemWorkDayBinding) : RecyclerView.ViewHolder(binding.root){
        private val startTime: TextView = binding.startTime
        private val stopTime: TextView = binding.stopTime
        private val shiftTotal: TextView = binding.shiftTotal

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemWorkDayBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

        fun bind(item: WorkDay) {
            val res = itemView.context.resources
            val timeStart = item.startTimeMilli
            val timeStop = item.endTimeMilli
            val startTime = getTimeInstance(SHORT).format(timeStart)
            val stopTime = getTimeInstance(SHORT).format(timeStop)
            val todaysDate = SimpleDateFormat("d").format(Date())
            val databaseDate = SimpleDateFormat("d").format(timeStart)

            if (databaseDate == todaysDate) {

                this.startTime.text = startTime
                if (item.endTimeMilli > item.startTimeMilli) {
                    this.stopTime.text = stopTime
                    shiftTotal.text =
                        convertToHoursMinutes(
                            item.startTimeMilli,
                            item.endTimeMilli,
                            res
                        )
                } else {
                    this.stopTime.text = null
                    this.shiftTotal.text = null
                }
            }
        }
    }


}

class WorkDayDiffCallback : DiffUtil.ItemCallback<WorkDay>() {
    override fun areItemsTheSame(oldItem: WorkDay, newItem: WorkDay): Boolean {
        return oldItem.dayId == newItem.dayId
    }

    override fun areContentsTheSame(oldItem: WorkDay, newItem: WorkDay): Boolean {
        return oldItem == newItem
    }

}