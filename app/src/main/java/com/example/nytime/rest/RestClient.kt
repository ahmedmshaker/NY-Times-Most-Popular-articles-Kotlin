package com.example.nytime.rest

import com.example.nytime.ArticlesClient
import com.example.nytime.exceptions.ArticlesException
import com.squareup.moshi.JsonAdapter
import kotlinx.coroutines.experimental.CancellableContinuation
import okhttp3.*
import java.io.IOException
import java.lang.reflect.Type


internal fun <T> ArticlesClient.handleResponse(response: Response, type: Type): T {
    val url = response.priorResponse()?.request()?.url() ?: response.request().url()
    try {
        // Override nullability, if there is no adapter, moshi will throw...
        val adapter: JsonAdapter<T> = moshi.adapter(type)!!

        val source = response.body()?.source()
        kotlin.checkNotNull(source) { "Missing body" }

        return adapter.fromJson(source) ?: throw ArticlesException("Error parsing JSON message", url = url.toString())
    } catch (ex: Exception) {
        throw ex
    } finally {
        response.body()?.close()
    }
}

internal suspend fun ArticlesClient.handleRequest(
        request: Request,
        largeFile: Boolean = false,
        allowRedirects: Boolean = true
): Response = kotlinx.coroutines.experimental.suspendCancellableCoroutine { continuation ->
    val callback = object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            logger.debug {
                "Failed request: ${request.method()} - ${request.url()} - ${e.message}"
            }
            continuation.tryResumeWithException {
                ArticlesException("Network Error: ${e.message}", e, request.url().toString())
            }
        }

        override fun onResponse(call: Call, response: Response) {
            logger.debug {
                "Successful HTTP request: ${request.method()} - ${request.url()}: ${response.code()} ${response.message()}"
            }
            if (!response.isSuccessful) {
                continuation.tryResumeWithException {
                    java.lang.Exception()
                    //rocessCallbackError(moshi, request, response, logger, allowRedirects)
                }
            } else {
                continuation.tryToResume { response }
            }
        }
    }

    logger.debug {
        "Enqueueing: ${request.method()} - ${request.url()}"
    }

    val client = ensureClient(largeFile, allowRedirects)
    client.newCall(request).enqueue(callback)

    continuation.invokeOnCompletion {
        if (continuation.isCancelled) client.cancel(request.tag())
    }
}






private inline fun <T> CancellableContinuation<T>.tryResumeWithException(getter: () -> Exception) {
    isActive || return
    resumeWithException(getter())
}

private fun OkHttpClient.cancel(tag: Any) {
    dispatcher().queuedCalls().filter { tag == it.request().tag() }.forEach { it.cancel() }
    dispatcher().runningCalls().filter { tag == it.request().tag() }.forEach { it.cancel() }
}



internal fun ArticlesClient.ensureClient(largeFile: Boolean, allowRedirects: Boolean): OkHttpClient {
    return if (largeFile || !allowRedirects) {
        httpClient.newBuilder().apply {
            if (largeFile) {
                writeTimeout(90, java.util.concurrent.TimeUnit.SECONDS)
                readTimeout(90, java.util.concurrent.TimeUnit.SECONDS)
            }
            followRedirects(allowRedirects)
        }.build()
    } else {
        httpClient
    }
}


private inline fun <T> CancellableContinuation<T>.tryToResume(getter: () -> T) {
    isActive || return
    try {
        resume(getter())
    } catch (exception: Throwable) {
        resumeWithException(exception)
    }
}

