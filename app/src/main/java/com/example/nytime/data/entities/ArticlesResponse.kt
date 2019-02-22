package com.example.nytime.data.entities

import com.squareup.moshi.Json
import se.ansman.kotshi.JsonSerializable

@JsonSerializable
data class ArticlesResponse (

    @Json(name ="num_results")
    var numResults: Int,
    var status: String ,
    var copyright: String,

    @Json(name ="results")
    var articles: List<Article>
)
