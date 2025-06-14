package pl.edu.am_projekt.model.workout.response
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CardioExerciseParamsResponse(
    val interval: Int,
    val speed: Double,
    val time: String,
    val calories: Double
) : Parcelable
