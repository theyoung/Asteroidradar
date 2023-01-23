package com.udacity.asteroidradar.api.converter

import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.entities.NeoWSEntity
import com.udacity.asteroidradar.model.Asteroid
import okhttp3.ResponseBody
import okio.*
import okio.ByteString.Companion.decodeHex
import org.json.JSONObject
import retrofit2.Converter
import timber.log.Timber
import java.io.BufferedReader
import java.nio.ByteBuffer
import java.util.*
import kotlin.collections.ArrayList


object StringResponseBodyConverter : Converter<ResponseBody, ArrayList<NeoWSEntity>> {
    override fun convert(response: ResponseBody): ArrayList<NeoWSEntity>? {
        val responseBodySource: BufferedSource = response.source()
        val scanner = Scanner(responseBodySource.inputStream())
        val sb = StringBuffer()
        while(scanner.hasNext()){
            sb.append(scanner.nextLine())
        }
        val json = JSONObject(sb.toString())
        return parseAsteroidsJsonResult(json)
    }
}


