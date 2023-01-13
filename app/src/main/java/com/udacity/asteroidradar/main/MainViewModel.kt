package com.udacity.asteroidradar.main

import android.app.Application
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.lifecycle.*
import com.udacity.asteroidradar.api.network.FetchState
import com.udacity.asteroidradar.model.PictureOfDay
import com.udacity.asteroidradar.model.database.getDatabase
import com.udacity.asteroidradar.model.repository.AsterioidRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber
import java.net.UnknownHostException
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private var _fetchState : MutableLiveData<FetchState> = MutableLiveData(FetchState.NORMAL)
    var fetchState : LiveData<FetchState> = Transformations.map(_fetchState){
        it
    }

    val handler = CoroutineExceptionHandler {
            _, exception ->
        run {
            Timber.d("CoroutineExceptionHandler got $exception")
            when(exception){
                is UnknownHostException -> _fetchState.value = FetchState.INTERNET_DISCONNECT
                is HttpException -> _fetchState.value = FetchState.BAD_REQUEST
            }
        }
    }

    private val database = getDatabase(application)
    private val asterioidRepository :AsterioidRepository = AsterioidRepository(database)
    init {
        viewModelScope.launch(handler) {
            asterioidRepository.loadPictureOfDay()
        }
    }

    val today = asterioidRepository.pictureOfDay

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