package com.example.nytime.data.entities

import android.os.Parcel
import android.os.Parcelable
import se.ansman.kotshi.JsonSerializable


@JsonSerializable
data class MediaMetadatum protected constructor(val `parcel`: Parcel) : Parcelable {
    var format: String? = null
    var height: Long? = null
    var url: String? = null
    var width: Long? = null

    init {
        format = parcel.readString()
        height = parcel.readValue(Long::class.java.classLoader) as? Long
        url = parcel.readString()
        width = parcel.readValue(Long::class.java.classLoader) as? Long
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(format)
        parcel.writeValue(height)
        parcel.writeString(url)
        parcel.writeValue(width)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MediaMetadatum> {
        override fun createFromParcel(parcel: Parcel): MediaMetadatum {
            return MediaMetadatum(parcel)
        }

        override fun newArray(size: Int): Array<MediaMetadatum?> {
            return arrayOfNulls(size)
        }
    }

}
