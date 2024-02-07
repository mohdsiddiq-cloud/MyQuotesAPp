package com.example.android.myquotes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.myquotes.models.QuoteList
import com.example.android.myquotes.viewmodels.MainViewModel
import com.example.android.myquotes.viewmodels.MainViewModelFactory
import com.example.android.myquotes.viewmodels.QuotesViewModel

class QuotesActivity : AppCompatActivity() {
    lateinit var mainViewModel: MainViewModel
    lateinit var quotesViewModel: QuotesViewModel
    lateinit var data: LiveData<QuoteList>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quotes)

        var categoryData= intent.getStringExtra("category")
        val repository= (application as QuoteApplication).repository
        mainViewModel= ViewModelProvider(this,
            MainViewModelFactory(repository,categoryData!!)
        ).get(MainViewModel::class.java)

        data= mainViewModel.quotes

        quotesViewModel= ViewModelProvider(this).get(QuotesViewModel::class.java)

        val quoteText= findViewById<TextView>(R.id.quoteText)
        val quoteAuthor= findViewById<TextView>(R.id.quoteAuthor)

        quotesViewModel.index.observe(this, Observer {
            var temp= data?.value?.results?.get(it)
            if(temp?.content!=null){
                quoteText.text= temp.content.toString()
            }
            else{
                quoteText.text="Wait"

            }
            if(temp?.author!=null){
                quoteAuthor.text=data.value?.results?.get(it)?.author.toString()
            }
           else{
               quoteAuthor.text="Anonymous"
            }

        })
    }

    fun onPrevious(view: View) {
        if(quotesViewModel.index.value!! > 0)
        quotesViewModel.quotesBack()
        else{
            Toast.makeText(this,"first Quote",Toast.LENGTH_SHORT).show()
        }
    }
    fun onNext(view: View) {
        if(quotesViewModel.index.value!! < data?.value?.results?.size!!-2){
            quotesViewModel.quotesNext()
        }
        else{
            Toast.makeText(this,"last quote",Toast.LENGTH_SHORT).show()
        }
    }
}