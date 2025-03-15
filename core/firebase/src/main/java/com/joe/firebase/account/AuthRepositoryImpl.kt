package com.joe.firebase.account

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.util.Log
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.joe.firebase.R
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException


class AuthRepositoryImpl @Inject constructor(
    private val context: Context,
    private val oneTapClient: SignInClient,
    private val auth: FirebaseAuth
) : AuthRepository {

    override suspend fun signIn(): IntentSender? {
        Log.d("AUTH_REPO", "-----------> Attempting to start sign-in")
        val result = try {
            Log.d("AUTH_REPO", "-----------> WEB CLIENT : ${context.getString(R.string.web_client_id)}")
            oneTapClient.beginSignIn(
                buildSignInRequest()
            ).await()
        } catch (e: Exception) {
            Log.e("AUTH_REPO", "------------> Error in sign-in: ${e.message}", e)

            e.printStackTrace()
            if (e is CancellationException) throw e
            null
        }
        Log.d("AUTH_REPO", "-----------> Sign-in result: $result")
        val intentSender = result?.pendingIntent?.intentSender
        if (intentSender == null) {
            Log.e("AUTH_REPO", "------------> Error: intentSender is null")
        } else {
            Log.d("AUTH_REPO", "------------------> IntentSender is valid, proceeding with launch")
        }
        return intentSender
    }

    override suspend fun signInWithIntent(intent: Intent): SignInResult {
        Log.d("AuthRepository", "-----------> Processing sign-in intent")
        return try {
            val credential = oneTapClient.getSignInCredentialFromIntent(intent)
            Log.d("AuthRepository", "-----------> Credential received: ${credential.id}")

            val googleIdToken = credential.googleIdToken
            if (googleIdToken == null) {
                Log.e("AuthRepository", "-----------> Google ID Token is null")
                return SignInResult(data = null, errorMessage = "Google ID Token is null")
            }

            val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
            val user = auth.signInWithCredential(googleCredentials).await().user

            if (user != null) {
                Log.d("AuthRepository", "-----------> User signed in: ${user.displayName}")
            } else {
                Log.e("AuthRepository", "-----------> Firebase user is null")
            }

            SignInResult(
                data = user?.run {
                    UserData(
                        userId = uid,
                        username = displayName,
                        profilePictureUrl = photoUrl?.toString()
                    )
                },
                errorMessage = null
            )
        } catch (e: Exception) {
            Log.e("AuthRepository", "-----------> Exception in signInWithIntent: ${e.message}")
            e.printStackTrace()
            if (e is CancellationException) throw e
            SignInResult(data = null, errorMessage = e.message)
        }
    }


    override suspend fun signOut() {
        try {
            oneTapClient.signOut().await()
            auth.signOut()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
        }
    }

    override fun getSignedInUser(): UserData? = auth.currentUser?.run {
        UserData(
            userId = uid,
            username = displayName,
            profilePictureUrl = photoUrl?.toString()
        )
    }

    private fun buildSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false) // Change this to true for better results
                    .setServerClientId(context.getString(R.string.web_client_id))
                    .setNonce(null)
                    .build()
            )
            .setAutoSelectEnabled(false) // Set to false to allow user selection
            .build()
    }

}