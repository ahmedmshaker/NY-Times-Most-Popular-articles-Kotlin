package com.example.nytime.data.entities

import android.os.Parcel
import android.os.Parcelable

import com.squareup.moshi.Json
import se.ansman.kotshi.JsonSerializable

@JsonSerializable
data class Medium protected constructor(val `parcel`: Parcel) : Parcelable {
    @Json(name ="approved_for_syndication")
    var approvedForSyndication: Long? = null
    var caption: String? = null
    var copyright: String? = null
    @Json(name ="media-metadata")
    var mediaMetadata: List<MediaMetadatum>? = null
    var subtype: String? = null
    var type: String? = null

    init {
        approvedForSyndication = parcel.readValue(Long::class.java.classLoader) as? Long
        caption = parcel.readString()
        copyright = parcel.readString()
        mediaMetadata = parcel.createTypedArrayList(MediaMetadatum)
        subtype = parcel.readString()
        type = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(approvedForSyndication)
        parcel.writeString(caption)
        parcel.writeString(copyright)
        parcel.writeTypedList(mediaMetadata)
        parcel.writeString(subtype)
        parcel.writeString(type)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Medium> {
        override fun createFromParcel(parcel: Parcel): Medium {
            return Medium(parcel)
        }

        override fun newArray(size: Int): Array<Medium?> {
            return arrayOfNulls(size)
        }
    }


}
