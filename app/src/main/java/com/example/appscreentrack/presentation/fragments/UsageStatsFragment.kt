package com.example.appscreentrack.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ViewFlipper
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appscreentrack.R
import com.example.appscreentrack.databinding.FragmentUsageStatsBinding
import com.example.appscreentrack.domain.viewmodel.UsageStatsViewModel
import com.example.appscreentrack.presentation.adapters.AppsUsageAdapter
import com.example.appscreentrack.presentation.main.AppState
import com.example.appscreentrack.presentation.main.calendar.CalendarAdapter
import com.example.appscreentrack.presentation.main.calendar.CalenderLayoutManager
import com.example.appscreentrack.presentation.main.calendar.ClickListener
import com.example.appscreentrack.presentation.main.utils.DateUtils.getDaysOfMonth
import com.example.appscreentrack.presentation.main.utils.PieCartUtils
import com.example.appscreentrack.presentation.main.utils.ScreenUtils
import kotlinx.coroutines.*

class UsageStatsFragment : Fragment() {
    private val isPremium: Boolean = true
    private lateinit var binding: FragmentUsageStatsBinding
    private lateinit var horizontalCalendarAdapter: CalendarAdapter
    private val data = getDaysOfMonth()
    private var timeStamp: Long = 0
    private val usageAdapter = AppsUsageAdapter()
    private val viewModel: UsageStatsViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentUsageStatsBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModelObservers()
        initHorizontalDatePicker()
        binding.imageViewActivePin.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.shape_app_tab_indicator
            )
        )
        navigateBack()
        setAdapter()
    }

    private fun navigateBack() {
        binding.backAppBar.imageBackButtonClick {
            Navigation.findNavController(requireView()).popBackStack()
        }
    }

    private fun setupViewModelObservers() {
        viewModel.todayUsageData.observe(viewLifecycleOwner) {
            updateView(it)
        }
    }

    //Get Calendar
    private fun initHorizontalDatePicker() {
        // Setting the padding such that the items will appear in the middle of the screen
        ScreenUtils.setPadding(
            requireActivity(),
            requireContext(),
            binding.recyclerViewHorizontalDatePicker
        )

        val manager = CalenderLayoutManager(requireContext())
        manager.callback = object : ClickListener {
            override fun onItemClicked(position: Int) {
                fetchUsageData(position)
            }
        }

        //Setting Adapter
        horizontalCalendarAdapter = CalendarAdapter(requireContext())
        horizontalCalendarAdapter.setData(data)
        horizontalCalendarAdapter.callback = object : ClickListener {
            override fun onItemClicked(position: Int) {
                binding.recyclerViewHorizontalDatePicker.smoothScrollToPosition(position)
                fetchUsageData(position)
            }
        }
        CoroutineScope(Dispatchers.IO).launch {
            delay(260)
            withContext(Dispatchers.Main) {
                binding.recyclerViewHorizontalDatePicker.smoothScrollToPosition(CalendarAdapter.focusedItem!!)
            }
        }

        setHorizontalDatePicker(manager)
    }

    private fun fetchUsageData(layoutPosition: Int) {
        timeStamp = data[layoutPosition].timeStamp
        if (!isPremium) {
            when (layoutPosition) {
                8, 9 -> viewModel.fetchUsageStats(timeStamp)
                else ->
                    binding.flipper.switch(binding.cardPremiumNotification)
            }
        } else {
            viewModel.fetchUsageStats(timeStamp)
        }
        horizontalCalendarAdapter.setFillOutLine(layoutPosition)
    }

    private fun setHorizontalDatePicker(manager: RecyclerView.LayoutManager) {
        binding.recyclerViewHorizontalDatePicker.apply {
            adapter = horizontalCalendarAdapter
            layoutManager = manager
        }
    }

    private fun setAdapter() {
        binding.recyclerViewUsageList.apply {
            adapter = usageAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    //switch with update
    private fun updateView(state: AppState?) {
        when (state) {
            is AppState.Loading -> binding.flipper.switch(binding.progressBar)
            is AppState.Content -> {
                if (state.usageList.isEmpty())
                    binding.flipper.switch(binding.noDataView)
                else {
                    usageAdapter.submitList(state.usageList)
                    PieCartUtils.setPieChart(state.map, binding.pieChart, requireContext())
                    binding.flipper.switch(binding.content)
                }
            }
            is AppState.Error -> binding.flipper.switch(binding.noDataView)
            else -> {
            }
        }
    }
}

fun ViewFlipper.switch(v: View) {
    while (currentView != v)
        showNext()
}