package pl.edu.am_projekt.model.workout.response

import android.os.Parcelable
import pl.edu.am_projekt.model.BasicDictResponse
import kotlinx.parcelize.Parcelize

@Parcelize
data class StrengthExerciseInfoResponse(
    var muscles: List<BasicDictResponse>,
    var id: Int,
    var name: String
) : Parcelable
