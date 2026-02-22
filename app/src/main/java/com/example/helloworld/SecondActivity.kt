package com.example.helloworld

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        val recyclerView = findViewById<RecyclerView>(R.id.myRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val lugares = listOf(
            MyItem(1, "Campus Sur UPM", 40.3894, -3.6266),
            MyItem(2, "Puerta del Sol", 40.4167, -3.7033),
            MyItem(3, "Retiro", 40.4153, -3.6839)
        )
        recyclerView.adapter = MyAdapter(lugares)
    }
}