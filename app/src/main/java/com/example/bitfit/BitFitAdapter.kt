package com.example.bitfit

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bitfit.models.DisplayCalories

class BitFitAdapter(private val context: Context, private val calories: List<DisplayCalories>) :
    RecyclerView.Adapter<BitFitAdapter.ViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_calories, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = calories[position]
        holder.bind(article)
    }

    override fun getItemCount() = calories.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvFood = itemView.findViewById<TextView>(R.id.tvFood)
        private val tvCalories = itemView.findViewById<TextView>(R.id.tvCalories)

        fun bind(calories: DisplayCalories) {
            tvFood.text = calories.name
            tvCalories.text = calories.calories.toString()
        }
    }
}