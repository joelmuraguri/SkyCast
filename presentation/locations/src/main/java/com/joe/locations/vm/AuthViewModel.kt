package com.joe.locations.vm

import android.content.Intent
import android.content.IntentSender
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joe.firebase.account.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState = _authState.asStateFlow()

    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated = _isAuthenticated.asStateFlow()



    fun signIn() {
        Log.d(AUTH_VIEWMODEL, "-----------> Start Google auth")
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                Log.d(AUTH_VIEWMODEL, "-----------> Calling signIn()")
                val signInIntentSender = authRepository.signIn()
                Log.d(AUTH_VIEWMODEL, "-----------> Sign-in intent created successfully")
                _authState.value = AuthState.SignInRequested(signInIntentSender)
            } catch (e: Exception) {
                Log.e(AUTH_VIEWMODEL, "-----------> Sign-in failed: ${e.message}")
                _authState.value = AuthState.Error(e.message ?: "Sign-in failed")
            }
        }
    }

    fun handleSignInResult(intent: Intent)  {
        Log.d(AUTH_VIEWMODEL, "-----------> Handling sign-in result")
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
//                if (intent.data == null) {
//                    Log.e(AUTH_VIEWMODEL, "-----------> Sign-in result data is null")
//                    _authState.value = AuthState.Error("Sign-in result data is null")
//                    return@launch
//                }

                val signInResult = authRepository.signInWithIntent(intent)
                Log.d(AUTH_VIEWMODEL, "-----------> Sign-in success: ${signInResult.data}")

                if (signInResult.data != null) {
                    _authState.value = AuthState.Success
                    _isAuthenticated.value = true
                    Log.d(AUTH_VIEWMODEL, "-----------> Authentication successful")
                } else {
                    _authState.value = AuthState.Error(signInResult.errorMessage ?: "Sign-in failed")
                    Log.e(AUTH_VIEWMODEL, "-----------> Sign-in failed: ${signInResult.errorMessage}")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Sign-in failed")
                Log.e(AUTH_VIEWMODEL, "-----------> Exception during sign-in: ${e.message}")
            }
        }
    }



    fun handleSignInResultT(result: ActivityResult) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val signInResult = authRepository.signInWithIntent(result.data ?: return@launch)
                if (signInResult.data != null) {
                    _authState.value = AuthState.Success
                    _isAuthenticated.value = true
                } else {
                    _authState.value =
                        AuthState.Error(signInResult.errorMessage ?: "Sign-in failed")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Sign-in failed")
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                authRepository.signOut()
                _authState.value = AuthState.Idle
                _isAuthenticated.value = false
                Log.d(AUTH_VIEWMODEL, "-----------> User signed out successfully")
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Sign-out failed")
                Log.e(AUTH_VIEWMODEL, "-----------> Sign-out failed: ${e.message}")
            }
        }
    }


    companion object{
        private val AUTH_VIEWMODEL = "AuthViewModel"
    }
}

sealed class AuthState {
    data object Idle : AuthState()
    data object Loading : AuthState()
    data class SignInRequested(val intentSender: IntentSender?) : AuthState()
    data object Success : AuthState()
    data class Error(val message: String) : AuthState()
}

data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null
)