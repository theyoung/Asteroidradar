package com.udacity.asteroidradar.database.model.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.network.NasaNetwork
import com.udacity.asteroidradar.database.entities.*
import com.udacity.asteroidradar.database.model.Asteroid
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
    val asteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidsDao.loadAsteroids(getToday(), getEndDay())) {
            it?.asDomainModel()
        }

    val asteroidsToday: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidsDao.loadAsteroidsToday(getToday())){
            it?.asDomainModel()
        }

    val asteroidsAllDay: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidsDao.loadAsteroidsAll(getToday())){
            it?.asDomainModel()
        }

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

    suspend fun loadAsteroidById(id:Long) : LiveData<NeoWSEntity>{
        return withContext(Dispatchers.IO) {
            database.asteroidsDao.findAsteroidById(id)
        }
    }
}

fun getToday(): String {
    var calendar = Calendar.getInstance()
    // nasa issue https://github.com/nasa/apod-api/issues/48
//    calendar.timeZone = TimeZone.getTimeZone("est")
    // to clear issue
//    calendar.add(Calendar.DATE,-1)
    val c: Date = calendar.time

    val df = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT)

    Timber.d("date = " + df.format(c))
    return df.format(c)
}

fun getEndDay(): String {
    var calendar = Calendar.getInstance()
    // nasa issue https://github.com/nasa/apod-api/issues/48
//    calendar.timeZone = TimeZone.getTimeZone("est")
    // to clear issue
    calendar.add(Calendar.DATE,7)
    val c: Date = calendar.time

    val df = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT)

    Timber.d("date = " + df.format(c))
    return df.format(c)
}