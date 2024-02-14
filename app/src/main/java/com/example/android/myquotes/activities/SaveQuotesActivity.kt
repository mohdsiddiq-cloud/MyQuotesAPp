package com.example.android.myquotes.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.myquotes.QuoteApplication
import com.example.android.myquotes.R
import com.example.android.myquotes.models.Result
import com.example.android.myquotes.repository.QuoteRepository
import com.example.android.myquotes.viewmodels.QuotesViewModel
import com.example.android.myquotes.viewmodels.SaveQuotesViewModel
import com.example.android.myquotes.viewmodels.SaveQuotesViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.*

class SaveQuotesActivity : AppCompatActivity() {
    lateinit var quotesViewModel: QuotesViewModel
    lateinit var data: LiveData<List<Result>>
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
    lateinit var categoryData: String
    lateinit var temp:Result
    var ind=-1
    lateinit var repository: QuoteRepository
    lateinit var saveQuotesViewModel:SaveQuotesViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_quotes)
        repository= (application as QuoteApplication).repository
        saveQuotesViewModel= ViewModelProvider(this, SaveQuotesViewModelFactory(repository)).get(SaveQuotesViewModel::class.java)
        progressBar= findViewById(R.id.progressBar1)
        prev=findViewById<TextView>(R.id.prev1)
        next=findViewById<TextView>(R.id.next1)
        shareButton=findViewById<FloatingActionButton>(R.id.floatingActionButton1)
        saveButton=findViewById<FloatingActionButton>(R.id.floatingActionButtonSave1)

        showProgressBar()
        CoroutineScope(Dispatchers.Main).launch {
            delay(3000) // 3 seconds delay
            progressBar.visibility = View.INVISIBLE
            prev.visibility= View.VISIBLE
            next.visibility= View.VISIBLE
        }
        quotesViewModel= ViewModelProvider(this).get(QuotesViewModel::class.java)
        quoteText= findViewById<TextView>(R.id.quoteText1)
        quoteAuthor= findViewById<TextView>(R.id.quoteAuthor1)
        quoteHead= findViewById<TextView>(R.id.headline1)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val job = async {
                    saveQuotesViewModel.saveQuotes
                }
                data = job.await()
                ind=0;
                withContext(Dispatchers.Main) {
                    quotesViewModel.index.observe(this@SaveQuotesActivity, Observer {

                        ind=it
                        temp= data.value?.get(it)!!

                        if(temp?.content!=null){
                            currentQuote= temp.content.toString()
                            quoteText.text= temp.content.toString()
                        }
                        else{
                            currentQuote= ""
                            quoteText.text="Quote"
                        }
                        if(temp?.author!=null){
                            currentAuthor=temp.author.toString()
                            quoteAuthor.text=temp.author.toString()
                        }
                        else{
                            currentAuthor=""
                            quoteAuthor.text="Author"
                        }
                        if(temp?.tags!=null){
                            var count=0
                            var txt:String =""
                            for(item in temp.tags!!){
                                txt= txt+ "\n" + item
                                count++
                                if(count>2)
                                    break
                            }
                            quoteHead.text= txt
                        }
                        else{
                            quoteHead.text= "No Quotes Found"
                        }
                    })
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("quotes-not-fetch",e.printStackTrace().toString())
            }
        }

    }
    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    suspend fun printQuotes() {
        val job=CoroutineScope(Dispatchers.IO).async {
            saveQuotesViewModel.saveQuotes
        }
        data=job.await()
    }

    fun onPrevious(view: View) {
        if(quotesViewModel.index.value!! > 0)
            quotesViewModel.quotesBack()
        else{
            Toast.makeText(this,"first Quote", Toast.LENGTH_SHORT).show()
        }
    }
    fun onNext(view: View) {
        if(quotesViewModel.index.value!! < data.value?.size!!-1){
            quotesViewModel.quotesNext()
        }
        else{
            Toast.makeText(this,"last quote", Toast.LENGTH_SHORT).show()
        }
    }

    fun onShare(view: View) {
        val intent= Intent(Intent.ACTION_SEND)
        intent.setType("text/plain")
        intent.putExtra(Intent.EXTRA_TEXT,"Quote : $currentQuote \n Author: $currentAuthor")
        startActivity(intent)
    }

    fun onDelete(view: View) {
        CoroutineScope(Dispatchers.IO).launch {
            if(data.value?.size!! >0)
                repository.deleteQuote(data.value?.get(ind)!!)
        }
        Toast.makeText(this,"Unsaved", Toast.LENGTH_SHORT).show()
    }
}