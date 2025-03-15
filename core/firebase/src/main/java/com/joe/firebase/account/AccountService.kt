package com.joe.firebase.account

import android.content.Intent
import android.content.IntentSender
import com.joe.models.User
import kotlinx.coroutines.flow.Flow

interface AccountService {

    val currentUserId: String
    val hasUser: Boolean
    val currentUser: Flow<User>

    suspend fun googleSignIn()
    suspend fun signOut()
    suspend fun deleteAccount()
}

interface AuthRepository {
    suspend fun signIn(): IntentSender?
    suspend fun signInWithIntent(intent: Intent): SignInResult
    suspend fun signOut()
    fun getSignedInUser(): UserData?
}