package com.nutmeg.nutmegtest.api

import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

class DefaultNetworker(private val host: Host) : Networker {

    private val client = OkHttpClient()
    private val gson = Gson()

    override suspend fun getData(path: NetworkingPath): Result<ByteArray> =
        withContext(Dispatchers.IO) {
            try {
                val url = "https://${host.path}${path.path}"
                val request = Request.Builder().url(url).get().build()
                client.newCall(request).execute().use { response ->
                    val body = response.body?.bytes()
                    if (response.isSuccessful && body != null) {
                        Result.success(body)
                    } else {
                        Result.failure(NetworkError.Generic)
                    }
                }
            } catch (e: Exception) {
                Result.failure(NetworkError.Generic)
            }
        }

    override suspend fun <T> getModel(modelType: Class<T>, path: NetworkingPath): Result<T> {
        val dataResult = getData(path)
        val data = dataResult.getOrElse { return Result.failure(it) }

        return try {
            val model = gson.fromJson(String(data), modelType)
            Result.success(model)
        } catch (e: Exception) {
            Result.failure(NetworkError.Decoding)
        }
    }
}
