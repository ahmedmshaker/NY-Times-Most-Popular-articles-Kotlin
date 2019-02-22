package com.example.nytime


import com.example.nytime.util.CalendarISO8601Converter
import com.example.nytime.util.Logger
import com.example.nytime.util.PlatformLogger
import com.example.nytime.util.ifNull
import com.example.nytime.data.entities.ISO8601Date
import com.example.nytime.data.entities.TimestampAdapter
import com.squareup.moshi.Moshi
import kotlinx.coroutines.experimental.channels.Channel
import okhttp3.HttpUrl
import okhttp3.MediaType
import okhttp3.OkHttpClient
import java.security.InvalidParameterException

class ArticlesClient private constructor(
    internal val httpClient: OkHttpClient,
    baseUrl: String,
    internal val logger: Logger
) {
    internal val moshi: Moshi = Moshi.Builder()
//        .add(FallbackSealedClassJsonAdapter.ADAPTER_FACTORY)
//        .add(RestResult.JsonAdapterFactory())
//        .add(RestMultiResult.JsonAdapterFactory())
//        .add(SettingsAdapter())
//        .add(AttachmentAdapterFactory(logger))
//        .add(RoomListAdapterFactory(logger))
//        .add(MetaJsonAdapter.ADAPTER_FACTORY)
        .add(java.lang.Long::class.java, ISO8601Date::class.java, TimestampAdapter(CalendarISO8601Converter()))
        .add(Long::class.java, ISO8601Date::class.java, TimestampAdapter(CalendarISO8601Converter()))
        // XXX - MAKE SURE TO KEEP CommonJsonAdapterFactory and CoreJsonAdapterFactory as the latest Adapters...
        .build()

    internal lateinit var restUrl: HttpUrl
    val url: String
    val typingStatusChannel = Channel<Pair<String, Boolean>>()

    init {
        url = sanitizeUrl(baseUrl)

        HttpUrl.parse(url)?.let {
            restUrl = it
        }.ifNull {
            throw InvalidParameterException("You must pass a valid HTTP or HTTPS URL")
        }


    }

    private fun sanitizeUrl(baseUrl: String): String {
        var url = baseUrl.trim()
        while (url.endsWith('/')) {
            url = url.dropLast(1)
        }
        return url
    }

    private constructor(builder: Builder) : this(
        builder.httpClient,
        builder.restUrl,
        Logger(builder.platformLogger, builder.restUrl))

    companion object {
        val CONTENT_TYPE_JSON = MediaType.parse("application/json; charset=utf-8")

        fun create(init: Builder.() -> Unit) = Builder(init).build()
    }

    class Builder private constructor() {

        constructor(init: Builder.() -> Unit) : this() {
            init()
        }

        lateinit var httpClient: OkHttpClient
        lateinit var restUrl: String
        lateinit var platformLogger: PlatformLogger

        fun httpClient(init: Builder.() -> OkHttpClient) = apply { httpClient = init() }

        fun restUrl(init: Builder.() -> String) = apply { restUrl = init() }


        fun platformLogger(init: Builder.() -> PlatformLogger) = apply { platformLogger = init() }

        fun build() = ArticlesClient(this)
    }


}
