package com.example.hangman.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hangman.api.WordNikService
import com.example.hangman.data.WordRepository
import kotlinx.coroutines.launch

class WordViewModel : ViewModel() {
    private val repository = WordRepository(WordNikService.create())

    private val _wordResult = MutableLiveData<String?>(null)
    val wordResult: LiveData<String?> = _wordResult

    fun loadReqResult(
        maxLength: Int,
        key: String
    ) {
        viewModelScope.launch {
            val result = repository.loadWordRequest(maxLength, key)
            _wordResult.value = result.getOrNull()
        }
    }
}