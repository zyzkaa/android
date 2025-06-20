package pl.edu.am_projekt.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.edu.am_projekt.R
import pl.edu.am_projekt.model.parcelable.Product

class ProductAdapter(
    private var products: MutableList<Product>,
    private val onProductClick: (Int, String) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name = view.findViewById<TextView>(R.id.productNameTextView)
        val calories = view.findViewById<TextView>(R.id.caloriesTextView)
        val protein = view.findViewById<TextView>(R.id.proteinTextView)
        val carbs = view.findViewById<TextView>(R.id.carbsTextView)
        val fat = view.findViewById<TextView>(R.id.fatTextView)

        init {
            view.setOnClickListener {
                val product = products[adapterPosition]
                onProductClick(product.id, product.name)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.name.text = product.name
        holder.calories.text = "Kcal: ${product.caloriesPer100g}"
        holder.protein.text = "Biał.: ${product.proteinPer100g}g"
        holder.carbs.text = "Węgl.: ${product.carbsPer100g}g"
        holder.fat.text = "Tłusz.: ${product.fatPer100g}g"
    }

    override fun getItemCount(): Int = products.size

    fun updateData(newProducts: List<Product>) {
        products.clear()
        products.addAll(newProducts)
        notifyDataSetChanged()
    }
}


