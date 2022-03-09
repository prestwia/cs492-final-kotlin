package com.example.hangman.data

import android.util.Log
import com.example.hangman.api.WordNikService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WordRepository(
    private val service: WordNikService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun loadWordRequest(maxLength: Int, key: String) :  Result<String> = withContext(ioDispatcher) {
        try {
            val results = service.getWord(
                maxLength.toString(),
                key
            )
            Result.success(results.word)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}