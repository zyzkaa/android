package pl.edu.am_projekt.model.workout

data class CardioExerciseParamsResponse(
    val interval: Int,
    val speed: Double,
    val time: String,
    val calories: Double
)
