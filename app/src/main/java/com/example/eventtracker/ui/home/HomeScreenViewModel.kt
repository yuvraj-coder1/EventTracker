package com.example.eventtracker.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventtracker.model.EventData
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val db: FirebaseFirestore,
) : ViewModel() {
    private val _isSearchOn = MutableStateFlow(false)
    val isSearchOn = _isSearchOn.asStateFlow()
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()
    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun toggleSearch() {
        _isSearchOn.value = !_isSearchOn.value
    }

    private val _eventList = MutableStateFlow(listOf(EventData()))

    @OptIn(FlowPreview::class)
    val eventList = searchQuery
        .debounce(1000)
        .onEach { _isSearching.update { true } }
        .combine(_eventList) { query, eventList ->
            if (query.isBlank()) {
                eventList
            } else {
                delay(2000)
                eventList.filter {
                    it.doesMatchSearchQuery(query)
                }
            }
        }.onEach { _isSearching.update { false } }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000), _eventList.value
        )


    init {
        db.collection("events").get().addOnSuccessListener {
            val events = it.toObjects(EventData::class.java)
            Log.d("events fetched", events.toString())
            _eventList.value = events
        }
    }
}