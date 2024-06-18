package com.example.eventtracker.ui.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.eventtracker.model.EventData
import com.example.eventtracker.model.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) : ViewModel() {
    val currentUser = UserData()
    private val _userName = MutableStateFlow(currentUser.name)
    val userName = _userName.asStateFlow()
    private val _userEmail = MutableStateFlow(currentUser.email)
    val userEmail = _userEmail.asStateFlow()
    private val _userUsn = MutableStateFlow(currentUser.collegeId)
    val userUsn = _userUsn.asStateFlow()
    private val _userEvents = MutableStateFlow(listOf<EventData>())
    val userEvents = _userEvents.asStateFlow()
    var eventListener: ListenerRegistration? = null

    init {
        db.collection("users")
            .document(auth.currentUser!!.uid)
            .get()
            .addOnSuccessListener {
                _userName.value = it.get("name") as String
                _userEmail.value = it.get("email") as String
                _userUsn.value = it.get("collegeId") as String
            }
        val user = auth.currentUser
        if (user != null) {
           eventListener = db.collection("users")
                .document(user.uid)
                .collection("Hosted Events")
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        return@addSnapshotListener
                    }
                    if (value != null) {
                        val events = value.toObjects(EventData::class.java)
                        _userEvents.value = events
                    }
                }

        }
    }
    fun logOut() {
        auth.signOut()
        Log.d("TAG", auth.currentUser.toString())
    }
}