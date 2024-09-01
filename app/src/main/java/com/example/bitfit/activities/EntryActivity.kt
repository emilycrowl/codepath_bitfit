package com.example.bitfit.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.bitfit.R
import com.example.bitfit.models.DisplayCalories

const val ENTRY_EXTRA = "ENTRY_EXTRA"

class EntryActivity : AppCompatActivity() {
    private lateinit var etFood: EditText
    private lateinit var etCalories: EditText
    private lateinit var btnSubmit: Button
    private val TAG = "EntryActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entry)

        // Get the views
        etFood = findViewById(R.id.etFoodEntry)
        etCalories = findViewById(R.id.etCaloriesEntry)
        btnSubmit = findViewById(R.id.btnSubmit)

        // When "Submit" button is clicked, launch the list activity
        // and pass the new Food as an Extra
        btnSubmit.setOnClickListener {
            Log.d(TAG, "submit clicked")
            val intent = Intent(this, MainActivity::class.java)
            val calories = DisplayCalories(etFood.text.toString(), etCalories.text.toString().toLong())
            intent.putExtra(ENTRY_EXTRA, calories)
            this.startActivity(intent)
        }
    }
}