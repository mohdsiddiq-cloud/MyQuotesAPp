package com.example.android.myquotes.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.myquotes.models.MyQuotes
import com.example.android.myquotes.models.QuoteList
import com.example.android.myquotes.repository.QuoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.android.myquotes.models.Result

class SaveQuotesViewModel(private val repository: QuoteRepository): ViewModel() {
    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getSavedQuotes()
            repository.getMyQuotes()
        }
    }
    val saveQuotes : LiveData<List<Result>>
        get() = repository.saveQuotes
    val myQuotes : LiveData<List<MyQuotes>>
        get() = repository.myQuotes
}