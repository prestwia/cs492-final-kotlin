package com.example.hangman.data

import com.example.hangman.api.WordNikWOTDService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WordOfTheDayRepository(
    private val serviceWOTD: WordNikWOTDService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun loadWordOfTheDayRequest(key: String) :  Result<String> = withContext(ioDispatcher) {
        try {
            val results = serviceWOTD.getWord(
                key
            )
            Result.success(results.word)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}