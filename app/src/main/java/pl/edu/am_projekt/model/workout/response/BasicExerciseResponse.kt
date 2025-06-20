package pl.edu.am_projekt.model.workout.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BasicExerciseResponse(
    val id: Int,
    val namePl: String,
    val nameEn: String
) : Parcelable
