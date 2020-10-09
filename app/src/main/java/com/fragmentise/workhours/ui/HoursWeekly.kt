package com.fragmentise.workhours.ui


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fragmentise.workhours.R
import com.fragmentise.workhours.database.WorkDatabase
import com.fragmentise.workhours.databinding.HoursWeeklyBinding
import com.fragmentise.workhours.util.WeekListAdaptor


class HoursWeekly : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return inflater.inflate(R.layout.hours_weekly, container, false)
        val binding: HoursWeeklyBinding = DataBindingUtil.inflate(inflater, R.layout.hours_weekly, container, false)
        val application = requireNotNull(this.activity).application
        val dataSource = WorkDatabase.getInstance(application).workDatabaseDao
        val viewModelFactory = MainViewModelFactory(dataSource, application)
        val mainViewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        binding.mainViewModel = mainViewModel

        binding.lifecycleOwner = this
        val weekAdaptor = WeekListAdaptor()
        binding.weekList.adapter = weekAdaptor

        mainViewModel.days.observe(viewLifecycleOwner, Observer {it?.let {weekAdaptor.submitList(it)}
        })

        return binding.root
    }


}
