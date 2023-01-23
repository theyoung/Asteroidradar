package com.udacity.asteroidradar.api.converter

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Converter
import java.io.IOException

object RequestBodyConverter : Converter<Object, RequestBody> {
    private val MEDIA_TYPE: MediaType = "text/plain; charset=UTF-8".toMediaType()

    @Throws(IOException::class)
    override fun convert(value: Object): RequestBody {
        return value.toString().toRequestBody(MEDIA_TYPE)
    }
}
