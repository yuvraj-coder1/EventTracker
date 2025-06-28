package com.example.eventtracker.ui.postNewEvent

import android.content.ContentValues.TAG
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventtracker.data.event.NetworkEventRepository
import com.example.eventtracker.dto.CreateEventRequest
import com.example.eventtracker.dto.EventDto
import com.example.eventtracker.model.EventData
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

@HiltViewModel
class PostNewEventViewModel @Inject constructor(
    private val db: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val auth: FirebaseAuth,
    private val eventRepository: NetworkEventRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _uiState = MutableStateFlow(PostNewEventUiState())
    val uiState: StateFlow<PostNewEventUiState> = _uiState.asStateFlow()
    var uri by mutableStateOf<Uri?>(null)
    val initialUiState = PostNewEventUiState()
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

    fun updateEventLink(eventLink: String) {
        _uiState.value = _uiState.value.copy(eventLink = eventLink)
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

    fun addEventToDatabase(onSuccess: () -> Unit, onFail: () -> Unit = {}) {
        val namePart = _uiState.value.eventName.toString().toPart()
        val descriptionPart = _uiState.value.eventDescription.toString().toPart()
        val categoryPart = _uiState.value.eventCategory.toString().toPart()
        val datePart = _uiState.value.eventDate.toString().toPart()
        val timePart = _uiState.value.eventTime.toString().toPart()
        val locationPart = _uiState.value.location.toString().toPart()
        val imageLinkPart = _uiState.value.eventLink.toString().toPart()
        val image: MultipartBody.Part? = uri?.let {uriToMultiPartFile(context = context, uri = uri!!, partname = "image")}
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val resp = eventRepository.createEvent(
                    namePart = namePart,
                    descriptionPart = descriptionPart,
                    datePart = datePart,
                    timePart = timePart,
                    locationPart = locationPart,
                    imagePart = image,
                    categoryPart = categoryPart,
                    eventLinkPart = imageLinkPart
                )
                withContext(context = Dispatchers.Main) {
                    if (resp.success) {
                        onSuccess()
                        _uiState.value = initialUiState
                        uri = null
                    } else {
                        onFail()
                    }
                }
            } catch (exception: Exception) {
                withContext(context = Dispatchers.Main) {
                    onFail()
                }
                Log.e("addEventToDatabase", "addEventToDatabase: $exception")
            }
        }
    }

    fun uriToMultiPartFile(
        context: Context,
        uri: Uri,
        partname: String
    ): MultipartBody.Part {
        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw IllegalArgumentException("Unable to open URI:$uri")
        val bytes = inputStream.readBytes()
        inputStream.close()
        val mimeType = context.contentResolver.getType(uri)
            ?: "application/octet-stream"
        val requestFile = bytes.toRequestBody(mimeType.toMediaTypeOrNull())
        val filename = "${System.currentTimeMillis()}.jpg"
        return MultipartBody.Part.createFormData(
            partname,
            filename,
            requestFile
        )
    }
    fun String.toPart() =
        toRequestBody("text/plain".toMediaType())
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
    val eventLink: String = ""
)