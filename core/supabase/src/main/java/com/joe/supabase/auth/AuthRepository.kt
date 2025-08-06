package com.joe.supabase.auth

import android.accounts.AuthenticatorException
import android.app.Activity
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.joe.models.User
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.IDToken
import io.github.jan.supabase.exceptions.RestException
import io.ktor.http.parsing.ParseException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive
import javax.inject.Inject
import com.joe.supabase.R


sealed interface AuthResponse {
    data class Error(val message : String?) : AuthResponse
    data object Success : AuthResponse
}

interface AuthService {
    val currentUserId: String
    val hasUser: Boolean
    val currentUser: Flow<User>
    fun isUserAuthenticated(): Boolean
    suspend fun googleSignIn(activity: Activity) : Flow<AuthResponse>
    suspend fun signOut() : Flow<AuthResponse>
}

class AuthServiceImpl @Inject constructor(
    private val supabase : SupabaseClient,
    private val sessionService: SessionService
) : AuthService {


    override val currentUserId: String
        get() = supabase.auth.currentSessionOrNull()?.user?.id ?: ""

    override val hasUser: Boolean
        get() = supabase.auth.currentSessionOrNull()?.user != null

    override val currentUser: Flow<User> = flow {
        val session = supabase.auth.currentSessionOrNull()
        val userData = session?.user

        if (userData != null) {

            val name = (userData.userMetadata?.get("full_name")?.jsonPrimitive?.contentOrNull) ?: "Unknown"
            val picture = (userData.userMetadata?.get("picture")?.jsonPrimitive?.contentOrNull) ?: ""

            val user = User(
                uid = userData.id,
                email = userData.email ?: "",
                name = name,
                profileUrl = picture
            )
            sessionService.saveUserData(user)
            emit(user)
        } else {
            emit(User(uid = "", email = "", name = ""))
        }
    }.flowOn(Dispatchers.IO)

    override fun isUserAuthenticated(): Boolean {
        return hasUser
    }


    override suspend fun googleSignIn(activity: Activity): Flow<AuthResponse> = flow{
        val googleIdOption : GetSignInWithGoogleOption = GetSignInWithGoogleOption
            .Builder(activity.getString(R.string.web_client_id))
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        val credentialManager = CredentialManager.create(activity)

        try {
            val result = credentialManager.getCredential(
                context = activity,
                request = request
            )

            val googleIdTokenCredential = GoogleIdTokenCredential
                .createFrom(result.credential.data)

            val googleIdToken = googleIdTokenCredential.idToken
            Log.d("GO0GLE ID :" , "--> $googleIdToken")

            supabase.auth.signInWith(IDToken) {
                idToken = googleIdToken
                provider = Google
            }

            currentUser.collect { user ->
                sessionService.saveUserData(user)
            }

            emit(AuthResponse.Success)

        } catch (e: Exception) {
            Log.e("google", e.localizedMessage)
            emit(AuthResponse.Error(e.localizedMessage))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun signOut(): Flow<AuthResponse> = flow{
        try {
            supabase.auth.signOut()
            emit(AuthResponse.Success)
        } catch (e: ParseException) {
            emit(AuthResponse.Error(e.localizedMessage))
        }catch (e: RestException) {
            emit(AuthResponse.Error(e.localizedMessage))
        }catch (e: AuthenticatorException) {
            emit(AuthResponse.Error(e.localizedMessage))
        }catch (e: Exception) {
            emit(AuthResponse.Error(e.localizedMessage))
        }
    }.flowOn(Dispatchers.IO)

}