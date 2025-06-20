package pl.edu.am_projekt.model

import android.content.Context
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import pl.edu.am_projekt.model.workout.response.BasicExerciseResponse
import pl.edu.am_projekt.model.workout.response.StrengthExerciseInfoResponse

@Parcelize
data class Exercise(
    val id: Int,
    val name: String,
    val muscles: List<BasicDictResponse>?
) : Parcelable{
    companion object{
        fun fromBasicDictResponse(exercise: BasicExerciseResponse, context: Context) : Exercise {
            val currentLang = context.resources.configuration.locales[0].language
            val name = if (currentLang == "pl") exercise.namePl else exercise.nameEn
            return Exercise(exercise.id, name, null)
        }

        fun fromStrExercise(exercise: StrengthExerciseInfoResponse, context: Context) : Exercise {
            val muscles = exercise.muscles.map {
                m ->
                val resId = context.resources.getIdentifier(m.name, "string", context.packageName)
                val muscleName = if (resId != 0) context.getString(resId) else m.name
                m.copy(name = muscleName)
            }
            val currentLang = context.resources.configuration.locales[0].language
            val name = if (currentLang == "pl") exercise.namePl else exercise.nameEn
            return Exercise(exercise.id, name, muscles)
        }
    }

}
