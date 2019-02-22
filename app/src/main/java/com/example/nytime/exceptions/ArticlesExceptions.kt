package com.example.nytime.exceptions

open class ArticlesException(message: String, cause: Throwable? = null, private val url: String? = null) : RuntimeException(message, cause) {
    override fun toString(): String {
        return "${super.toString()}(url='$url')"
    }
}
