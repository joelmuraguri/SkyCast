package com.joe.firebase.account

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.joe.firebase.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import java.security.MessageDigest
import java.util.UUID
import kotlin.coroutines.cancellation.CancellationException

class AuthCredentialManager(
    private val context: Context
) {

    private val auth = Firebase.auth


    private fun createNonce() : String{
        val rawNonce = UUID.randomUUID().toString()
        val bytes = rawNonce.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold(""){ str, it ->
            str + "%02x".format(it)
        }
    }

    suspend fun signInWithCredentialManager(): Flow<AuthResponse> {
        return flow {
            try {
                val googleIdOption = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(context.getString(R.string.web_client_id))
                    .setAutoSelectEnabled(false)
                    .setNonce(createNonce()) // Ensure correct nonce
                    .build()

                val credentialManager = CredentialManager.create(context)
                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

                val credentialResponse = credentialManager.getCredential(context, request)
                val googleIdTokenCredential = credentialResponse.credential as GoogleIdTokenCredential
                val idToken = googleIdTokenCredential.idToken

                // Authenticate with Firebase or Backend
                val firebaseAuth = FirebaseAuth.getInstance()
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                val authResult = firebaseAuth.signInWithCredential(credential).await()

                emit(AuthResponse.Success(authResult.user!!))
            } catch (e: Exception) {
                emit(AuthResponse.Error(e.localizedMessage ?: "Unknown error"))
            }
        }.flowOn(Dispatchers.IO)
    }


    fun signInWithGoogle(): Flow<AuthResponse> = flow {
        Log.d("AuthCredentialManager", "-----------------> Starting Google Sign-In flow")

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(context.getString(R.string.web_client_id))
            .setAutoSelectEnabled(false)
            .setNonce(createNonce()) // Ensure correct nonce
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        try {
            val credentialManager = CredentialManager.create(context)
            val result = credentialManager.getCredential(context, request) // Suspending call

            val credentials = result.credential
            if (credentials is CustomCredential && credentials.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credentials.data)

                Log.d("GoogleSignIn", "Google ID Token: ${googleIdTokenCredential.idToken}")

                val firebaseCredential = GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)

                auth.signInWithCredential(firebaseCredential).await() // No need to store authResult
                emit(AuthResponse.Success(auth.currentUser!!))
            } else {
                emit(AuthResponse.Error("Invalid credential type"))
            }
        } catch (e: Exception) {
            Log.e("AuthCredentialManager", "-----------------> Exception during sign-in: ${e.message}", e)
            emit(AuthResponse.Error(message = e.message ?: "Unknown error"))
        }
    }



//    fun signInWithGoogle() : Flow<AuthResponse> = callbackFlow{
//        val googleIdOption = GetGoogleIdOption.Builder()
//            .setFilterByAuthorizedAccounts(false)
//            .setServerClientId(context.getString(R.string.web_client_id))
//            .setAutoSelectEnabled(false)
//            .setNonce(createNonce())
//            .build()
//
//        val request = GetCredentialRequest.Builder()
//            .addCredentialOption(googleIdOption)
//            .build()
//
//        try {
//            val credentialManager = CredentialManager.create(context)
//            val result = credentialManager.getCredential(context, request)
//
//            val credentials = result.credential
//            if (credentials is CustomCredential){
//                if (credentials.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL){
//                    try {
//                        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credentials.data)
//
//                        Log.d("GoogleSignIn", "---------------------> Google ID Token: ${googleIdTokenCredential.idToken}")
//
//                        val firebaseCredential = GoogleAuthProvider.getCredential(
//                            googleIdTokenCredential.idToken, null
//                        )
//
//                        auth.signInWithCredential(firebaseCredential)
//                            .addOnCompleteListener {
//                                if (it.isSuccessful){
//                                    trySend(AuthResponse.Success)
//                                }else {
//                                    Log.e("AuthError", "-----------------------> Sign-in failed", it.exception)
//                                    trySend(AuthResponse.Error(message = it.exception?.message ?: ""))
//                                }
//                            }
//
//                    } catch (e : GoogleIdTokenParsingException){
//                        Log.e("GoogleSignIn", "-------------------------> GoogleIdTokenParsingException: ${e.message}")
//                        trySend(AuthResponse.Error(message = e.message ?: ""))
//                    }
//
//                }
//            }
//
//        } catch (e : Exception){
//            Log.e("GoogleSignIn", "-----------------------> Exception: ${e.message}")
//            trySend(AuthResponse.Error(message = e.message ?: ""))
//        }
//        awaitClose()
//    }

    fun saveUserToFirestore(user: FirebaseUser = Firebase.auth.currentUser!!) {
        val userDoc = Firebase.firestore.collection("users").document(user.uid)

        val userData = mapOf(
            "displayName" to user.displayName,
            "email" to user.email,
            "profileUrl" to user.photoUrl.toString(),
            "userId" to user.uid
        )
        userDoc.set(userData)
            .addOnSuccessListener { Log.d("Firestore", "-----------------> User saved successfully") }
            .addOnFailureListener { Log.e("Firestore", "---------------------> Error saving user", it) }
    }


}

class GoogleAuthUiClient(
    private val context: Context,
    private val oneTapClient: SignInClient
) {
    private val auth = Firebase.auth

    suspend fun signIn(): IntentSender? {
        val result = try {
            oneTapClient.beginSignIn(
                buildSignInRequest()
            ).await()
        } catch(e: Exception) {
            e.printStackTrace()
            if(e is CancellationException) throw e
            null
        }
        return result?.pendingIntent?.intentSender
    }

    suspend fun signInWithIntent(intent: Intent): SignInResult {
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
        return try {
            val user = auth.signInWithCredential(googleCredentials).await().user
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
        } catch(e: Exception) {
            e.printStackTrace()
            if(e is CancellationException) throw e
            SignInResult(
                data = null,
                errorMessage = e.message
            )
        }
    }

    suspend fun signOut() {
        try {
            oneTapClient.signOut().await()
            auth.signOut()
        } catch(e: Exception) {
            e.printStackTrace()
            if(e is CancellationException) throw e
        }
    }

    fun getSignedInUser(): UserData? = auth.currentUser?.run {
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
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(context.getString(R.string.web_client_id))
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }
}

interface AuthResponse{
    data class Error(val message : String) : AuthResponse
    data class Success(val user : FirebaseUser) : AuthResponse
}

data class SignInResult(
    val data: UserData?,
    val errorMessage: String?
)

data class UserData(
    val userId: String,
    val username: String?,
    val profilePictureUrl: String?
)

