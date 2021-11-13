package com.cryoggen.android.vklikes.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.vk.com/"

private val moshi: Moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface VkApiService {
    @GET("method/photos.getAll")
    suspend fun getProperties(
        @Query("access_token") token: String,
        @Query("v") version: String,
        @Query("extended") extended: String,
        @Query("count") count: String,
        @Query("offset") offset: String
        //   @Query("owner_id") ownerId: String - if you need to add a user ID to the request (by default, the current user ID is passed)
    ):
            VkProperty
}

object VkApi {
    val retrofitService: VkApiService by lazy {
        retrofit.create(VkApiService::class.java)
    }
}


