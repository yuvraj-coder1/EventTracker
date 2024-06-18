package com.example.eventtracker.ui.signIn

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.eventtracker.model.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) :ViewModel() {
    private val _uiState = MutableStateFlow(SignInUiState())
    val uiState:StateFlow<SignInUiState> = _uiState.asStateFlow()
    var inProcess by mutableStateOf(false)

    var currentUser = auth.currentUser
    fun updateEmail(email:String){
        _uiState.value = _uiState.value.copy(email = email)
    }
    fun updatePassword(password:String){
        _uiState.value = _uiState.value.copy(password = password)
    }
    fun updateConfirmPassword(confirmPassword:String){
        _uiState.value = _uiState.value.copy(confirmPassword = confirmPassword)
    }
    fun updateName(name:String){
        _uiState.value = _uiState.value.copy(username = name)
    }
    fun updateIsSignIn(isSignIn:Boolean){
        _uiState.value = _uiState.value.copy(isSignIn = isSignIn)
    }
    fun updateCollegeId(collegeId:String){
        _uiState.value = _uiState.value.copy(collegeId = collegeId)
    }
    fun signIn(
        onSuccess:()->Unit,
        onFailure:()->Unit
    ){
        inProcess = true
        auth.signInWithEmailAndPassword(uiState.value.email,uiState.value.password)
            .addOnSuccessListener {
                currentUser = auth.currentUser
                inProcess = false

                onSuccess()
            }
            .addOnFailureListener{
                inProcess = false
                onFailure()
            }
    }
    fun signUp(
        onSuccess:()->Unit,
        onFailure:()->Unit
    ){
        if(uiState.value.password != uiState.value.confirmPassword)
            return
        auth.createUserWithEmailAndPassword(uiState.value.email,uiState.value.password)
            .addOnSuccessListener {result ->
                val user: UserData? = result.user?.uid?.let {
                    UserData(
                        name = uiState.value.username,
                        email = uiState.value.email,
                        collegeId = uiState.value.collegeId,
                        password = uiState.value.password,
                        id = it,
                    )
                }
                if (user != null) {
                    db.collection("users").document(user.id).set(user)
                        .addOnSuccessListener {
                            onSuccess()
                            Log.d("Sign", "onSignUp: Success")
                            _uiState.update { it.copy(isSignIn = true) }
                        }
                        .addOnFailureListener {
                            onFailure()
                            Log.d("Sign", "onSignUp: ${it.message}")
                        }
                }
            }
            .addOnFailureListener {
                onFailure()
                Log.d("Sign", "onSignUp: ${it.message}")
            }
    }
    fun checkIfLoggedIn():Boolean {
        return currentUser != null
    }

    fun signOut(){
        auth.signOut()
        currentUser = null
    }
}
