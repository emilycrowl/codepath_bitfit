package com.example.bitfit.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bitfit.BitFitAdapter
import com.example.bitfit.BitFitApplication
import com.example.bitfit.R
import com.example.bitfit.databinding.ActivityMainBinding
import com.example.bitfit.models.CaloriesEntity
import com.example.bitfit.models.DisplayCalories
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val articles = mutableListOf<DisplayCalories>()
    private lateinit var rvCalorieList: RecyclerView
    private lateinit var tvTotalCalories: TextView
    private lateinit var btnAdd: Button
    private lateinit var binding: ActivityMainBinding
   private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Launching")

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        rvCalorieList = findViewById(R.id.rvCalorieList)
        tvTotalCalories = findViewById(R.id.tvTotalCalories)

        val bitfitAdapter = BitFitAdapter(this, articles)
        rvCalorieList.adapter = bitfitAdapter
        rvCalorieList.layoutManager = LinearLayoutManager(this).also {
            val dividerItemDecoration = DividerItemDecoration(this, it.orientation)
            rvCalorieList.addItemDecoration(dividerItemDecoration)
        }

        // update the RecyclerView when db is updated
        lifecycleScope.launch {
            (application as BitFitApplication).db.caloriesDao().getAll().collect { databaseList: List<CaloriesEntity> ->
                val totalCalories = databaseList.sumOf { it.calories ?: 0L } // Calculate total calories
                tvTotalCalories.text = "Total Calories: $totalCalories" // Update the TextView

                databaseList.map { entity ->
                    DisplayCalories(
                        entity.name,
                        entity.calories ?: 0L,
                        entity.totalCalories ?: 0L
                    )
                }.also { mappedList ->
                    articles.clear()
                    articles.addAll(mappedList)
                    bitfitAdapter.notifyDataSetChanged()
                }
            }
        }

        // check if MainActivity has an extra
        val calories = intent.getSerializableExtra("ENTRY_EXTRA")
        if (calories != null) {
            Log.d(TAG, "got an extra")
            Log.d(TAG, (calories as DisplayCalories).toString())
            // Since there's an extra, let's add it to the DB.
            lifecycleScope.launch(IO) {
                (application as BitFitApplication).db.caloriesDao().insert(
                    CaloriesEntity(
                        name = calories.name,
                        calories = calories.calories,
                        totalCalories = calories.totalCalories
                    )
                )
            }
        }
        else {
            Log.d(TAG, "no extra")
        }

        btnAdd = findViewById(R.id.btnAdd)
        btnAdd.setOnClickListener {
            Log.d(TAG, "add clicked")
            val intent = Intent(this, EntryActivity::class.java)
            this.startActivity(intent)
        }
    }
}