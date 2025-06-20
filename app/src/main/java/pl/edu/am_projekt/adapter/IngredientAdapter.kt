
package pl.edu.am_projekt.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.edu.am_projekt.R
import pl.edu.am_projekt.model.parcelable.Ingredient

class IngredientAdapter(
    private var ingredients: MutableList<Ingredient>,
    private var isEditMode: Boolean = false,
    private val onDelete: (Ingredient) -> Unit,
    private val onQuantityChanged: (Ingredient, Int) -> Unit
) : RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder>() {

    fun setEditMode(enabled: Boolean) {
        isEditMode = enabled
        notifyDataSetChanged()
    }

    fun getEditModeStatus(): Boolean = isEditMode

    fun addIngredient(ingredient: Ingredient) {
        ingredients.add(ingredient)
        notifyItemInserted(ingredients.size - 1)
    }

    fun getIngredients(): MutableList<Ingredient> = ingredients

    inner class IngredientViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameText: TextView = view.findViewById(R.id.ingredientName)
        val quantityText: EditText = view.findViewById(R.id.quantity)
        val deleteButton: ImageButton = view.findViewById(R.id.removeButton)
        var currentWatcher: TextWatcher? = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ingredient, parent, false)
        return IngredientViewHolder(view)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val item = ingredients[position]

        holder.nameText.text = item.name
        holder.quantityText.setText(item.quantity.toString())
        holder.quantityText.isEnabled = isEditMode
        holder.deleteButton.visibility = if (isEditMode) View.VISIBLE else View.GONE

        holder.deleteButton.setOnClickListener {
            val pos = holder.adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                val removed = ingredients[pos]
                onDelete(removed)
                ingredients.removeAt(pos)
                notifyItemRemoved(pos)
            }
        }

        // Usuń stary watcher
        holder.currentWatcher?.let {
            holder.quantityText.removeTextChangedListener(it)
        }

        // Utwórz nowy watcher
        val watcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val newQty = s?.toString()?.toIntOrNull() ?: 0
                item.quantity = newQty
                onQuantityChanged(item, newQty)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        holder.quantityText.addTextChangedListener(watcher)
        holder.currentWatcher = watcher

        // Walidacja po utracie focusu
        holder.quantityText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val value = holder.quantityText.text.toString().toIntOrNull() ?: 0
                if (value <= 0) {
                    holder.quantityText.error = "Wprowadź poprawną ilość (> 0)"
                    holder.quantityText.requestFocus()
                }
            }
        }

        // Auto-focus na ostatnim dodanym
        if (position == ingredients.size - 1 && isEditMode) {
            holder.quantityText.requestFocus()
        }
    }

    override fun getItemCount(): Int = ingredients.size
}

