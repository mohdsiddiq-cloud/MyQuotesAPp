package com.example.android.myquotes.activities


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.android.myquotes.R
import com.example.android.myquotes.applications.QuoteApplication
import com.example.android.myquotes.models.MyQuotes
import com.example.android.myquotes.repository.QuoteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddQuotesActivity : AppCompatActivity() {
    lateinit var quoteText: EditText
    lateinit var authorText: EditText
    lateinit var repository: QuoteRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_quotes)
        quoteText = findViewById<EditText>(R.id.myQuoteText)
        authorText= findViewById<EditText>(R.id.myAuthorText)
        repository= (application as QuoteApplication).repository
    }

    fun onSaveMyQuote(view: View) {
        val quote:String= quoteText.text.toString().trim()
        val author:String= authorText.text.toString().trim()
        if(quote.isEmpty()){
            quoteText.error="This field is required"
        }
        if(author.isEmpty()){
            authorText.error="This field is required"
        }
        else{
            CoroutineScope(Dispatchers.IO).launch {
                repository.saveQuote(MyQuotes(0,author,quote,""))
            }
            quoteText.text.clear()
            authorText.text.clear()
            Toast.makeText(this,"Saved Successfully", Toast.LENGTH_SHORT).show()
        }
    }
}