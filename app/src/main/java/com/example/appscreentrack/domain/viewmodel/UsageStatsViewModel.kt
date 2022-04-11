package com.example.appscreentrack.domain.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appscreentrack.presentation.main.AppState
import com.example.appscreentrack.domain.repository.UsageStatsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class UsageStatsViewModel @Inject constructor(
    private val repository: UsageStatsRepository
) : ViewModel() {

    val todayUsageData = MutableLiveData<AppState>()

    init {
        fetchUsageStats(System.currentTimeMillis())
        viewModelScope.launch { repository.updateData() }
    }

    fun fetchUsageStats(millis: Long) {
        val tempHashMap = HashMap<String, Float>()
        todayUsageData.value = (AppState.Loading)
        viewModelScope.launch(IO) {

            repository.getCurrentDayUsageData(millis).collect {
                //clicked day app usage graphic
                it.usageList.forEachIndexed { index, appUsage ->
                    if (index < 4) tempHashMap[appUsage.app.appName] = appUsage.totalTime.toFloat()
                    else {
                        val keyByIndex = tempHashMap.keys.elementAt(0) // Get key by index.
                        val valueOfElement = tempHashMap.getValue(keyByIndex) // Get value.
                        tempHashMap["Diğerleri"] = appUsage.totalTime.toFloat() + valueOfElement
                    }
                }
                withContext(Main) {
                    if (it.usageList.isEmpty()) todayUsageData.value = (AppState.Error)
                    else todayUsageData.value = (AppState.Content(it.usageList, tempHashMap))
                }
            }
        }
    }
}