package com.udacity.asteroidradar.database.model.database

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
    @Query("select * from neo_ws where date between :start and :end order by date")
    fun loadAsteroids(start : String, end : String) : LiveData<List<NeoWSEntity>>

    @Query("select * from neo_ws where date = :start order by date")
    fun loadAsteroidsToday(start : String) : LiveData<List<NeoWSEntity>>

    @Query("select * from neo_ws order by date")
    fun loadAsteroidsAll() : LiveData<List<NeoWSEntity>>


    @Query("select * from neo_ws where id = :id")
    fun findAsteroidById(id:Long) : LiveData<NeoWSEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNeoWS(neoWSEntity: NeoWSEntity)
}

@Database(entities = [PictureOfDayEntity::class, NeoWSEntity::class], version = 1)
abstract class AstreoidDatabase : RoomDatabase() {
    abstract val pictureOfDayDao: PictureOfDayDao
    abstract val asteroidsDao: AsteroidsDao
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