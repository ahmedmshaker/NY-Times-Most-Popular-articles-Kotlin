package com.example.nytime.data.entities

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json

import se.ansman.kotshi.JsonSerializable

@JsonSerializable
class Article protected constructor(parcel: Parcel) : Parcelable {
    var abstract: String? = null
    @Json(name ="adx_keywords")
    var adxKeywords: String? = null
    @Json(name ="asset_id")
    var assetId: Long? = null
    var byline: String? = null
    var column: Any? = null
    @Json(name ="des_facet")
    var desFacet: Any? = null
        private set
    @Json(name ="geo_facet")
    var geoFacet: Any? = null
        private set
    var id: Long? = null
    var media: List<Medium>? = null
    @Json(name ="org_facet")
    var orgFacet: Any? = null
        private set
    @Json(name ="per_facet")
    var perFacet: Any? = null
        private set
    @Json(name ="published_date")
    var publishedDate: String? = null

    var section: String? = null
    var source: String? = null
    var title: String? = null
    var type: String? = null
    var url: String? = null
    var views: Long? = null

    init {
        abstract = parcel.readString()
        adxKeywords = parcel.readString()
        assetId = parcel.readValue(Long::class.java.classLoader) as? Long
        byline = parcel.readString()
        id = parcel.readValue(Long::class.java.classLoader) as? Long
        media = parcel.createTypedArrayList(Medium.CREATOR)
        publishedDate = parcel.readString()
        section = parcel.readString()
        source = parcel.readString()
        title = parcel.readString()
        type = parcel.readString()
        url = parcel.readString()
        views = parcel.readValue(Long::class.java.classLoader) as? Long
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(abstract)
        parcel.writeString(adxKeywords)
        parcel.writeValue(assetId)
        parcel.writeString(byline)
        parcel.writeValue(id)
        parcel.writeTypedList(media)
        parcel.writeString(publishedDate)
        parcel.writeString(section)
        parcel.writeString(source)
        parcel.writeString(title)
        parcel.writeString(type)
        parcel.writeString(url)
        parcel.writeValue(views)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Article> {
        override fun createFromParcel(parcel: Parcel): Article {
            return Article(parcel)
        }

        override fun newArray(size: Int): Array<Article?> {
            return arrayOfNulls(size)
        }
    }


}
