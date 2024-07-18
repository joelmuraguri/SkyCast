package com.joel.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.joel.database.entity.FavouritePlace
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavourite(favouritePlace: FavouritePlace)

    @Query("SELECT * FROM favourite_place")
    fun getFavourites(locationName: String): List<Flow<FavouritePlace>>

    @Query("DELETE FROM favourite_place WHERE locationName = :locationName")
    suspend fun deleteFavourites(locationName: String)
}
