package pl.edu.am_projekt.model.workout.request

import com.google.gson.annotations.SerializedName

data class StrengthExercise(
    @SerializedName("exerciseId")
    val exerciseId: Int,
    @SerializedName("params")
    val params: List<StrengthParams>
)
