package com.example.eventtracker.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.eventtracker.model.EventData
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val db: FirebaseFirestore,
) : ViewModel() {
        private val _eventList= MutableStateFlow(listOf(EventData()))
    val eventList= _eventList.asStateFlow()
    init{
        db.collection("events").get().addOnSuccessListener {
            val events = it.toObjects(EventData::class.java)
            Log.d("events fetched",events.toString())
            _eventList.value = events
        }
    }
}