package pl.edu.am_projekt.model

data class MealPlanDto(
    val id: Int,
    val date: String,
    val userName: String,
    val meals: List<MealReturnDto>
)


