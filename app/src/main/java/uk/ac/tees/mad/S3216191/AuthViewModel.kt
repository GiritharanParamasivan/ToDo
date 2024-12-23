package uk.ac.tees.mad.s3216191

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel : ViewModel() {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()


    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    val authState: StateFlow<AuthState> = _authState

    init {

        firebaseAuth.currentUser?.let {
            _authState.value = AuthState.Authenticated(it.uid)
        }
    }

   // For New User
    fun signup(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("Email or password cannot be blank")
            return
        }


        viewModelScope.launch {
            try {
                val user = firebaseAuth.createUserWithEmailAndPassword(email, password).await().user
                if (user != null) {
                    _authState.value = AuthState.Authenticated(user.uid)
                    Log.d("AuthViewModel", "Signup successful: ${user.uid}")
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Signup failed: ${e.message}")
                _authState.value = AuthState.Error(e.message ?: "Signup failed")
            }
        }
    }

    // Existing User
    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            Log.e("AuthViewModel", "Email or password cannot be blank")
            return
        }

        viewModelScope.launch {
            _authState.value = AuthState.Loading
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = task.result?.user
                        user?.let {
                            _authState.value = AuthState.Authenticated(it.uid)
                            Log.d("AuthViewModel", "Login successful: ${it.uid}")
                        }
                    } else {
                        Log.e("AuthViewModel", "Login failed: ${task.exception?.message}")
                        _authState.value =
                            AuthState.Error(task.exception?.message ?: "Unknown error")
                    }
                }
        }
    }

    // Sign out
    fun signout() {
        firebaseAuth.signOut()
        _authState.value = AuthState.Unauthenticated
        Log.d("AuthViewModel", "User signed out")
    }
}


sealed class AuthState {
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Authenticated(val userId: String) : AuthState()
    data class Error(val message: String) : AuthState()
}