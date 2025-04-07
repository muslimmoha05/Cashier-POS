package com.utopiatechhub.cashierpos.data

import androidx.room.DatabaseView

@DatabaseView(
    viewName = "food_category_view",
    value = """
        SELECT id, category_name AS categoryName, translated_name AS translatedName, photo_url AS photoUrl
        FROM food_category_table
    """
)
data class FoodCategoryView(
    val id: Long,
    val categoryName: String,
    val translatedName: String,
    val photoUrl: String
)