package pl.edu.am_projekt

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import pl.edu.am_projekt.model.workout.ExerciseResponse
import pl.edu.am_projekt.room.Exercise

class ExerciseAdapter(private val exercises: List<Exercise>, private val context: Context)
    : RecyclerView.Adapter<ExerciseAdapter.ExerciseHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExerciseHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(
        holder: ExerciseHolder,
        position: Int
    ) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    class ExerciseHolder(val view: View) : RecyclerView.ViewHolder(view)
}