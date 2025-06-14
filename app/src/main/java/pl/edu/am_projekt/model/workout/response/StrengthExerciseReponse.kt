package pl.edu.am_projekt.model.workout.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StrengthExerciseReponse(
    var strengthExercise: StrengthExerciseInfoResponse,
    var params: List<StrengthExerciseParamsResponse>,
    var totalVolume: Int
) : ExerciseResponse(), Parcelable
