package com.udacity.asteroidradar.view.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.api.network.FetchState
import com.udacity.asteroidradar.database.model.Asteroid
import com.udacity.asteroidradar.database.model.PictureOfDay
import com.udacity.asteroidradar.database.model.database.getDatabase
import com.udacity.asteroidradar.database.model.repository.AsterioidRepository
import com.udacity.asteroidradar.database.model.repository.AsteroidsFilter
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class MainViewModel(application: Application, private val filter: MutableLiveData<AsteroidsFilter>) : AndroidViewModel(application) {

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
                is SocketTimeoutException -> _fetchState.value = FetchState.TIMEOUT
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

    fun reloadAsteroidList() = viewModelScope.launch(handler) { asterioidRepository.loadAsteroidList() }

    //Main Today's a Image
    val today = Transformations.map(asterioidRepository.pictureOfDay){
        PictureOfDay(it?.mediaType ?: "",it?.title ?: "",it?.url ?: "")
    }

    //Asteroid List
    val asteroids : LiveData<List<Asteroid>> =
        Transformations.switchMap(filter){ filter ->
        when(filter){
            AsteroidsFilter.WEEK -> asterioidRepository.asteroids
            AsteroidsFilter.TODAY -> asterioidRepository.asteroidsToday
            AsteroidsFilter.ALL -> asterioidRepository.asteroidsAllDay
        }
    }

    fun updateFilter(filter:AsteroidsFilter){
        this.filter.value = filter
    }
    /**
     * Factory for constructing DevByteViewModel with parameter
     */
    class Factory(val app: Application, private val filter: MutableLiveData<AsteroidsFilter>) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app, filter) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}