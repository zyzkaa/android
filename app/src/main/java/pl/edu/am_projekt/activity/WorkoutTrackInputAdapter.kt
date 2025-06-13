package pl.edu.am_projekt.activity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import pl.edu.am_projekt.InputData
import androidx.core.widget.addTextChangedListener
import pl.edu.am_projekt.R

class WorkoutTrackInputAdapter (private val boxes: MutableList<InputData>) :
    RecyclerView.Adapter<WorkoutTrackInputAdapter.WorkoutTrackInputHolder>() {

    class WorkoutTrackInputHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val inputLeft: EditText = view.findViewById(R.id.inputLeft)
        val inputRight: EditText = view.findViewById(R.id.inputRight)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutTrackInputHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.workout_track_input_item, parent, false)
        return WorkoutTrackInputHolder(view)
    }

    override fun onBindViewHolder(holder: WorkoutTrackInputHolder, position: Int) {
        val box = boxes[position]
        holder.inputLeft.setText(box.first)
        holder.inputRight.setText(box.second)

        holder.inputLeft.addTextChangedListener { text -> box.first = text.toString().toInt() }
        holder.inputRight.addTextChangedListener { text -> box.second = text.toString().toInt() }
    }

    override fun getItemCount(): Int = boxes.size
}