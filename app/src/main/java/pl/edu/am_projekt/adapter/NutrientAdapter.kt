package pl.edu.am_projekt.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.edu.am_projekt.R
import pl.edu.am_projekt.model.parcelable.Nutrient

class NutrientAdapter(private val nutrients: List<Nutrient>) :
    RecyclerView.Adapter<NutrientAdapter.NutrientViewHolder>() {

    class NutrientViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.nutrientName)
        val amount: TextView = view.findViewById(R.id.quantity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NutrientViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_nutrient, parent, false)
        return NutrientViewHolder(view)
    }

    override fun onBindViewHolder(holder: NutrientViewHolder, position: Int) {
        val (name, amount) = nutrients[position]
        holder.name.text = name
        holder.amount.text = amount.toString()
    }

    override fun getItemCount(): Int = nutrients.size
}
