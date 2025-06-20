package pl.edu.am_projekt.model.parcelable

data class Product(
    val id: Int,
    val name: String,
    val caloriesPer100g: Int,
    val proteinPer100g: Double,
    val carbsPer100g: Double,
    val fatPer100g: Double
)
