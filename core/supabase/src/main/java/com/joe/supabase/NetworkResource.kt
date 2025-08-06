package com.joe.supabase

sealed class NetworkResource<out T> {
    data class Success<out T>(val data: T) : NetworkResource<T>()
    data class Error(
        val message: String,
        val throwable: Throwable? = null
    ) : NetworkResource<Nothing>()
    data object Loading : NetworkResource<Nothing>()
}

suspend fun <T> safeSupabaseCall(
    block: suspend () -> T
): NetworkResource<T> {
    return try {
        val result = block()
        NetworkResource.Success(result)
    } catch (e: io.github.jan.supabase.exceptions.RestException) {
        NetworkResource.Error("Supabase error: ${e.message}", e)
    } catch (e: kotlinx.serialization.SerializationException) {
        NetworkResource.Error("Serialization error", e)
    } catch (e: java.net.UnknownHostException) {
        NetworkResource.Error("No internet connection", e)
    } catch (e: Exception) {
        NetworkResource.Error("Unexpected error: ${e.localizedMessage}", e)
    }
}
