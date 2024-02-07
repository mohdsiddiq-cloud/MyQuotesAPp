package com.example.android.myquotes.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.android.myquotes.models.Result

@Dao
interface QuoteDao {
    @Insert
    suspend fun addQuote(quotes :List<Result>)

    @Query("Select * from quote")
    suspend fun getQuote() : List<Result>
}