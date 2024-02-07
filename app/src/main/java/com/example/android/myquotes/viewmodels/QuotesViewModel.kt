package com.example.android.myquotes.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class QuotesViewModel : ViewModel() {
    private val _index = MutableLiveData<Int>()
    val index: LiveData<Int>
        get() = _index

    init {
        _index.value = 0;
    }
    fun quotesNext() {
        _index.value= (_index.value ?:0) +1
    }

    fun quotesBack() {
        if(_index.value!! > 0)
        _index.value= (_index.value ?:0) -1
    }
}