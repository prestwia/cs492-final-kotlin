package com.example.hangman.api

import com.example.hangman.data.Word
import com.squareup.moshi.Moshi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

/* https://api.wordnik.com/v4/words.json/randomWords?hasDictionaryDef=true&maxCorpusCount=-1&minDictionaryCount=1&maxDictionaryCount=-1&minLength=5&maxLength=-1&limit=10&api_key=YOURAPIKEY */
interface WordNikService {
    @GET("randomWord?hasDictionaryDef=true&maxCorpusCount=-1&minDictionaryCount=1&maxDictionaryCount=-1&minLength=5&limit=10")
    suspend fun getWord (
        @Query("maxLength") maxLength: String,
        @Query("api_key") key: String
    ) : Word

    companion object {
        private const val BASE_URL = "https://api.wordnik.com/v4/words.json/"

        fun create() : WordNikService {
            val moshi = Moshi.Builder()
                .addLast(KotlinJsonAdapterFactory())
                .build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(WordNikService::class.java)
        }
    }
}