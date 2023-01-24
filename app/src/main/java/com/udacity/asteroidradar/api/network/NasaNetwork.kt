package com.udacity.asteroidradar.api.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.api.converter.CustomConverterFactory
import com.udacity.asteroidradar.api.request.NeoWSRequest
import com.udacity.asteroidradar.api.request.PictureOfDayRequest
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object NasaNetwork {
    // Configure retrofit to parse JSON and use coroutines
    //kotlin annotation(reflection) support를 위한 적용
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    //logging 처리를 위한 http3 client 재 정의
    //TODO 프로젝트 완료후 삭제 처리
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder().addInterceptor(logging)
//                        .connectTimeout(5, TimeUnit.SECONDS)
//                        .writeTimeout(5, TimeUnit.SECONDS)
//                        .readTimeout(10, TimeUnit.SECONDS)
//                        .callTimeout(10, TimeUnit.SECONDS)
                        .build()


    private val retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl("https://api.nasa.gov/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    private val retrofit2 = Retrofit.Builder()
        .client(client)
        .baseUrl("https://api.nasa.gov/")
        .addConverterFactory(CustomConverterFactory)
        .build()

    val pictureOfDayCall = retrofit.create(PictureOfDayRequest::class.java)
    val neoWsCall = retrofit2.create(NeoWSRequest::class.java)
}