package com.example.android.myquotes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.myquotes.adapters.QuotesAdapter
import com.example.android.myquotes.viewmodels.MainViewModel
import com.example.android.myquotes.viewmodels.MainViewModelFactory

class MainActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        recyclerView=findViewById(R.id.recyclerView1)
        recyclerView.layoutManager= LinearLayoutManager(this)
        recyclerView.adapter=QuotesAdapter(ConstantData().CATEGORYDATA)
    }

}