package com.example.eventtracker.ui.home

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.collection.emptyLongSet
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.saveable.rememberSaveable

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventtracker.data.event.EventApiService
import com.example.eventtracker.data.event.NetworkEventRepository
import com.example.eventtracker.dto.toEventData
import com.example.eventtracker.model.EventData
import com.example.eventtracker.model.GetEventResponse
import com.google.apphosting.datastore.testing.DatastoreTestTrace.FirestoreV1Action.Listen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val eventRepository: NetworkEventRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _isSearchOn = MutableStateFlow(false)
    val isSearchOn = _isSearchOn.asStateFlow()
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()
    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()
    private val _bookmarkedEventsId = MutableStateFlow<List<String>>(emptyList())
    val bookmarkedEventsId = _bookmarkedEventsId.asStateFlow()
    private val _bookmarkedEvents = MutableStateFlow<List<EventData>>(emptyList())
    val bookmarkedEvents = _bookmarkedEvents.asStateFlow()
    private val _hostedEvents = MutableStateFlow<List<EventData>>(emptyList())
    val hostedEvents = _hostedEvents.asStateFlow()

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

    fun getEvents() {
        viewModelScope.launch {
            try {
                val events = eventRepository.getEvents()
                _eventList.value = events.data?.map {
                    it.toEventData()
                }?:emptyList()
//                _eventList.value = _allEvents.value
            } catch (e: Exception) {
                Toast.makeText(context, "Error getting events", Toast.LENGTH_SHORT).show()
                Log.d("TAG", "getEvents: $e")
            }

        }
        getBookmarkedEvents()
        getUserEvents()

    }

    fun getBookmarkedEvents() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val events = eventRepository.getBookmarkedEvents()
                _bookmarkedEvents.value = events.data?.map {
                    it.toEventData()
                }?:emptyList()

            } catch (e: Exception) {
//                Toast.makeText(context, "Error getting bookmarked events", Toast.LENGTH_SHORT)
//                    .show()
                Log.e("getbookmarkedEventsId", "getbookmarkedEventsId: $e")
            }
        }
    }

    fun getUserEvents() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val events = eventRepository.getUserEvents()
                _hostedEvents.value = events.data?.map {
                    it.toEventData()
                }?:emptyList()

            } catch (e: Exception) {
//                Toast.makeText(context, "Error getting events", Toast.LENGTH_SHORT).show()
                Log.e("getEvents", "getEvents: $e")
            }
        }
    }

    fun onInterestedClicked(event: EventData, onSuccess: () -> Unit, onFailure: () -> Unit): Unit {
        Log.d("event", event.eventId.toString())
        val isBookmarked = bookmarkedEventsId.value.any { it == event.eventId }
        Log.d("isBookmarked", isBookmarked.toString())
        try {
            if (isBookmarked)
                unBookmarkEvent(event.eventId, {
                    Toast.makeText(context, "Event UnBookmarked", Toast.LENGTH_SHORT).show()
                }, onFailure)
            else
                bookmarkEvent(event.eventId, onSuccess, onFailure)
        } catch (
            e: Exception
        ) {
            Log.d("onInterestedClicked", "onInterestedClicked: $e")
            onFailure()
        }
        Log.d("Bookmark List","${bookmarkedEventsId.value}")
    }

    fun bookmarkEvent(eventId: String, onSuccess: () -> Unit, onFailure: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val resp = eventRepository.bookmarkEvent(eventId)
                withContext(Dispatchers.Main) {
                    if (resp.success) {
                        _bookmarkedEventsId.update {
                            it + eventId
                        }
                        onSuccess()
                    } else
                        onFailure()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("bookmarkEvent", "Error: ${e.message}", e)
                    onFailure()
                }
            }

        }
    }

    fun unBookmarkEvent(eventId: String, onSuccess: () -> Unit, onFailure: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val resp = eventRepository.unBookmarkEvent(eventId)
                withContext(Dispatchers.Main) {
                    if (resp.success) {
                        _bookmarkedEventsId.update {
                            it - eventId
                        }
                        onSuccess()
                    } else
                        onFailure()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("unBookmarkEvent", "Error: ${e.message}", e)
                    onFailure()
                }
            }

        }
    }

    fun checkIfBookmarked(event: EventData): Boolean {
        if (bookmarkedEventsId.value.isEmpty())
            return false
        return bookmarkedEventsId.value.any { it == event.eventId }
    }
}
