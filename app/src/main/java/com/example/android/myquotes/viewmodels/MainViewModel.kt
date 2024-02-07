package com.example.android.myquotes.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.android.myquotes.repository.QuoteRepository
import androidx.lifecycle.viewModelScope
import com.example.android.myquotes.models.QuoteList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainViewModel(private val repository: QuoteRepository,private val data: String) : ViewModel() {
    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getQuotes(data)
        }
    }
    val quotes : LiveData<QuoteList>
    get() = repository.quotes

}