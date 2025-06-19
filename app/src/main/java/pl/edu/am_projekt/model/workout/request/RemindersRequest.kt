package pl.edu.am_projekt.model.workout.request

data class RemindersRequest (
    val time: String,
    val days: List<Int>
)