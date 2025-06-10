package pl.edu.am_projekt.model.workout

data class WorkoutDetailsResponse(
    val id: Int,
    val name: String,
    val date: String,
    val duration: String,
    val strengthExercises: List<StrengthExerciseReponse>,
    val cardioExercises: List<CardioExerciseResponse>,
    val totalCalories: Double
)

