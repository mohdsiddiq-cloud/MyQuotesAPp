package com.example.android.myquotes.api

import com.example.android.myquotes.models.QuoteList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface QuoteService {

    @GET("/quotes")
    suspend fun getQuotes(@Query("tags") tags : String) : Response<QuoteList>;

}