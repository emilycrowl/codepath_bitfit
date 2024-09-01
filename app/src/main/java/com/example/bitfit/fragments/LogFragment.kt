package com.example.bitfit.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bitfit.BitFitAdapter
import com.example.bitfit.BitFitApplication
import com.example.bitfit.R
import com.example.bitfit.models.CaloriesEntity
import com.example.bitfit.models.DisplayCalories
import kotlinx.coroutines.launch

class LogFragment() : Fragment() {
    private val articles = mutableListOf<DisplayCalories>()
    private lateinit var rvCalories: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_log, container, false)

        rvCalories = view.findViewById(R.id.rvCalories)

        val bitfitAdapter = BitFitAdapter(view.context, articles)
        rvCalories.adapter = bitfitAdapter
        rvCalories.layoutManager = LinearLayoutManager(view.context).also {
            val dividerItemDecoration = DividerItemDecoration(view.context, it.orientation)
            rvCalories.addItemDecoration(dividerItemDecoration)
        }

        // update the RecyclerView when db is updated
        lifecycleScope.launch {
            (activity?.application as BitFitApplication).db.caloriesDao().getAll().collect { databaseList: List<CaloriesEntity> ->

                databaseList.map { entity ->
                    DisplayCalories(
                        entity.name,
                        entity.calories,
                    )
                }.also { mappedList ->
                    articles.clear()
                    articles.addAll(mappedList)
                    bitfitAdapter.notifyDataSetChanged()
                }
            }
        }

        return view
    }

    fun getAdapter(): BitFitAdapter {
        return rvCalories.adapter as BitFitAdapter
    }

    fun getCalories(): MutableList<DisplayCalories> {
        return articles
    }

    companion object {
        fun newInstance(): LogFragment {
            return LogFragment()
        }
    }
}