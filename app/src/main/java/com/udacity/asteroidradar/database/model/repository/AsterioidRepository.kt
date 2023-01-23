package com.udacity.asteroidradar.database.model.repository

import androidx.lifecycle.LiveData
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.network.NasaNetwork
import com.udacity.asteroidradar.database.entities.NeoWSEntity
import com.udacity.asteroidradar.database.entities.PictureOfDayEntity
import com.udacity.asteroidradar.database.model.database.AstreoidDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.await
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class AsterioidRepository(private val database: AstreoidDatabase) {
    val pictureOfDay: LiveData<PictureOfDayEntity> = database.pictureOfDayDao.loadPictureOfDay(
        getToday()
    )
    val asteroids: LiveData<List<NeoWSEntity>> = database.asteroidsDao.loadAsteroids(getToday(), getEndDay())

    suspend fun loadPictureOfDay(){
        if(pictureOfDay.value != null) return
        withContext(Dispatchers.IO) {
            val pictureOfDay = NasaNetwork.pictureOfDayCall.requestPictureOfDayNetwork(getToday(), BuildConfig.KEY_STRING).await()
            database.pictureOfDayDao.insertPictureOfDay(pictureOfDay)
        }
    }

    suspend fun loadAsteroidList(){
        withContext(Dispatchers.IO) {
            val asteroidsData:ArrayList<NeoWSEntity> = NasaNetwork.neoWsCall.requestNeoWsWithinOneWeek(
                getToday(), getEndDay(),BuildConfig.KEY_STRING).await()
            for(entity in asteroidsData) database.asteroidsDao.insertNeoWS(entity)
        }
    }

}

fun getToday(): String {
    var calendar = Calendar.getInstance()
    // nasa issue https://github.com/nasa/apod-api/issues/48
//    calendar.timeZone = TimeZone.getTimeZone("est")
    // to clear issue
    calendar.add(Calendar.DATE,-1)
    val c: Date = calendar.time

    val df = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())

    Timber.d("date = " + df.format(c))
    return df.format(c)
}

fun getEndDay(): String {
    var calendar = Calendar.getInstance()
    // nasa issue https://github.com/nasa/apod-api/issues/48
//    calendar.timeZone = TimeZone.getTimeZone("est")
    // to clear issue
    calendar.add(Calendar.DATE,6)
    val c: Date = calendar.time

    val df = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())

    Timber.d("date = " + df.format(c))
    return df.format(c)
}