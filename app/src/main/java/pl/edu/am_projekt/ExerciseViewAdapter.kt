package pl.edu.am_projekt

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.edu.am_projekt.model.workout.CardioExerciseResponse
import pl.edu.am_projekt.model.workout.ExerciseResponse
import pl.edu.am_projekt.model.workout.StrengthExerciseReponse

class ExerciseViewAdapter(private val exercises: List<ExerciseResponse>, private val context: Context) : RecyclerView.Adapter<ExerciseViewAdapter.ExerciseViewHolder>(){

    class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val exerciseName : TextView = itemView.findViewById(R.id.exercise_name)
        val additionalInfo : TextView = itemView.findViewById(R.id.additional_info)
        val details: TextView = itemView.findViewById(R.id.details)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.workout_view_list_item, parent, false)
        return ExerciseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val exercise = exercises[position]

        holder.exerciseName.text = when (exercise){
            is StrengthExerciseReponse -> exercise.strengthExercise.name
            is CardioExerciseResponse -> exercise.cardioExercise.name
            else -> ""
        }

        holder.additionalInfo.text = when (exercise){
            is StrengthExerciseReponse -> exercise.strengthExercise.muscles.joinToString(", ") {
                val resId = context.resources.getIdentifier(it.name, "string", context.packageName)
                if (resId != 0) context.getString(resId) else it.name
            }
            is CardioExerciseResponse -> exercise.time
            else -> ""
        }

        holder.details.text = when (exercise){
            is StrengthExerciseReponse -> exercise.params.joinToString(", ") {"${it.weight} x ${it.repetitions}: ${it.volume}"}
            is CardioExerciseResponse -> String.format("%.0f", exercise.calories) + "kcal"
            else -> ""
        }
    }

    override fun getItemCount(): Int = exercises.size
}