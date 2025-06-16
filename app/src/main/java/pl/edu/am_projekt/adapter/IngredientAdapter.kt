package pl.edu.am_projekt.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.edu.am_projekt.R
import pl.edu.am_projekt.model.parcelable.Ingredient

class IngredientAdapter(private val ingredients: List<Ingredient>) :
    RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder>() {

    class IngredientViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameText: TextView = view.findViewById(R.id.heading)
        val quantityText: TextView = view.findViewById(R.id.supportText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ingredient, parent, false)
        return IngredientViewHolder(view)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val item = ingredients[position]
        holder.nameText.text = item.name
        holder.quantityText.text = "${item.quantity} g"
    }

    override fun getItemCount(): Int = ingredients.size
}
