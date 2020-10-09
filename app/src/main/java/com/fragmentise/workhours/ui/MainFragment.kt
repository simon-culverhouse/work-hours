package com.fragmentise.workhours.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.fragmentise.workhours.R
import com.fragmentise.workhours.database.WorkDatabase
import com.fragmentise.workhours.databinding.MainFragmentBinding
import com.fragmentise.workhours.util.HoursListAdaptor


class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val navController by lazy { NavHostFragment.findNavController(this) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: MainFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false)
        val application = requireNotNull(this.activity).application
        val dataSource = WorkDatabase.getInstance(application).workDatabaseDao
        val viewModelFactory = MainViewModelFactory(dataSource, application)
        val mainViewModel =
            ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        binding.mainViewModel = mainViewModel

        binding.lifecycleOwner = this



        setHasOptionsMenu(true)

        mainViewModel.navigateToHistory.observe(this, Observer { isFinished ->
            if (isFinished) {
                //val action = (R.id.action_mainFragment_to_historyFragment)
                //navController.navigate(action)

                mainViewModel.doneNavigating()
            }
        })


        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(
            item,
            view!!.findNavController()
        )
                || super.onOptionsItemSelected(item)
    }


}
