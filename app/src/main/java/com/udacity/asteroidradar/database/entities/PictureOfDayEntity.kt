package com.udacity.asteroidradar.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.FromJson
import com.squareup.moshi.Json
import com.udacity.asteroidradar.model.PictureOfDay

//한번 만들어진 Entity는 수정 불가
@Entity(tableName = "picture_of_day")
data class PictureOfDayEntity constructor(
                              @PrimaryKey val date:String,
                              val explanation:String?,
                              val hdurl:String?,
                              @ColumnInfo(name = "media_type") @Json(name = "media_type") val mediaType:String?,
                              @ColumnInfo(name = "service_version") @Json(name = "service_version") val serviceVersion:String?,
                              val title:String?,
                              val url:String?)
