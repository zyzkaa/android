package pl.edu.am_projekt.model.workout.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WorkoutDetailsResponse(
    val id: Int,
    val name: String,
    val date: String,
    val duration: String,
    val strengthExercises: List<StrengthExerciseReponse> = emptyList(),
    val cardioExercises: List<CardioExerciseResponse> = emptyList(),
    val totalCalories: Double
) : Parcelable

