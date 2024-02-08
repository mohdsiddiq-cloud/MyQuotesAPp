package com.example.android.myquotes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.myquotes.adapters.QuotesAdapter
import com.example.android.myquotes.repository.QuoteRepository
import com.example.android.myquotes.viewmodels.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView=findViewById(R.id.recyclerView1)
        recyclerView.layoutManager= LinearLayoutManager(this)
        recyclerView.adapter=QuotesAdapter(ConstantData().CATEGORYDATA)
    }
    fun onSaveQuotes(view: View) {

        CoroutineScope(Dispatchers.Main).launch {
            val intent= Intent(this@MainActivity,SaveQuotesActivity::class.java)
            startActivity(intent)
        }
    }


}