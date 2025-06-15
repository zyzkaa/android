package pl.edu.am_projekt.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pl.edu.am_projekt.model.ExerciseWithTrackData
import pl.edu.am_projekt.model.InputData
import pl.edu.am_projekt.R

class WorkoutTrackAdapter(private var exercises: MutableList<ExerciseWithTrackData>)
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

        if(exerciseData.exercise.muscles == null){
            holder.nameLeft.text = "predkosc"
            holder.nameRight.text = "minuty"
        } else {
            holder.nameLeft.text = "wieght"
            holder.nameRight.text = "reps"
        }

        val inputAdapter = WorkoutTrackInputAdapter(mutableListOf()) { position ->
                exerciseData.data.removeAt(position)
        }
        holder.inputRecycler.layoutManager = LinearLayoutManager(holder.view.context)
        holder.inputRecycler.adapter = inputAdapter
        inputAdapter.setBoxes(exerciseData.data)


//        holder.inputAdapter.setBoxes(exerciseData.data)

//        holder.deleteButton.setOnClickListener{
//
//        }

        holder.addButton.setOnClickListener{
            val newBox = InputData(0.0, 0)
            exerciseData.data.add(newBox)
//            holder.inputAdapter.setBoxes(exerciseData.data)
            inputAdapter.addBox(exerciseData.data.last())
            exerciseData.data.forEach{
                data -> Log.d("data", data.first.toString() + " " + data.second.toString())
            }
        }


    }

    override fun getItemCount(): Int {
        return exercises.size
    }

    class WorkoutTrackHolder(val view: View) : RecyclerView.ViewHolder(view) {
//        val deleteButton : Button = view.findViewById(R.id.deleteButton)
        val nameLeft : TextView = view.findViewById(R.id.nameLeft)
        val nameRight : TextView = view.findViewById(R.id.nameRight)
        val addButton : Button = view.findViewById(R.id.addButton)
        val title : TextView = view.findViewById(R.id.title)
        val inputRecycler : RecyclerView = view.findViewById(R.id.inputRecycler)
//        init {
//            inputRecycler.layoutManager = LinearLayoutManager(view.context)
//            inputRecycler.adapter = inputAdapter
//        }
    }

    fun removeData(position: Int){
        exercises.removeAt(position)
        notifyItemRemoved(position)
    }

}
