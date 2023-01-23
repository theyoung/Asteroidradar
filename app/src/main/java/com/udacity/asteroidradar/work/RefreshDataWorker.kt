package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.model.database.getDatabase
import com.udacity.asteroidradar.database.model.repository.AsterioidRepository
import retrofit2.HttpException
import timber.log.Timber

class RefreshDataWorker(appContext:Context, params:WorkerParameters) : CoroutineWorker(appContext, params) {
    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }
    override suspend fun doWork(): Result {
        Timber.d("Worker Start")
        val database = getDatabase(applicationContext)
        val repository = AsterioidRepository(database)
        return try {
            repository.loadAsteroidList()
            Result.success()
        } catch (e:HttpException){
            Result.retry()
        }
        Timber.d("Worker End")
    }
}