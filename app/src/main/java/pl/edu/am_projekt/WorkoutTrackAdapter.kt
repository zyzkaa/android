package pl.edu.am_projekt

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.edu.am_projekt.activity.WorkoutTrackInputAdapter
import pl.edu.am_projekt.room.Exercise

class WorkoutTrackAdapter(private var exercises: MutableList<ExerciseWithTackData>)
    : RecyclerView.Adapter<WorkoutTrackAdapter.WorkoutTrackHolder>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WorkoutTrackHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.workout_track_item, parent, false)
        return WorkoutTrackHolder(view)
    }

    override fun onBindViewHolder(
        holder: WorkoutTrackHolder,
        position: Int
    ) {
        val exerciseData = exercises[position]
        holder.title.text = exerciseData.exercise.name

        val inputAdapter = WorkoutTrackInputAdapter(exerciseData.data)
        holder.inputRecycler.adapter = inputAdapter


        holder.deleteButton.setOnClickListener{
            exercises.removeAt(position)
            notifyItemRemoved(position)
        }

        holder.addButton.setOnClickListener{
            exerciseData.data.add(InputData(0, 0))
            inputAdapter.notifyItemInserted(exerciseData.data.lastIndex)
        }
    }

    override fun getItemCount(): Int {
        return exercises.size
    }

    class WorkoutTrackHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val deleteButton : Button = view.findViewById(R.id.deleteButton)
        val addButton : Button = view.findViewById(R.id.addButton)
        val title : TextView = view.findViewById(R.id.title)
        val inputRecycler : RecyclerView = view.findViewById(R.id.inputRecycler)
    }

    fun addData(newExercise: Exercise){
        val newEmptyExercise = ExerciseWithTackData(newExercise, mutableListOf())
        if(exercises.contains(newEmptyExercise)) return
        exercises.add(newEmptyExercise)
        notifyItemInserted(exercises.lastIndex)
    }

}
