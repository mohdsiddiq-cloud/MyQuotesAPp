package com.example.android.myquotes.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.myquotes.R
import com.example.android.myquotes.applications.QuoteApplication
import com.example.android.myquotes.databinding.ActivityMyQuotesBinding
import com.example.android.myquotes.models.MyQuotes
import com.example.android.myquotes.repository.QuoteRepository
import com.example.android.myquotes.viewmodels.QuotesViewModel
import com.example.android.myquotes.viewmodels.SaveQuotesViewModel
import com.example.android.myquotes.viewmodels.SaveQuotesViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.*

class MyQuotesActivity : AppCompatActivity() {
    lateinit var quotesViewModel: QuotesViewModel
    lateinit var data: LiveData<List<MyQuotes>>
    lateinit var shareButton: FloatingActionButton
    lateinit var saveButton: FloatingActionButton
    private lateinit var progressBar: ProgressBar
    lateinit var prev  : TextView
    lateinit var next   : TextView
    var temp= MyQuotes(-1,"Author","Quotes","No Quotes Found")
    var ind=0
    lateinit var repository: QuoteRepository
    lateinit var saveQuotesViewModel: SaveQuotesViewModel
    lateinit var binding: ActivityMyQuotesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this,R.layout.activity_my_quotes)
        binding.result=temp
        repository= (application as QuoteApplication).repository
        saveQuotesViewModel= ViewModelProvider(this, SaveQuotesViewModelFactory(repository)).get(SaveQuotesViewModel::class.java)
        progressBar= findViewById(R.id.progressBar2)
        prev=findViewById<TextView>(R.id.prev2)
        next=findViewById<TextView>(R.id.next2)
        shareButton=findViewById<FloatingActionButton>(R.id.floatingActionButton2)
        saveButton=findViewById<FloatingActionButton>(R.id.floatingActionButtonSave2)

        showProgressBar()
        CoroutineScope(Dispatchers.Main).launch {
            delay(3000) // 3 seconds delay
            progressBar.visibility = View.INVISIBLE
            prev.visibility= View.VISIBLE
            next.visibility= View.VISIBLE
            quotesViewModel.index.observe(this@MyQuotesActivity, Observer {
                ind=it
                if(data.value?.size!=0)
                    temp= data.value?.get(it)!!
                binding.result=temp
            })
        }
        quotesViewModel= ViewModelProvider(this).get(QuotesViewModel::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            val job= CoroutineScope(Dispatchers.IO).async {
                printQuotes()
            }
            job.await()
        }
    }
    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    suspend fun printQuotes() {
        val job= CoroutineScope(Dispatchers.IO).async {
            saveQuotesViewModel.myQuotes
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
        intent.putExtra(Intent.EXTRA_TEXT,"Quote : ${temp.content} \n Author: ${temp.author}")
        startActivity(intent)
    }

    fun onDelete(view: View) {
        CoroutineScope(Dispatchers.IO).launch {
            if(data.value?.size!! >0)
                repository.deleteQuote(data.value?.get(ind)!!)

        }
        val intent = intent
        finish()
        startActivity(intent)

        Toast.makeText(this,"Unsaved", Toast.LENGTH_SHORT).show()
    }
}