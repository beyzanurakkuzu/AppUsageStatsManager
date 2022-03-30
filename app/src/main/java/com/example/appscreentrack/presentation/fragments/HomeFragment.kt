package com.example.appscreentrack.presentation.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ViewFlipper
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appscreentrack.R
import com.example.appscreentrack.presentation.adapters.AppsUsageAdapter
import com.example.appscreentrack.presentation.main.calendar.CalendarAdapter
import com.example.appscreentrack.databinding.FragmentHomeBinding

import com.example.appscreentrack.domain.models.AppUsageState
import com.example.appscreentrack.presentation.main.utils.DateUtils.getDaysOfMonth
import com.example.appscreentrack.presentation.main.utils.ScreenUtils
import com.example.appscreentrack.presentation.main.utils.Utils
import com.example.appscreentrack.domain.viewmodel.HomeViewModel
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import kotlinx.coroutines.*

class HomeFragment : Fragment() {
    private val isPremium: Boolean = false
    private lateinit var binding: FragmentHomeBinding
    private lateinit var horizontalCalendarAdapter: CalendarAdapter
    private val data = getDaysOfMonth()
    private var timeStamp: Long = 0
    private val usageAdapter = AppsUsageAdapter()
    private val viewModel: HomeViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentHomeBinding.inflate(layoutInflater)
        setAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root


    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModelObservers()
        initHorizontalDatePicker()
        binding.imageViewActivePin.setImageDrawable(requireActivity().getDrawable(R.drawable.shape_app_tab_indicator))
        binding.backAppBar.imgBackButtonClick {
            Navigation.findNavController(requireView()).popBackStack()
        }
    }

    private fun initHorizontalDatePicker() {
        // Setting the padding such that the items will appear in the middle of the screen
        val padding: Int = ScreenUtils.getScreenWidth(requireContext()) / 2 - ScreenUtils.dpToPx(
            requireContext(),
            44
        )
        binding.recyclerViewHorizontalDatePicker.setPadding(padding, 0, padding, 0)
        val manager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.HORIZONTAL,false)
        binding.recyclerViewHorizontalDatePicker.layoutManager = manager
        binding.recyclerViewHorizontalDatePicker.setItemViewCacheSize(9)
        //Setting Adapter
        horizontalCalendarAdapter = CalendarAdapter {
            prepareFetchOperation(it)

        }
        horizontalCalendarAdapter.setData(data)
        CoroutineScope(Dispatchers.IO).launch {
            delay(260)
            withContext(Dispatchers.Main) {
                prepareFetchOperation(CalendarAdapter.focusedItem!!)
            }
        }

        binding.recyclerViewHorizontalDatePicker.adapter = horizontalCalendarAdapter

    }

    private fun prepareFetchOperation(it: Int) {
        println("clickedhoritem")
        binding.recyclerViewHorizontalDatePicker.smoothScrollToPosition(
            it
        )
        println("onItemSelected $it")

        timeStamp = data[it].timeStamp
        binding.flipper.switch(binding.progressBar)
        when (it) {
            8, 9 -> viewModel.fetchData(timeStamp)
            else -> if (isPremium)
                viewModel.fetchData(timeStamp)
            else
                binding.flipper.switch(binding.cardPremiumNotification)
        }
        horizontalCalendarAdapter.setFillOutLine(it)
    }

    private fun setPieChart(appNames: HashMap<String, Float>) {
        val pieChart = binding.pieChart
        val pieChartEntry = ArrayList<PieEntry>()
        val sortedAppNames =
            appNames.toList().sortedBy { (_, v) -> v }.toMap()

        for (i in sortedAppNames) {
            pieChartEntry.add(PieEntry(i.value, i.key))
        }

        val pieDataSet = PieDataSet(pieChartEntry, "")
        val pieData = PieData(pieDataSet)

        Utils.setPropertiesAndLegends(pieDataSet, pieChart, pieData, requireContext())
    }

    private fun setupViewModelObservers() {
        viewModel.todayUsageData.observe(viewLifecycleOwner, {
            updateView(it)
        })
    }

    private fun updateView(state: AppUsageState?) {
        when (state) {
            is AppUsageState.Loading -> binding.flipper.switch(binding.progressBar)
            is AppUsageState.Content -> {
                if (state.usageList.isEmpty())
                    binding.flipper.switch(binding.noDataView)
                else {
                    usageAdapter.submitList(state.usageList)
                    setPieChart(state.map)
                    binding.flipper.switch(binding.content)
                }
            }
            is AppUsageState.Error -> binding.flipper.switch(binding.noDataView)
        }
    }

    private fun setAdapter() {
        binding.recyclerViewUsageList.apply {
            adapter = usageAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }
}

fun ViewFlipper.switch(v: View) {
    while (currentView != v)
        showNext()
}

