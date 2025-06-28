package com.example.eventtracker.ui.profile

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventtracker.data.login.NetworkLogInRepository
import com.example.eventtracker.model.EventData
import com.example.eventtracker.model.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val loginRepository: NetworkLogInRepository,
    private val sharedPreferences: SharedPreferences
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

    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userid = sharedPreferences.getString("userId", null)
                    ?: throw IllegalStateException("User ID not found in SharedPreferences")
                Log.d("ProfileScreenViewModel", "User ID: $userid")
                loginRepository.getUserById(userid).let {
                    _userName.value = it.username
                    _userEmail.value = it.email
                    _userUsn.value = it.collegeId
                }
            } catch (e: Exception) {
                Log.e("ProfileScreenViewModel", "Error getting user data: ${e.message}", e)
            }
        }

    }
}
