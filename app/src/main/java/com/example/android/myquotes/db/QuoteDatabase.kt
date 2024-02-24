package com.example.android.myquotes.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.android.myquotes.models.MyQuotes
import com.example.android.myquotes.models.Result


@Database(entities = [Result::class,MyQuotes::class], version = 1)
abstract class QuoteDatabase : RoomDatabase() {
    abstract fun quotesDao(): QuoteDao
    companion object{
        private var INSTANCE: QuoteDatabase?=null
        fun getDatabase(context: Context) : QuoteDatabase {
            synchronized(this){
                if(INSTANCE ==null){
                    INSTANCE = Room.databaseBuilder(context, QuoteDatabase::class.java,"quoteDB").build()
                }
            }
            return INSTANCE!!;
        }
    }

}