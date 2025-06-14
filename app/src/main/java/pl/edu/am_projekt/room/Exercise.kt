package pl.edu.am_projekt.room

import android.content.Context
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import pl.edu.am_projekt.model.BasicDictResponse
import pl.edu.am_projekt.model.workout.response.StrengthExerciseInfoResponse

@Parcelize
data class Exercise(
    val id: Int,
    val name: String,
    val muscles: List<BasicDictResponse>?
) : Parcelable{
    companion object{
        fun fromBasicDictResponse(exercise: BasicDictResponse) : Exercise{
            return Exercise(exercise.id, exercise.name, null)
        }

        fun fromStrExercise(exercise: StrengthExerciseInfoResponse, context: Context) : Exercise{
            var muscles = exercise.muscles.map {
                m ->
                val resId = context.resources.getIdentifier(m.name, "string", context.packageName)
                val muscleName = if (resId != 0) context.getString(resId) else m.name
                m.copy(name = muscleName)
            }
            return Exercise(exercise.id, exercise.name, muscles)
        }
    }

}
