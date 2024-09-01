package com.example.bitfit.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.bitfit.BitFitApplication
import com.example.bitfit.R
import com.example.bitfit.databinding.ActivityMainBinding
import com.example.bitfit.fragments.DashboardFragment
import com.example.bitfit.fragments.LogFragment
import com.example.bitfit.models.CaloriesEntity
import com.example.bitfit.models.DisplayCalories
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val articles = mutableListOf<DisplayCalories>()
    private lateinit var btnAdd: Button
    private lateinit var binding: ActivityMainBinding
   private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Launching")

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        handleNewEntry()

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)

        // handle navigation selection
        bottomNavigationView.setOnItemSelectedListener { item ->
            lateinit var fragment: Fragment
            when (item.itemId) {
                R.id.tabLog -> fragment = LogFragment()
                R.id.tabDashboard -> fragment = DashboardFragment()
            }
            replaceFragment(fragment)
            true
        }

        // set default selection
        bottomNavigationView.selectedItemId = R.id.tabDashboard
    }

    private fun replaceFragment(newFragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.article_frame_layout, newFragment)
        fragmentTransaction.commit()
    }

    private fun handleNewEntry() {
        val calories = intent.getSerializableExtra("ENTRY_EXTRA")
        if (calories != null) {
            Log.d(TAG, "got an extra")
            Log.d(TAG, (calories as DisplayCalories).toString())

            lifecycleScope.launch(IO) {
                (application as BitFitApplication).db.caloriesDao().insert(
                    CaloriesEntity(
                        name = calories.name,
                        calories = calories.calories,
                        totalCalories = calories.totalCalories
                    )
                )
            }
            intent.removeExtra("ENTRY_EXTRA")
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