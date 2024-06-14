package com.example.eventtracker.ui.home

import android.util.Log
import androidx.collection.emptyLongSet
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.saveable.rememberSaveable

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventtracker.model.EventData
import com.google.apphosting.datastore.testing.DatastoreTestTrace.FirestoreV1Action.Listen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
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
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ViewModel() {
    private val _isSearchOn = MutableStateFlow(false)
    val isSearchOn = _isSearchOn.asStateFlow()
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()
    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()
    var eventListener: ListenerRegistration? = null
    var bookmarkEventListener: ListenerRegistration? = null
    private val _bookmarkedEvents = MutableStateFlow(emptyList<EventData>())
    val bookmarkedEvents = _bookmarkedEvents.asStateFlow()

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
        eventListener = db.collection("events").addSnapshotListener { value, error ->
            if (error != null) {
                Log.d("TAG", "populateMessages: $error")
                return@addSnapshotListener
            }
            val events = value?.toObjects(EventData::class.java)
            Log.d("events fetched", events.toString())
            if (events != null) {
                _eventList.value = events
            }
        }
        bookmarkEventListener = auth.currentUser?.let {
            db.collection("users")
                .document(it.uid)
                .collection("Bookmarked Events")
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        Log.d("TAG", "populateMessages: $error")
                        return@addSnapshotListener
                    }
                    val events = value?.toObjects(EventData::class.java)
                    if (events != null) {
                        _bookmarkedEvents.value = events
                    }

                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        eventListener?.remove()
        bookmarkEventListener?.remove()
    }

    init {
        getEvents()
    }

//   suspend fun onInterestedClicked(event: EventData, isBookmarked: Boolean):Boolean {
//        val currentUserId = auth.currentUser?.uid
//        var wasSuccess:Boolean = false
//        if (currentUserId != null) {
//            if (!isBookmarked) {
//                db.collection("users")
//                    .document(currentUserId)
//                    .collection("Bookmarked Events")
//                    .add(event)
//                    .addOnSuccessListener {
//                        Log.d("TAG", "onInterestedClicked: Success")
//                        wasSuccess = true
////                        Log.d("TAG", "onInterestedClicked: $wasSuccess")
//                    }
//                    .addOnFailureListener {
//                        Log.d("TAG", "onInterestedClicked: Failure")
//                    }
//            } else {
//                db.collection("users")
//                    .document(currentUserId)
//                    .collection("Bookmarked Events").whereEqualTo("eventId", event.eventId)
//                    .get()
//                    .addOnSuccessListener { querySnapshot ->
//                        if (querySnapshot.documents.isNotEmpty()) {
//                            val documentId = querySnapshot.documents[0].id
//                            db.collection("users")
//                                .document(currentUserId)
//                                .collection("Bookmarked Events")
//                                .document(documentId)
//                                .delete()
//                                .addOnSuccessListener {
//                                    Log.d("TAG", "Event Deleted Successful")
//                                    wasSuccess = true
//                                }
//                                .addOnFailureListener {
//                                    Log.d("TAG", "Event Deletion Failed")
//                                }
//                        }
//                        else {
//                            Log.d("TAG", "Could not find event")
//                        }
//                    }
//                    .addOnFailureListener {
//                        Log.d("TAG", "Could not find event")
//                    }
//            }
//        }
//        Log.d("TAG", "wasSuccess: $wasSuccess")
//        return if(wasSuccess) !isBookmarked else isBookmarked
//    }

    suspend fun onInterestedClicked(event: EventData, isBookmarked: Boolean): Boolean {
        val currentUserId = auth.currentUser?.uid
        return if (currentUserId != null) {
            if (!isBookmarked) {
                try {
                    db.collection("users")
                        .document(currentUserId)
                        .collection("Bookmarked Events")
                        .add(event)
                        .await() // Use await() to suspend until the operation completes
                    Log.d("TAG", "onInterestedClicked: Success")
                    true // Return true if successful
                } catch (e: Exception) {
                    Log.d("TAG", "onInterestedClicked: Failure", e)
                    false // Return false on failure
                }
            } else {
                try {
                    val querySnapshot = db.collection("users")
                        .document(currentUserId)
                        .collection("Bookmarked Events")
                        .whereEqualTo("eventId", event.eventId)
                        .get()
                        .await()
                    if (querySnapshot.documents.isNotEmpty()) {
                        val documentId = querySnapshot.documents[0].id
                        db.collection("users")
                            .document(currentUserId)
                            .collection("Bookmarked Events")
                            .document(documentId)
                            .delete()
                            .await()
                        Log.d("TAG", "Event Deleted Successful")
                        false
                    } else {
                        Log.d("TAG", "Could not find event")
                        true}
                } catch (e: Exception) {
                    Log.d("TAG", "Could not find event", e)
                    true
                }
            }
        } else {
            false // Return false if no user is logged in
        }
    }


    fun checkIfBookmarked(event: EventData): Boolean {
        if(bookmarkedEvents.value.isEmpty())
            return false
        return bookmarkedEvents.value.contains(event)
    }
}