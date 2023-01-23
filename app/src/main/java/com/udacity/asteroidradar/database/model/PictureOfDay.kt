package com.udacity.asteroidradar.database.model

import android.os.Parcelable
import androidx.room.Entity
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PictureOfDay(@Json(name = "media_type") val mediaType: String, val title: String,val url: String) :
    Parcelable