package com.udacity.asteroidradar.api.request

import com.udacity.asteroidradar.database.entities.NeoWSEntity
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NeoWSRequest {
    @GET("neo/rest/v1/feed")
    fun requestNeoWsWithinOneWeek(@Query("start_date") today:String,@Query("end_date") endDay:String, @Query("api_key") apiKey:String) : Call<ArrayList<NeoWSEntity>>
}