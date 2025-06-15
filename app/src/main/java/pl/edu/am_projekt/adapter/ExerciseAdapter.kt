package pl.edu.am_projekt.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.edu.am_projekt.R
import pl.edu.am_projekt.model.Exercise

class ExerciseAdapter(private var exercises: List<Exercise>, private val onExerciseClick: (Exercise) -> Unit)
    : RecyclerView.Adapter<ExerciseAdapter.ExerciseHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExerciseHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.exercise_choose_item, parent, false)
        return ExerciseHolder(view)
    }

    override fun onBindViewHolder(
        holder: ExerciseHolder,
        position: Int
    ) {
        val exercise = exercises[position]
        holder.exerciseName.text = exercise.name

        if (!exercise.muscles.isNullOrEmpty()) {
            exercise.muscles.forEach {
                m -> Log.d("muscle", m.name)
            }
            val musclesString = exercise.muscles!!.joinToString(", ") { it.name }
            holder.muscles.visibility = View.VISIBLE
            holder.muscles.text = musclesString
        } else {
            holder.muscles.visibility = View.GONE
        }

        holder.itemView.setOnClickListener{
            onExerciseClick(exercise)
        }
    }

    override fun getItemCount(): Int {
        return exercises.size
    }

    class ExerciseHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val exerciseName : TextView = view.findViewById(R.id.exerciseName)
        val muscles : TextView = view.findViewById(R.id.exerciseMuscles)
    }

    fun updateData(newItems: List<Exercise>) {
        Log.d("exercise adapter", "change data")
        exercises = newItems
        notifyDataSetChanged()
    }
}