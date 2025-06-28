package com.example.eventtracker.ui.userEventsScreen

import android.util.Log
import androidx.collection.emptyLongSet
import androidx.lifecycle.ViewModel
import com.example.eventtracker.model.EventData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class UserEventsScreenViewModel @Inject constructor(
    private val db : FirebaseFirestore,
    private val auth : FirebaseAuth
) : ViewModel() {
    private val _uiState = MutableStateFlow(UserEventsScreenUiState())
    val uiState: StateFlow<UserEventsScreenUiState> = _uiState.asStateFlow()
    fun updateIsBookmarkedSelected(isBookmarkedSelected:Boolean) {
        _uiState.value = _uiState.value.copy(isBookmarksSelected = isBookmarkedSelected)
    }
}