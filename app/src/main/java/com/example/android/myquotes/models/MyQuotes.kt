package com.example.android.myquotes.models


import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.android.myquotes.converters.Converters

@Entity(tableName = "myquote")
@TypeConverters(Converters::class)
data class MyQuotes(
    @PrimaryKey(autoGenerate = true)
    val quoteId: Int,
    val author: String,
    val content: String,
    val tags: String,
)