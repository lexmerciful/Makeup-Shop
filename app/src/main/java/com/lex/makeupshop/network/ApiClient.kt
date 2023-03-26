package com.lex.makeupshop.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Creating a ApiClient object so as to call the Api service directly
 * without needing to initialize the class, so we don't need to create object of the class to use it
 */
object ApiClient {

    private val BASE_URL = "https://makeup-api.herokuapp.com/api/v1/"

    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}

/**
 * An interface is created to define how Retrofit
 * talks to the service using the GET method
 */
interface ApiService {
    @GET("products.json")
    suspend fun fetchMakeupItems(): List<MakeupItem>
}