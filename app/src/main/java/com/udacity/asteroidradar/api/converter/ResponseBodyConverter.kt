package com.udacity.asteroidradar.api.converter

import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.entities.NeoWSEntity
import okhttp3.ResponseBody
import okio.*
import org.json.JSONObject
import retrofit2.Converter
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList


object StringResponseBodyConverter : Converter<ResponseBody, ArrayList<NeoWSEntity>> {
    override fun convert(response: ResponseBody): ArrayList<NeoWSEntity>? {
        Timber.d("Start WSNeo Request")
        val responseBodySource: BufferedSource = response.source()
        val scanner = Scanner(responseBodySource.inputStream())
        val sb = StringBuffer()
        while(scanner.hasNext()){
            sb.append(scanner.nextLine())
        }
        val json = JSONObject(sb.toString())
        Timber.d("End WSNeo Request")
        return parseAsteroidsJsonResult(json)
    }
}


