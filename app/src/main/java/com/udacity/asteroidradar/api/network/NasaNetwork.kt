package com.udacity.asteroidradar.api.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object NasaNetwork {
    // Configure retrofit to parse JSON and use coroutines
    //kotlin annotation(reflection) support를 위한 적용
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.nasa.gov/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val pictureOfDayCall = retrofit.create(PictureOfDayNetwork::class.java)
}