package com.example.bitfit.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.bitfit.BitFitApplication
import com.example.bitfit.R
import com.example.bitfit.models.DisplayCalories
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class DashboardFragment : Fragment() {
    private lateinit var tvDisplayAverage: TextView
    private lateinit var tvDisplayMax: TextView
    private lateinit var tvDisplayMin: TextView
    private lateinit var tvTotalCalories: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // inflate layout for fragment
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        tvDisplayAverage = view.findViewById(R.id.tvDisplayAvg)
        tvDisplayMin = view.findViewById(R.id.tvDisplayMin)
        tvDisplayMax = view.findViewById(R.id.tvDisplayMax)
        tvTotalCalories = view.findViewById(R.id.tvTotalCal)

        // update the RecyclerView when db is updated
        lifecycleScope.launch {
            (activity?.application as BitFitApplication).db.caloriesDao().getAll().collect { databaseList ->
                databaseList.map { entity ->
                    DisplayCalories(
                        entity.name,
                        entity.calories,
                        entity.totalCalories
                    )
                }.also { mappedList ->
                    update(mappedList)
                }
            }
        }

        var clearButtonView : Button = view.findViewById(R.id.btnClear)
        clearButtonView.setOnClickListener {
            lifecycleScope.launch(IO) {
                (activity?.application as BitFitApplication).db.caloriesDao().deleteAll()
            }
        }

        return view
    }

    private fun update(calories: List<DisplayCalories>) {

        if (calories.isEmpty()) {
            tvDisplayAverage.text = "No Data"
            tvDisplayMin.text = "No Data"
            tvDisplayMax.text = "No Data"
            tvTotalCalories.text = "No Data"
            return
        }

        var min : Long = Long.MAX_VALUE
        var max : Long = Long.MIN_VALUE
        var sum : Long = 0
        for (cal in calories) {
            cal.calories?.let {
                sum += it
                if (it < min) min = it
                if (it > max) max = it
            }
        }

        // update the average, max, and min
        tvDisplayAverage.text = (sum / calories.size).toString()
        tvDisplayMin.text = min.toString()
        tvDisplayMax.text = max.toString()
        tvTotalCalories.text = sum.toString()
    }


    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(): DashboardFragment {
            return DashboardFragment()
        }
    }
}