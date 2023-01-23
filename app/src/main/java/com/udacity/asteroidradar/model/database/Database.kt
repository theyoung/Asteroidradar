package com.udacity.asteroidradar.model.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.database.entities.NeoWSEntity
import com.udacity.asteroidradar.database.entities.PictureOfDayEntity

@Dao
interface PictureOfDayDao {
    @Query("select * from picture_of_day where date = :date")
    fun loadPictureOfDay(date : String) : LiveData<PictureOfDayEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPictureOfDay(pictureOfDayEntity: PictureOfDayEntity)
}

@Dao
interface AsteroidsDao {
    @Query("select * from picture_of_day where date = :date")
    fun loadPictureOfDay(date : String) : LiveData<PictureOfDayEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPictureOfDay(pictureOfDayEntity: PictureOfDayEntity)
}

@Database(entities = [PictureOfDayEntity::class, NeoWSEntity::class], version = 1)
abstract class AstreoidDatabase : RoomDatabase() {
    abstract val pictureOfDayDao:PictureOfDayDao
    abstract val asteroidsDao:AsteroidsDao
}

private lateinit var INSTANCE : AstreoidDatabase

fun getDatabase(context: Context) : AstreoidDatabase {
    synchronized(AstreoidDatabase::class.java){
        if(!::INSTANCE.isInitialized){
            INSTANCE = Room.databaseBuilder(context.applicationContext, AstreoidDatabase::class.java, "astreoid").build()
        }
    }

    return INSTANCE
}