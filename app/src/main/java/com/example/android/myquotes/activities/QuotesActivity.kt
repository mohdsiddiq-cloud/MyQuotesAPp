package com.example.android.myquotes.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.myquotes.QuoteApplication
import com.example.android.myquotes.R
import com.example.android.myquotes.models.QuoteList
import com.example.android.myquotes.repository.QuoteRepository
import com.example.android.myquotes.viewmodels.MainViewModel
import com.example.android.myquotes.viewmodels.MainViewModelFactory
import com.example.android.myquotes.viewmodels.QuotesViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.*


class QuotesActivity : AppCompatActivity() {
    lateinit var mainViewModel: MainViewModel
    lateinit var quotesViewModel: QuotesViewModel
    lateinit var data: LiveData<QuoteList>
    lateinit var shareButton: FloatingActionButton
    lateinit var saveButton: FloatingActionButton
    private lateinit var progressBar: ProgressBar
    lateinit var quoteText : TextView
    lateinit var quoteAuthor : TextView
    lateinit var quoteHead   : TextView
    lateinit var prev  : TextView
    lateinit var next   : TextView
    lateinit var currentQuote : String
    lateinit var currentAuthor : String
    lateinit var repository: QuoteRepository
    lateinit var categoryData: String
    var ind=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quotes)
        progressBar= findViewById(R.id.progressBar)
        prev=findViewById<TextView>(R.id.prev)
        next=findViewById<TextView>(R.id.next)
        shareButton=findViewById<FloatingActionButton>(R.id.floatingActionButton)
        saveButton=findViewById<FloatingActionButton>(R.id.floatingActionButtonSave)

        showProgressBar()
        CoroutineScope(Dispatchers.Main).launch {
            delay(3000) // 3 seconds delay
            progressBar.visibility = View.INVISIBLE
            prev.visibility= View.VISIBLE
            next.visibility= View.VISIBLE
        }
        categoryData= intent.getStringExtra("category").toString()
        repository= (application as QuoteApplication).repository
        mainViewModel= ViewModelProvider(this,
            MainViewModelFactory(repository,categoryData!!)
        ).get(MainViewModel::class.java)

        quoteText= findViewById<TextView>(R.id.quoteText)
        quoteAuthor= findViewById<TextView>(R.id.quoteAuthor)
        quoteHead= findViewById<TextView>(R.id.headline)

        quotesViewModel= ViewModelProvider(this).get(QuotesViewModel::class.java)

        quoteHead.text=categoryData

        CoroutineScope(Dispatchers.IO).launch {
            val job=CoroutineScope(Dispatchers.IO).async {
                printQuotes()
            }
            job.await()

            withContext(Dispatchers.Main){
                quotesViewModel.index.observe(this@QuotesActivity, Observer {

                    ind=it

                    val temp= data.value?.results?.get(it)

                    if(temp?.content!=null){
                        currentQuote= temp.content.toString()
                        quoteText.text= temp.content.toString()
                    }
                    else{
                        currentQuote= ""
                        quoteText.text="Quote"
                    }
                    if(temp?.author!=null){
                        currentAuthor=temp?.author.toString()
                        quoteAuthor.text=temp?.author.toString()
                    }
                    else{
                        currentAuthor=""
                        quoteAuthor.text="Author"
                    }
                })
            }
        }
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    suspend fun printQuotes() {
        val job=CoroutineScope(Dispatchers.IO).async {
            mainViewModel.quotes
        }
        data=job.await()
    }

    fun onPrevious(view: View) {
        if(quotesViewModel.index.value!! > 0)
        quotesViewModel.quotesBack()
        else{
            Toast.makeText(this,"first Quote",Toast.LENGTH_SHORT).show()
        }
    }
    fun onNext(view: View) {
        if(quotesViewModel.index.value!! < data.value?.results?.size!!-1){
            quotesViewModel.quotesNext()
        }
        else{
            Toast.makeText(this,"last quote",Toast.LENGTH_SHORT).show()
        }
    }

    fun onShare(view: View) {
        val intent= Intent(Intent.ACTION_SEND)
        intent.setType("text/plain")
        intent.putExtra(Intent.EXTRA_TEXT,"Quote : $currentQuote \n Author: $currentAuthor")
        startActivity(intent)
    }

    fun onSave(view: View) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.saveQuote(data.value?.results!!.get(ind))
        }
        Toast.makeText(this,"Saved Successfully",Toast.LENGTH_SHORT).show()
    }
}