package com.example.robertpolovitzer.babyfoot.api

import android.content.Context
import android.widget.Toast
import com.example.robertpolovitzer.babyfoot.api.objects.LoginObject
import com.example.robertpolovitzer.babyfoot.api.objects.LoginResponseObject
import com.example.robertpolovitzer.babyfoot.api.objects.MatchListObject
import com.example.robertpolovitzer.babyfoot.helpers.AppHelper
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import com.squareup.okhttp.OkHttpClient
import retrofit.Callback
import retrofit.RequestInterceptor
import retrofit.RestAdapter
import retrofit.RetrofitError
import retrofit.client.OkClient
import retrofit.client.Response
import retrofit.converter.GsonConverter
import retrofit.http.*
import retrofit.mime.TypedFile
import java.io.IOException
import java.util.ArrayList
import java.util.concurrent.TimeUnit

/**
 * Created by robertpolovitzer on 18-02-21.
 */
class ApiHandler {

    var context: Context? = null

    private var ApiService: ApiInterface? = null
    private var AppKey: String = "3w5MTCehQUfNzPcwC26WKyPBbRUxEGxr"
    private var SecretKey: String = "QR4cjjEVhvsewwqwuZ8fm699LYFEzfXs"
    val ApiPageBaseUrl = "http://babyfoot.agyl.ninja"

    interface ApiInterface {
        @GET("/v1/matches")
        fun getAllMatches(callback: Callback<ArrayList<MatchListObject>>)

        @POST("/v1/user/login")
        fun postLogin(@Body loginInfo: LoginObject, callback: Callback<LoginResponseObject>)
    }

    private val RequestBearer = RequestInterceptor {
        request -> request.addHeader("Authorization", "Bearer " + AppHelper().getBearer(context!!))
    }

    private val RequestBasic = RequestInterceptor {
        request -> request.addHeader("Authorization", "Basic M3c1TVRDZWhRVWZOelBjd0MyNldLeVBCYlJVeEVHeHI6UVI0Y2pqRVZodnNld3dxd3VaOGZtNjk5TFlGRXpmWHM=")
    }

    class NullStringToEmptyAdapterFactory<T> : TypeAdapterFactory {
        override fun <T> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T>? {
            val rawType = type.rawType as Class<T>

            return if (rawType != String::class.java) {
                null
            } else StringAdapter() as TypeAdapter<T>

        }
    }

    class StringAdapter : TypeAdapter<String>() {
        @Throws(IOException::class)
        override fun read(reader: JsonReader): String {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull()
                return ""
            }
            return reader.nextString()
        }

        @Throws(IOException::class)
        override fun write(writer: JsonWriter, value: String?) {
            if (value == null) {
                writer.nullValue()
                return
            }
            writer.value(value)
        }
    }

    private val GSON = GsonBuilder()
            .setExclusionStrategies(object : ExclusionStrategy {
                override fun shouldSkipField(f: FieldAttributes): Boolean {
                    return false
                }

                override fun shouldSkipClass(clazz: Class<*>): Boolean {
                    return false
                }
            })
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            .registerTypeAdapterFactory(NullStringToEmptyAdapterFactory<Any>())
            .create()

    fun getService(context: Context?, checkIfBearer: Boolean): ApiInterface? {
        this.context = context
        if (ApiService == null) {
            val okHttpClient = OkHttpClient()
            okHttpClient.setReadTimeout(60, TimeUnit.SECONDS)
            okHttpClient.setConnectTimeout(60, TimeUnit.SECONDS)

            ApiService = RestAdapter.Builder()
                    .setEndpoint(ApiPageBaseUrl)
                    .setRequestInterceptor(if(checkIfBearer) { RequestBearer } else { RequestBasic})
                    .setConverter(GsonConverter(GSON))
                    .setClient(OkClient(okHttpClient))
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .build().create(ApiInterface::class.java)
        }

        return ApiService
    }

}