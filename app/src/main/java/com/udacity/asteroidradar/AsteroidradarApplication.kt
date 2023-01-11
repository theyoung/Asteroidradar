package com.udacity.asteroidradar

import android.app.Application
import android.util.Log
import timber.log.Timber


class AsteroidradarApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        //Timber logger 연결
        //https://github.com/JakeWharton/timber
        Timber.plant(Timber.DebugTree())
    }


}
