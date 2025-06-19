package pl.edu.am_projekt.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import pl.edu.am_projekt.R
import pl.edu.am_projekt.databinding.ItemSelectedMealBinding
import pl.edu.am_projekt.model.MealSummary

class MealSummaryAdapter(
    private val meals: MutableList<MealSummary>,
    private val onClick: (MealSummary) -> Unit,
    private val onRemove: (MealSummary) -> Unit
) : RecyclerView.Adapter<MealSummaryAdapter.MealViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val binding = ItemSelectedMealBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MealViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        holder.bind(meals[position])
    }

    override fun getItemCount(): Int = meals.size

    fun updateData(newList: List<MealSummary>) {
        meals.clear()
        meals.addAll(newList)
        notifyDataSetChanged()
    }


    inner class MealViewHolder(private val binding: ItemSelectedMealBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(meal: MealSummary) {
            binding.mealName.text = meal.name
            binding.mealCalories.text = "${meal.calories} kcal"

            Glide.with(binding.mealImage.context)
                .load(meal.imageUrl)
                .placeholder(R.drawable.no_meal)
                .into(binding.mealImage)

            // Item click
            binding.root.setOnClickListener {
                onClick(meal)
            }

            // Remove button logic
            binding.removeButton.setOnClickListener {
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    val removed = meals.removeAt(pos)
                    notifyItemRemoved(pos)
                    onRemove(removed)
                }
            }
        }
    }
}
