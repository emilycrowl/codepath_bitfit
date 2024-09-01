package com.example.bitfit.models


data class DisplayCalories(
    val name: String?,
    val calories: Long?,
    val totalCalories: Long? = 0L
) : java.io.Serializable