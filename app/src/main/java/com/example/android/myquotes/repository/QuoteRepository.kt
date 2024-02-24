package com.example.android.myquotes.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.myquotes.api.QuoteService
import com.example.android.myquotes.models.QuoteList
import com.example.android.myquotes.db.QuoteDatabase
import com.example.android.myquotes.models.MyQuotes
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

    val myQuotesMutable = MutableLiveData<List<MyQuotes>>()
    val myQuotes: LiveData<List<MyQuotes>> get() = myQuotesMutable


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
        getSavedQuotes()
    }

    suspend fun getSavedQuotes(){
        var quotes= quoteDatabase.quotesDao().getQuote()
        saveQuotesMutable.postValue(quotes)
    }

    suspend fun getMyQuotes(){
        var quotes= quoteDatabase.quotesDao().getMyQuote()
        myQuotesMutable.postValue(quotes)
    }
    suspend fun deleteQuote(quote: MyQuotes){
        quoteDatabase.quotesDao().deleteQuote(quote)
    }


    suspend fun deleteQuote(quote: Result){
        quoteDatabase.quotesDao().deleteQuote(quote)
    }
    suspend fun saveQuote(quote: MyQuotes){
        quoteDatabase.quotesDao().addQuote(quote)
        getSavedQuotes()
    }
}