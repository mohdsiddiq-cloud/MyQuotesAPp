package com.example.android.myquotes.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.myquotes.api.QuoteService
import com.example.android.myquotes.models.QuoteList
import com.example.android.myquotes.db.QuoteDatabase
import com.example.android.myquotes.utils.NetworkUtils

class QuoteRepository(
    private val quoteService: QuoteService,
    private val quoteDatabase: QuoteDatabase,
    private val applicationContext: Context,
) {
    private var quotesLiveData= MutableLiveData<QuoteList>()

    val quotes: LiveData<QuoteList> get() = quotesLiveData


    suspend fun getQuotes(tag: String){

        if(NetworkUtils.isInternetAvailable(applicationContext)){
            var result= quoteService.getQuotes(tag);
            if(result?.body() != null){
                quoteDatabase.quotesDao().addQuote(result.body()!!.results)
                quotesLiveData.postValue(result.body());
            }
        }
        else{
            var quotes= quoteDatabase.quotesDao().getQuote()
            var quotesList = QuoteList(1,1,1,quotes,1,1)
            quotesLiveData.postValue(quotesList)
        }
    }
}