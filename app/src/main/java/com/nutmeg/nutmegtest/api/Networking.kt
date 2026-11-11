package com.nutmeg.nutmegtest.api

enum class Host(val path: String) {
    JSON_API("jsonplaceholder.typicode.com"),
    PICSUM("picsum.photos")
}

sealed class NetworkingPath(val path: String) {
    object Posts : NetworkingPath("/posts")
    object Comments : NetworkingPath("/comments")
    object Users : NetworkingPath("/users")
    data class Picture(val id: Int) : NetworkingPath("/id/$id/3000")
}

sealed class NetworkError : Exception() {
    object Generic : NetworkError()
    object Decoding : NetworkError()
}

interface Networker {
    suspend fun getData(path: NetworkingPath): Result<ByteArray>
    suspend fun <T> getModel(modelType: Class<T>, path: NetworkingPath): Result<T>
}
