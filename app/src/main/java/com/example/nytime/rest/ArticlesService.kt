package com.example.nytime.rest

import com.example.nytime.ArticlesClient
import com.example.nytime.BuildConfig
import com.example.nytime.data.entities.ArticlesResponse
import okhttp3.HttpUrl
import okhttp3.Request


suspend fun ArticlesClient.loadArticles(): ArticlesResponse {
    val urlBuilder = requestUrl(restUrl, "all-sections")
    urlBuilder.addQueryParameter("api-key", BuildConfig.API_KEY)


    val builder = Request.Builder().url(urlBuilder.build())

    val request = builder.get().build()
    val response = handleRequest(request)
    return handleResponse<ArticlesResponse>(response, ArticlesResponse::class.java)
}


internal fun requestUrl(baseUrl: HttpUrl, method: String): HttpUrl.Builder {
    return baseUrl.newBuilder()
            .addPathSegment("v2")
            .addPathSegment("mostviewed")
            .addPathSegment(method)
}