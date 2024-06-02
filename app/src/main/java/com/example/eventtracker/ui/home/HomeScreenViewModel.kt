package com.example.eventtracker.ui.home

import androidx.lifecycle.ViewModel
import com.example.eventtracker.model.EventData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeScreenViewModel : ViewModel() {
    //    private val _eventList= MutableStateFlow(EventData())
//    val eventList= _eventList.asStateFlow()
    val eventList =
        listOf(EventData(), EventData(), EventData(), EventData(), EventData(), EventData())
}