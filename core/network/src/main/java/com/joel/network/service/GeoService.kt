package com.joel.network.service

import com.joel.network.models.GeoCodingResponse
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GeoService {

    @GET("search")
    suspend fun locationSearch(
        @Query("name") name : String,
        @Query("count") count : Int = 10,
    ) : ApiResponse<GeoCodingResponse>

}

/***
 * https://geocoding-api.open-meteo.com/v1/search?name=Berlin&count=10&language=en&format=json
 * */