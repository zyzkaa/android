package pl.edu.am_projekt

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.edu.am_projekt.activity.WorkoutViewActivity
import pl.edu.am_projekt.model.workout.WorkoutShortResponse

class WorkoutAdapter(private val workouts: List<WorkoutShortResponse>) :
    RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder>(){

    class WorkoutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val workoutName : TextView = itemView.findViewById(R.id.name)
        val date : TextView = itemView.findViewById(R.id.date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.workout_list_item, parent, false)
        return WorkoutViewHolder(view)
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        holder.workoutName.text = workouts[position].name
        holder.date.text = workouts[position].date

        holder.itemView.setOnClickListener{
            val context = holder.itemView.context
            val intent = Intent(context, WorkoutViewActivity::class.java)
            intent.putExtra("ID", workouts[position].id)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int = workouts.size
}
