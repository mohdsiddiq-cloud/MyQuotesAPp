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
import com.example.android.myquotes.applications.QuoteApplication
import com.example.android.myquotes.R
import com.example.android.myquotes.databinding.ActivityQuotesBinding
import com.example.android.myquotes.models.QuoteList
import com.example.android.myquotes.repository.QuoteRepository
import com.example.android.myquotes.viewmodels.MainViewModel
import com.example.android.myquotes.viewmodels.MainViewModelFactory
import com.example.android.myquotes.viewmodels.QuotesViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.*
import com.example.android.myquotes.models.Result


class QuotesActivity : AppCompatActivity() {
    lateinit var mainViewModel: MainViewModel
    lateinit var quotesViewModel: QuotesViewModel
    lateinit var data: LiveData<QuoteList>
    lateinit var shareButton: FloatingActionButton
    lateinit var saveButton: FloatingActionButton
    private lateinit var progressBar: ProgressBar
    lateinit var prev  : TextView
    lateinit var next   : TextView
    lateinit var repository: QuoteRepository
    lateinit var categoryData: String
    var ind=0
    var temp=Result(-1,"id","Author","Slug","Quotes","date","dateModified",1, listOf("Category"))
    lateinit var binding: ActivityQuotesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_quotes)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_quotes)
        binding.result=temp
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
            quotesViewModel.index.observe(this@QuotesActivity, Observer {
                ind=it
                temp= data.value?.results?.get(it)!!
                binding.result= temp;
            })
        }

        categoryData= intent.getStringExtra("category").toString()
        repository= (application as QuoteApplication).repository
        mainViewModel= ViewModelProvider(this,
            MainViewModelFactory(repository,categoryData!!)
        ).get(MainViewModel::class.java)

        quotesViewModel= ViewModelProvider(this).get(QuotesViewModel::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            val job=CoroutineScope(Dispatchers.IO).async {
                printQuotes()
            }
            job.await()
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
        intent.putExtra(Intent.EXTRA_TEXT,"Quote : ${temp.content} \n Author: ${temp.author}")
        startActivity(intent)
    }

    fun onSave(view: View) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.saveQuote(data.value?.results!!.get(ind))
        }
        Toast.makeText(this,"Saved Successfully",Toast.LENGTH_SHORT).show()
    }

}