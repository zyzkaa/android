package pl.edu.am_projekt.model.workout.response
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StrengthExerciseParamsResponse(
    var set: Int,
    var weight: Double,
    var repetitions: Int,
    var volume: Int,
) : Parcelable
