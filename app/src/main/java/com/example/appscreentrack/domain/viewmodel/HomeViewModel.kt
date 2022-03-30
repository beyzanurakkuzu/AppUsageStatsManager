package com.example.appscreentrack.domain.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appscreentrack.domain.models.AppUsageState
import com.example.appscreentrack.domain.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HomeRepository
) : ViewModel() {

    private val _viewState = MutableLiveData<AppUsageState>()
    val viewState: LiveData<AppUsageState>
        get() = _viewState
    val todayUsageData = MutableLiveData<AppUsageState>()

    init {
        fetchData(System.currentTimeMillis())
        viewModelScope.launch { repository.updateData() }
    }


    fun fetchData(millis: Long) {
        val tempHashMap = HashMap<String, Float>()
        todayUsageData.value = (AppUsageState.Loading)
        viewModelScope.launch(IO) {
            repository.getCurrentDayUsageData(millis).collect {
                it.usageList.forEachIndexed { index, appUsage ->
                    if (index < 4) {
                        tempHashMap[appUsage.app.appName] = appUsage.totalTime.toFloat()
                    } else {
                        val keyByIndex = tempHashMap.keys.elementAt(0) // Get key by index.
                        val valueOfElement = tempHashMap.getValue(keyByIndex) // Get value.
                        tempHashMap["Diğerleri"] = appUsage.totalTime.toFloat() + valueOfElement
                    }
                }

                withContext(Main) {
                    if (it.usageList.isEmpty()) {
                        todayUsageData.value = (AppUsageState.Error)
                    } else
                        todayUsageData.value = (AppUsageState.Content(it.usageList, tempHashMap))
                }
            }

        }
    }
}








