package com.joel.network.models

import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.mappers.ApiErrorModelMapper
import com.skydoves.sandwich.message
import com.skydoves.sandwich.retrofit.statusCode

data class ErrorResponse(
    val code: Int,
    val message: String?,
)

object ErrorResponseMapper : ApiErrorModelMapper<ErrorResponse> {

    override fun map(apiErrorResponse: ApiResponse.Failure.Error): ErrorResponse {
        return ErrorResponse(apiErrorResponse.statusCode.code, apiErrorResponse.message())
    }
}