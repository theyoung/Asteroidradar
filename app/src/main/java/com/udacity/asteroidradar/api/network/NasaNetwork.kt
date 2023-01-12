package com.udacity.asteroidradar.api.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import java.lang.reflect.Type

object NasaNetwork {
    // Configure retrofit to parse JSON and use coroutines
    //kotlin annotation(reflection) support를 위한 적용
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    //logging 처리를 위한 http3 client 재 정의
    //TODO 프로젝트 완료후 삭제 처리
    val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    val client = OkHttpClient.Builder().addInterceptor(logging).build()


    private val retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl("https://api.nasa.gov/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(object : CallAdapter.Factory() {
            override fun get(
                returnType: Type,
                annotations: Array<out Annotation>,
                retrofit: Retrofit
            ): CallAdapter<*, *>? {
                return null
            }

        })
        .build()

    val pictureOfDayCall = retrofit.create(PictureOfDayNetwork::class.java)
}