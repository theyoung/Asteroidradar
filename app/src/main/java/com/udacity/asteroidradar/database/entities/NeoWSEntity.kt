package com.udacity.asteroidradar.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "neo_ws")
data class NeoWSEntity constructor(@PrimaryKey val id : Long,
                                   val name:String,
                                   val date:String,
                                   @ColumnInfo(name = "absolute_magnitude_h") val absoluteMagnitude:Double,
                                   @ColumnInfo(name = "estimated_diameter.kilometers.estimated_diameter_max") val estimatedDiameter:Double,
                                   @ColumnInfo(name = "close_approach_data[0].relative_velocity.kilometers_per_second") val relativeVelocity:Double,
                                   @ColumnInfo(name = "miss_distance.astronomical") val distanceFromEarth:Double,
                                   @ColumnInfo(name = "is_potentially_hazardous_asteroid") val isPotentiallyHazardousAsteroid:Boolean
                                   )
