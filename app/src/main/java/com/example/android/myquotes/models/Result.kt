package com.example.android.myquotes.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.android.myquotes.converters.Converters

@Entity(tableName = "quote")
@TypeConverters(Converters::class) // Specify the Type Converter
data class Result(
    @PrimaryKey(autoGenerate = true)
    val quoteId: Int,
    val _id: String,
    val author: String,
    val authorSlug: String,
    val content: String,
    val dateAdded: String,
    val dateModified: String,
    val length: Int,
    val tags: List<String>? // Nullable list of strings
)