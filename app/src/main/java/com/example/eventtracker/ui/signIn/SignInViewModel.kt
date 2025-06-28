package com.example.eventtracker.ui.signIn

import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.security.crypto.EncryptedSharedPreferences
import com.example.eventtracker.data.login.NetworkLogInRepository
import com.example.eventtracker.model.SignUpResponse
import com.example.eventtracker.model.UserData
import com.example.eventtracker.model.UserLogInResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.core.content.edit

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val loginRepository: NetworkLogInRepository,
    private val encryptedSharedPreferences: SharedPreferences
) : ViewModel() {
    private val _uiState = MutableStateFlow(SignInUiState())
    val uiState: StateFlow<SignInUiState> = _uiState.asStateFlow()
    var inProcess by mutableStateOf(false)
    fun updateEmail(email: String) {
        _uiState.value = _uiState.value.copy(email = email)
    }

    fun updatePassword(password: String) {
        _uiState.value = _uiState.value.copy(password = password)
    }

    fun updateConfirmPassword(confirmPassword: String) {
        _uiState.value = _uiState.value.copy(confirmPassword = confirmPassword)
    }

    fun updateName(name: String) {
        _uiState.value = _uiState.value.copy(username = name)
    }

    fun updateIsSignIn(isSignIn: Boolean) {
        _uiState.value = _uiState.value.copy(isSignIn = isSignIn)
    }

    fun updateCollegeId(collegeId: String) {
        _uiState.value = _uiState.value.copy(collegeId = collegeId)
    }

    fun signIn(
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        viewModelScope.launch {
            inProcess = true
            try {
                val response: UserLogInResponse = loginRepository.signInUser(
                    username = uiState.value.username,
                    password = uiState.value.password
                )
                if (response.success) {
                    encryptedSharedPreferences.edit() { putString("jwt", response.data.jwt) }
                    encryptedSharedPreferences.edit() { putString("refreshToken", response.data.refreshToken) }
                    encryptedSharedPreferences.edit() { putString("userId", response.data.userId) }
                    Log.d("jwt", "signIn: ${response.data.jwt}")
                    onSuccess()
                } else {
                    onFailure()
                }
            } catch (
                e: Exception
            ) {
                Log.e("SignIn", e.message.toString())
                onFailure()
            } finally {
                inProcess = false
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun signUp(
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        if (uiState.value.password != uiState.value.confirmPassword)
            return
        viewModelScope.launch {
            inProcess = true
            try {
                val response: SignUpResponse = loginRepository.signUpUser(
                    username = uiState.value.username,
                    password = uiState.value.password,
                    collegeId = uiState.value.collegeId,
                    email = uiState.value.email
                )
                if (response.success) {
                    onSuccess()
                } else {
                    onFailure()
                }
            } catch (e: Exception) {
                Log.e("SignUp", e.message.toString())
                onFailure()
            } finally {
                inProcess = false
            }

        }
    }
    fun signOut() {
        encryptedSharedPreferences.edit() { putString("jwt", null) }
        encryptedSharedPreferences.edit() { putString("refreshToken", null) }
        encryptedSharedPreferences.edit() { putString("userId", null) }
    }
    fun isUserLoggedIn(): Boolean {
        return !encryptedSharedPreferences.getString("refreshToken",null).isNullOrEmpty()
    }
}
