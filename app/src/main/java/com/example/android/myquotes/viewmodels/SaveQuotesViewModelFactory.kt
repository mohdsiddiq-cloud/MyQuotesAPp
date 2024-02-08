package com.example.android.myquotes.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.myquotes.repository.QuoteRepository

class SaveQuotesViewModelFactory(private val repository: QuoteRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SaveQuotesViewModel(repository) as T
    }
}