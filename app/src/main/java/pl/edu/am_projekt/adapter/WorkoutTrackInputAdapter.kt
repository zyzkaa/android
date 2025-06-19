package pl.edu.am_projekt.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import pl.edu.am_projekt.R
import pl.edu.am_projekt.model.InputData

class WorkoutTrackInputAdapter (
    private val boxes: MutableList<InputData>,
    private val onDeleteBox: (position: Int) -> Unit) :
    RecyclerView.Adapter<WorkoutTrackInputAdapter.WorkoutTrackInputHolder>() {

    class WorkoutTrackInputHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val inputLeft: EditText = view.findViewById(R.id.inputLeft)
        val inputRight: EditText = view.findViewById(R.id.inputRight)
        val deleteButton: Button = view.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutTrackInputHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.workout_track_input_item, parent, false)
        return WorkoutTrackInputHolder(view)
    }

    override fun onBindViewHolder(holder: WorkoutTrackInputHolder, position: Int) {
        val box = boxes[position]

        holder.inputLeft.setTextIfChanged(box.first.toString())
        holder.inputRight.setTextIfChanged(box.second.toString())

        holder.inputLeft.doAfterTextChanged {
            box.first = it?.toString()?.toDoubleOrNull() ?: 0.0
        }
        holder.inputRight.doAfterTextChanged {
            box.second = it?.toString()?.toIntOrNull() ?: 0
        }

        holder.deleteButton.setOnClickListener{
            val pos = holder.adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                boxes.removeAt(pos)
                onDeleteBox(pos)
                notifyItemRemoved(position)
            }
        }
    }

    fun EditText.setTextIfChanged(text: String) {
        if (this.text.toString() != text) {
            setText(text)
            setSelection(text.length)
        }
    }


    override fun getItemCount(): Int = boxes.size

    fun addBox(newBox: InputData){
        Log.d("add box", "add box")
        boxes.add(newBox)
        notifyItemInserted(boxes.lastIndex)
    }

    fun setBoxes(newBoxes: List<InputData>){
        Log.d("set boxes", "set boxes")
        boxes.clear()
        boxes.addAll(newBoxes)
        notifyDataSetChanged()
    }
}