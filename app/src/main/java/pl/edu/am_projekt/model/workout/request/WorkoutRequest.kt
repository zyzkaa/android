package pl.edu.am_projekt.model.workout.request

import com.google.gson.annotations.SerializedName

data class WorkoutRequest(
    @SerializedName("name")
    val name: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("duration")
    val duration: String,
    @SerializedName("strengthExercises")
    val strengthExercises: List<StrengthExercise>,
    @SerializedName("cardioExercises")
    val cardioExercises: List<CardioExercise>
)
