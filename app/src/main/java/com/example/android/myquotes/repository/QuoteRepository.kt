package com.example.android.myquotes.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.myquotes.api.QuoteService
import com.example.android.myquotes.models.QuoteList
import com.example.android.myquotes.db.QuoteDatabase
import com.example.android.myquotes.utils.NetworkUtils
import com.example.android.myquotes.models.Result

class QuoteRepository(
    private val quoteService: QuoteService,
    private val quoteDatabase: QuoteDatabase,
    private val applicationContext: Context,
) {
    private var quotesLiveData= MutableLiveData<QuoteList>()

    val quotes: LiveData<QuoteList> get() = quotesLiveData

    val saveQuotesMutable = MutableLiveData<List<Result>>()
    val saveQuotes: LiveData<List<Result>> get() = saveQuotesMutable


    suspend fun getQuotes(tag: String){

        if(NetworkUtils.isInternetAvailable(applicationContext)){
            var result= quoteService.getQuotes(tag);
            if(result?.body() != null){
                quotesLiveData.postValue(result.body());
            }
        }
    }
    suspend fun saveQuote(quote: Result){
        quoteDatabase.quotesDao().addQuote(quote)
    }

    suspend fun getSavedQuotes(){
        var quotes= quoteDatabase.quotesDao().getQuote()
        saveQuotesMutable.postValue(quotes)
    }

    suspend fun deleteQuote(quote: Result){
        quoteDatabase.quotesDao().deleteQuote(quote)
    }
}