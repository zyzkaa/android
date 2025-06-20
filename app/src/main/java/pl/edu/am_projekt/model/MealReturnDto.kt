package pl.edu.am_projekt.model

data class MealReturnDto(
    val id: Int,
    val name: String,
    val calories: Double,
    val protein: Double,
    val carbs: Double,
    val fat: Double,
    val creatorID: Int,
    val editorID: Int?,
    val createdAt: String,
    val imageURL: String,
    val description: String?,
    val originalMealID: Int?,
    val ingredients: List<IngredientReturnDto>
)

