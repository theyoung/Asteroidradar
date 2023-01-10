package com.udacity.asteroidradar.model.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.Transformations.map
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.api.network.NasaNetwork
import com.udacity.asteroidradar.api.network.PictureOfDayNetwork
import com.udacity.asteroidradar.database.entities.PictureOfDayEntity
import com.udacity.asteroidradar.model.database.AstreoidDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.await
import java.text.SimpleDateFormat
import java.util.*

class AsterioidRepository(private val database: AstreoidDatabase) {
    val pictureOfDay: LiveData<PictureOfDayEntity> = database.pictureOfDayDao.loadPictureOfDay(getToday())

    suspend fun loadPictureOfDay(){
        withContext(Dispatchers.IO) {
            val pictureOfDay = NasaNetwork.pictureOfDayCall.requestPictureOfDayNetwork(BuildConfig.KEY_STRING).await()
            database.pictureOfDayDao.insertPictureOfDay(pictureOfDay)
        }
    }
}

fun getToday(): String {
    val c: Date = Calendar.getInstance().getTime()
    val df = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return df.format(c)
}