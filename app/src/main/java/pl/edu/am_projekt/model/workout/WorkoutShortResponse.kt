package pl.edu.am_projekt.model.workout

data class WorkoutShortResponse(
    val id: Int,
    val name: String,
    val date: String,
    val duration: String,
    val exercises: List<String>
)