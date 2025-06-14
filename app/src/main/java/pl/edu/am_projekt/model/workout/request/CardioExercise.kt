package pl.edu.am_projekt.model.workout.request

data class CardioExercise(
    val exerciseId: Int,
    val params: List<CardioParams>
)
