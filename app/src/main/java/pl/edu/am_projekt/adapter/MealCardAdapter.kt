package pl.edu.am_projekt.adapter

import MealResponseDAO
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import pl.edu.am_projekt.R
import pl.edu.am_projekt.network.RetrofitClient

class MealCardAdapter(
    private val meals: List<MealResponseDAO>,
    private val onClick: (MealResponseDAO) -> Unit
) : RecyclerView.Adapter<MealCardAdapter.MealViewHolder>() {

    inner class MealViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.mealName)
        val image: ImageView = view.findViewById(R.id.mealImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_box, parent, false)
        return MealViewHolder(view)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        val meal = meals[position]
        holder.name.text = meal.name

        val imageUrl = "${RetrofitClient.BASE_URL}image/get_by_mealId/${meal.id}"
        Glide.with(holder.image.context)
            .load(imageUrl)
            .placeholder(R.drawable.no_meal) // fallback
            .error(R.drawable.no_meal)       // error
            .into(holder.image)

        holder.view.setOnClickListener { onClick(meal) }
    }



    override fun getItemCount(): Int = meals.size
}
