package com.joe.supabase.auth

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.joe.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface SessionService {
    suspend fun saveUserData(user: User)
    fun readUserData() : Flow<User>
    suspend fun clearUserData()
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_session")

class SessionServiceImpl @Inject constructor(
    context : Context
) : SessionService {

    private object PreferencesKey {
        val userId = stringPreferencesKey(name = "user_id")
        val userName = stringPreferencesKey(name = "user_name")
        val userEmail = stringPreferencesKey(name = "user_email")
        val userProfileImage = stringPreferencesKey(name = "user_profile_image")
    }

    private val dataStore = context.dataStore


    override suspend fun saveUserData(user: User) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.userId] = user.uid
            preferences[PreferencesKey.userName] = user.name
            preferences[PreferencesKey.userEmail] = user.email
            preferences[PreferencesKey.userProfileImage] = user.profileUrl ?: ""
        }
    }

    override fun readUserData(): Flow<User> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val userId = preferences[PreferencesKey.userId] ?: ""
                val userName = preferences[PreferencesKey.userName] ?: "Unknown"
                val userEmail = preferences[PreferencesKey.userEmail] ?: ""
                val userProfileImage = preferences[PreferencesKey.userProfileImage] ?: ""
                User(uid = userId, email = userEmail, name = userName, profileUrl = userProfileImage)
            }
    }

    override suspend fun clearUserData() {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.userId] = ""
            preferences[PreferencesKey.userName] = ""
            preferences[PreferencesKey.userEmail] = ""
            preferences[PreferencesKey.userProfileImage] = ""
        }
    }
}