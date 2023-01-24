package com.udacity.asteroidradar.api.request

import com.udacity.asteroidradar.database.entities.PictureOfDayEntity
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PictureOfDayRequest {
    @GET("planetary/apod")
    fun requestPictureOfDayNetwork(@Query("date")date:String, @Query("api_key") apiKey:String) : Call<PictureOfDayEntity>
}