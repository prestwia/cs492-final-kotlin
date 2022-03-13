package com.example.hangman.api

import com.example.hangman.data.Word
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface WordNikWOTDService {
    @GET("wordOfTheDay")
    suspend fun getWord (
        @Query("api_key") key: String
    ) : Word

    companion object {
        private const val BASE_URL = "https://api.wordnik.com/v4/words.json/"

        fun create() : WordNikWOTDService {
            val moshi = Moshi.Builder()
                .addLast(KotlinJsonAdapterFactory())
                .build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(WordNikWOTDService::class.java)
        }
    }
}