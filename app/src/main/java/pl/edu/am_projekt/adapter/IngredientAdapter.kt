package pl.edu.am_projekt.adapter

import android.annotation.SuppressLint
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

    @SuppressLint("NotifyDataSetChanged")
    fun setEditMode(enabled: Boolean) {
        isEditMode = enabled
        notifyDataSetChanged()
    }
    fun getEditModeStatus(): Boolean {
        return isEditMode;
    }

    inner class IngredientViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameText: TextView = view.findViewById(R.id.ingredientName)
        val quantityText: EditText = view.findViewById(R.id.quantity)
        val deleteButton: ImageButton = view.findViewById(R.id.removeButton)
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
            val position = holder.adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val deleted = ingredients[position]
                onDelete(deleted)
                ingredients.removeAt(position)
                notifyItemRemoved(position)
            }
        }


        holder.quantityText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                    val newQtyString = s?.toString()?.trim() ?: return
                    val newQty = newQtyString.toIntOrNull() ?: 0
                    onQuantityChanged(item, newQty.toInt())

                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

    holder.quantityText.setOnFocusChangeListener { v, hasFocus ->
        if (!hasFocus) {
            val text = holder.quantityText.text.toString().trim()
            val value = text.toInt()

            if (value <= 0) {
                holder.quantityText.error = "Wprowadź poprawną ilość (> 0)"
                holder.quantityText.requestFocus()
            }
        }
    }





    }
    fun getIngredients() : MutableList<Ingredient>{
        return ingredients
    }

    override fun getItemCount(): Int = ingredients.size
}


