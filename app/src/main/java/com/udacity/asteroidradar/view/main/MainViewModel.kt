package com.udacity.asteroidradar.view.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.api.network.FetchState
import com.udacity.asteroidradar.database.model.Asteroid
import com.udacity.asteroidradar.database.model.PictureOfDay
import com.udacity.asteroidradar.database.model.database.getDatabase
import com.udacity.asteroidradar.database.model.repository.AsterioidRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber
import java.net.UnknownHostException

class MainViewModel(application: Application) : AndroidViewModel(application) {

    //Exception Control on the coroutine
    private var _fetchState : MutableLiveData<FetchState> = MutableLiveData(FetchState.NORMAL)
    var fetchState : LiveData<FetchState> = Transformations.map(_fetchState){
        it
    }

    val handler = CoroutineExceptionHandler {
            _, exception ->
        run {
            Timber.d("CoroutineExceptionHandler got ${exception.printStackTrace()}")
            when(exception){
                is UnknownHostException -> _fetchState.value = FetchState.INTERNET_DISCONNECT
                is HttpException -> _fetchState.value = FetchState.BAD_REQUEST
            }
        }
    }

    private val database = getDatabase(application)
    private val asterioidRepository : AsterioidRepository = AsterioidRepository(database)
    init {
        viewModelScope.launch(handler) {
            asterioidRepository.loadPictureOfDay()
            asterioidRepository.loadAsteroidList()
        }
    }

    //Main Today's a Image
    val today = Transformations.map(asterioidRepository.pictureOfDay){
        PictureOfDay(it?.mediaType ?: "",it?.title ?: "",it?.url ?: "")
    }

    //Asteroid List
    val asteroids : LiveData<List<Asteroid>> = Transformations.map(asterioidRepository.asteroids) {
        val list = ArrayList<Asteroid>()
        for(item in it){
            list.add(
                Asteroid(
                    item.id,
                    item.name,
                    item.date,
                    item.absoluteMagnitude,
                    item.estimatedDiameter,
                    item.relativeVelocity,
                    item.distanceFromEarth,
                    item.isPotentiallyHazardousAsteroid
                )
            )
        }
        list
    }

    /**
     * Factory for constructing DevByteViewModel with parameter
     */
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}