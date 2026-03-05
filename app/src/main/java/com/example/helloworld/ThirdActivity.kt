package com.example.helloworld

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.IOException

class ThirdActivity : AppCompatActivity() {
    private val TAG = "btaThirdActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_third)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        Log.d(TAG, "onCreate: The activity is being created.");

        val buttonNext: Button = findViewById(R.id.mapButton)
        buttonNext.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
            finish()
        }

        val buttonToThird: Button = findViewById(R.id.homeButton)
        buttonToThird.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        //Display the file contents
        val listView: ListView = findViewById(R.id.lvFileContents)
        //tvFileContents.text = readFileContents()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, readFileLines())
        listView.adapter = adapter
    }

    fun readFileLines(): List<String> {
        val fileName = "gps_coordinates.csv"
        return try {
            //Open the file from internal storage
            openFileInput(fileName).bufferedReader().readLines()
        } catch (e: IOException) {
            listOf("Error reading file: ${e.message}")
        }
    }
}