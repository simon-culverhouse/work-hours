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
import com.fragmentise.workhours.databinding.HoursTodayBinding
import com.fragmentise.workhours.util.HoursListAdaptor

/**
 * A simple [Fragment] subclass.
 */
class HoursToday : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: HoursTodayBinding = DataBindingUtil.inflate(inflater, R.layout.hours_today, container, false)
        val application = requireNotNull(this.activity).application
        val dataSource = WorkDatabase.getInstance(application).workDatabaseDao
        val viewModelFactory = MainViewModelFactory(dataSource, application)
        val mainViewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        binding.mainViewModel = mainViewModel

        binding.lifecycleOwner = this


        val adapter = HoursListAdaptor()
        binding.hoursList.adapter = adapter

        mainViewModel.days.observe(viewLifecycleOwner, Observer {
            it?.let { adapter.submitList(it) }
        })

        return binding.root
    }


}
