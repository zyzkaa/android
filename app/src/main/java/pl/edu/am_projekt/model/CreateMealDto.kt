package pl.edu.am_projekt.model

data class CreateMealDto(
    val name: String,
    val description: String? = null,
    val imageURL: String? = null,
    val isShared: Boolean = false,
    val originalMealID: Int? = null,
    val creatorID: Int,
    val ingredients: List<MealIngredientDto> = emptyList(),
) {
    data class MealIngredientDto(
        val productId: Int,
        val quantity: Int
    ) {}
}