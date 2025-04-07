package com.utopiatechhub.cashierpos.data

data class FoodWithCategory(
    val id: Long,
    val foodName: String,
    val foodPrice: Double,
    val foodCategoryId: Long,
    val categoryName: String
)