package pl.edu.am_projekt.model.workout.response

import android.os.Parcelable
import pl.edu.am_projekt.model.BasicDictResponse
import kotlinx.parcelize.Parcelize

@Parcelize
data class CardioExerciseResponse(
    var cardioExercise: BasicExerciseResponse,
    var params: List<CardioExerciseParamsResponse>,
    var calories: Double,
    var time: String
) : ExerciseResponse(), Parcelable
