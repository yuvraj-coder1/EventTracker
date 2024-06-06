package com.example.eventtracker.ui.postNewEvent

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.eventtracker.model.EventData
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class PostNewEventViewModel @Inject constructor(
    private val db: FirebaseFirestore,
    private val storage: FirebaseStorage
) : ViewModel() {
    private val _uiState = MutableStateFlow(PostNewEventUiState())
    val uiState: StateFlow<PostNewEventUiState> = _uiState.asStateFlow()
    var uri by mutableStateOf<Uri?>(null)

    fun updateEventName(eventName: String) {
        _uiState.value = _uiState.value.copy(eventName = eventName)
    }

    fun updateEventDescription(eventDescription: String) {
        _uiState.value = _uiState.value.copy(eventDescription = eventDescription)
    }

    fun updateEventCategory(eventCategory: String) {
        _uiState.value = _uiState.value.copy(eventCategory = eventCategory)
    }

    fun updateEventDate(eventDate: String) {
        _uiState.value = _uiState.value.copy(eventDate = eventDate)
    }

    fun updateEventTime(eventTime: String) {
        _uiState.value = _uiState.value.copy(eventTime = eventTime)
    }

    fun updateLocation(location: String) {
        _uiState.value = _uiState.value.copy(location = location)
    }

    fun updateId(id: String) {
        _uiState.value = _uiState.value.copy(id = id)
    }

    private suspend fun uploadEventImage(eventImageUri: Uri?): String {
        val storageRef = storage.reference.child("images/${_uiState.value.id}")
        if (eventImageUri != null) {
            try {
                storageRef.putFile(eventImageUri).await()
                val downloadUrl = storageRef.downloadUrl.await()
                Log.d("downloadUrl", "uploadEventImage: $downloadUrl")
                return downloadUrl.toString()
//                _uiState.value = _uiState.value.copy(eventImage = downloadUrl)
            } catch (e: Exception) {
                Log.e("UploadError", "Upload failed", e)
            }
        }
        return ""
    }

    fun addEventToDatabase() {
        val id = db.collection("events").document().id
        updateId(id)
        CoroutineScope(Dispatchers.Main).launch {
            val url = uploadEventImage(uri)
            _uiState.value = _uiState.value.copy(eventImage = url)
            val event = EventData(
                name = _uiState.value.eventName,
                description = _uiState.value.eventDescription,
                category = _uiState.value.eventCategory,
                date = _uiState.value.eventDate,
                time = _uiState.value.eventTime,
                location = _uiState.value.location,
                image = _uiState.value.eventImage
            )
            Log.d("url update", "addEventToDatabase: $url")
            db.collection("events").document(id).set(event).addOnSuccessListener {
                Log.d(TAG, "addEventToDatabase: Suceess")
                updateEventName("")
                updateEventDescription("")
                updateEventCategory("")
                updateEventDate("")
                updateEventTime("")
                updateLocation("")
                uri = null
            }
                .addOnFailureListener {
                    Log.d(TAG, "addEventToDatabase: Failed To Add Event`")
                }
        }
    }
}

data class PostNewEventUiState(
    val id: String = "",
    val eventName: String = "",
    val eventDescription: String = "",
    val eventCategory: String = "",
    val eventDate: String = "",
    val eventTime: String = "",
    val location: String = "",
    val eventImage: String = "",
)