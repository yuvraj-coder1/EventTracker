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
    private var hostedEventListener: ListenerRegistration?=null
    private var bookmarkedEventListener: ListenerRegistration?=null
    fun updateIsBookmarkedSelected(isBookmarkedSelected:Boolean) {
        _uiState.value = _uiState.value.copy(isBookmarksSelected = isBookmarkedSelected)
    }
    private fun fetchEvents() {
        val user = auth.currentUser
        if (user != null) {
            val userId = user.uid
            hostedEventListener = db.collection("users")
                .document(userId)
                .collection("Hosted Events")
                .addSnapshotListener{
                    snapshot, error ->
                    if (error != null) {
                        // Handle error
                        Log.d("TAG", "populateMessages: $error")
                        return@addSnapshotListener
                    }
                    if (snapshot != null && !snapshot.isEmpty) {
                        val hostedEvents = snapshot.toObjects(EventData::class.java)
                        _uiState.value = _uiState.value.copy(hostedEvents = hostedEvents)
                    }

                }
            bookmarkedEventListener = db.collection("users")
                .document(userId)
                .collection("Bookmarked Events")
                .addSnapshotListener {
                    snapshot, error ->
                    if (error != null) {
                        // Handle error
                        Log.d("TAG", "populateMessages: $error")
                        return@addSnapshotListener
                    }
                    if (snapshot != null && !snapshot.isEmpty) {
                        val bookmarkedEvents = snapshot.toObjects(EventData::class.java)
                        _uiState.value = _uiState.value.copy(bookmarkedEvents = bookmarkedEvents)
                    }
                    else {
                        _uiState.value = _uiState.value.copy(bookmarkedEvents = emptyList())
                    }
                }
        }
    }
    override fun onCleared() {
        super.onCleared()
        hostedEventListener?.remove()
        bookmarkedEventListener?.remove()
    }
    init {
        fetchEvents()
    }

}