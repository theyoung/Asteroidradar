package com.udacity.asteroidradar.model.repository

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.Transformations.map
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.network.NasaNetwork
import com.udacity.asteroidradar.api.network.PictureOfDayNetwork
import com.udacity.asteroidradar.database.entities.PictureOfDayEntity
import com.udacity.asteroidradar.model.database.AstreoidDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.await
import timber.log.Timber
import java.lang.Exception
import java.net.UnknownHostException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.coroutineContext

class AsterioidRepository(private val database: AstreoidDatabase) {
    val pictureOfDay: LiveData<PictureOfDayEntity> = database.pictureOfDayDao.loadPictureOfDay(getToday())

    suspend fun loadPictureOfDay(){
        if(pictureOfDay.value != null) return
        withContext(Dispatchers.IO) {
            val pictureOfDay = NasaNetwork.pictureOfDayCall.requestPictureOfDayNetwork(getToday(), BuildConfig.KEY_STRING).await()
            database.pictureOfDayDao.insertPictureOfDay(pictureOfDay)
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