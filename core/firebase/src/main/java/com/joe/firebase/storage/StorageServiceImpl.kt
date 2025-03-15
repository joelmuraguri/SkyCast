package com.joe.firebase.storage

import com.google.firebase.firestore.FirebaseFirestore
import com.joe.firebase.account.AccountService
import com.joe.models.ForecastInfo
import com.joe.models.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StorageServiceImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: AccountService
): StorageService {

    override val locations: Flow<List<ForecastInfo>>
        get() = TODO("Not yet implemented")

    override suspend fun getLocation(name: String): ForecastInfo? {
        TODO("Not yet implemented")
    }

    override suspend fun save(location: ForecastInfo): String {
        TODO("Not yet implemented")
    }

    override suspend fun saveUserInfo(user: User) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteUserInfo() {
        TODO("Not yet implemented")
    }

    override suspend fun update(location: ForecastInfo) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(name: String) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllLocations() {
        TODO("Not yet implemented")
    }

    companion object {
        private val USERS_COLLECTION = "users"
        private const val LOCATIONS_COLLECTION = "locations"
    }
}