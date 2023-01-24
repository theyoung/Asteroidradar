package com.udacity.asteroidradar.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.udacity.asteroidradar.database.model.Asteroid

@Entity(tableName = "neo_ws")
data class NeoWSEntity constructor(@PrimaryKey val id : Long,
                                   val name:String,
                                   val date:String,
                                   @ColumnInfo(name = "absolute_magnitude_h") val absoluteMagnitude:Double,
                                   @ColumnInfo(name = "estimated_diameter") val estimatedDiameter:Double,
                                   @ColumnInfo(name = "kilometers_per_second") val relativeVelocity:Double,
                                   @ColumnInfo(name = "miss_distance") val distanceFromEarth:Double,
                                   @ColumnInfo(name = "is_potentially_hazardous_asteroid") val isPotentiallyHazardousAsteroid:Boolean
                                   )

fun List<NeoWSEntity>.asDomainModel():List<Asteroid> {
    return map {
        Asteroid(
            it.id,
            it.name,
            it.date,
            it.absoluteMagnitude,
            it.estimatedDiameter,
            it.relativeVelocity,
            it.distanceFromEarth,
            it.isPotentiallyHazardousAsteroid
        )
    }
}