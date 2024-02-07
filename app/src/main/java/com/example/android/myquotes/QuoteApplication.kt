package com.example.android.myquotes

import android.app.Application
import androidx.lifecycle.Observer
import com.example.android.myquotes.adapters.QuotesAdapter
import com.example.android.myquotes.api.QuoteService
import com.example.android.myquotes.api.RetrofitHelper
import com.example.android.myquotes.repository.QuoteRepository
import com.example.android.myquotes.db.QuoteDatabase

class QuoteApplication() : Application() {
    lateinit var repository: QuoteRepository

    override fun onCreate() {
        super.onCreate()
        initialize();
    }
    private fun initialize(){
        val service = RetrofitHelper.getInstance().create(QuoteService::class.java)
        val database = QuoteDatabase.getDatabase(applicationContext)

        repository = QuoteRepository(service,database,applicationContext)
    }
}