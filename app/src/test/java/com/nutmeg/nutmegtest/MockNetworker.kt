package com.nutmeg.nutmegtest

import com.nutmeg.nutmegtest.api.NetworkError
import com.nutmeg.nutmegtest.api.NetworkingPath
import com.nutmeg.nutmegtest.api.Networker

class MockNetworker(
    private val dataToReturn: ByteArray? = null,
    private val modelsToReturn: Any? = null
) : Networker {

    override suspend fun getData(path: NetworkingPath): Result<ByteArray> =
        dataToReturn?.let { Result.success(it) } ?: Result.failure(NetworkError.Generic)

    @Suppress("UNCHECKED_CAST")
    override suspend fun <T> getModel(modelType: Class<T>, path: NetworkingPath): Result<T> =
        if (modelType.isInstance(modelsToReturn)) {
            Result.success(modelsToReturn as T)
        } else {
            Result.failure(NetworkError.Generic)
        }
}
