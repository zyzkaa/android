package pl.edu.am_projekt.activity

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.chip.Chip

import pl.edu.am_projekt.R
import pl.edu.am_projekt.adapter.IngredientAdapter
import pl.edu.am_projekt.adapter.NutrientAdapter
import pl.edu.am_projekt.databinding.ActivityMealDetailsBinding
import pl.edu.am_projekt.model.IngredientReturnDto
import pl.edu.am_projekt.model.parcelable.Ingredient
import pl.edu.am_projekt.model.parcelable.Nutrient
import pl.edu.am_projekt.network.RetrofitClient

class MealDetailsActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMealDetailsBinding

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)




        val mealId = intent.getIntExtra("mealId",0)
        val mealName = intent.getStringExtra("mealName") ?: "Brak nazwy"
        val calories = intent.getStringExtra("calories") ?: "0"
        val description = intent.getStringExtra("description") ?: ""
//        val fat = intent.getStringExtra("fat") ?: "0"
//        val carbs = intent.getStringExtra("carbs") ?: "0"
//        val protein = intent.getStringExtra("protein") ?: "0"
        val nutrients = intent.getParcelableArrayListExtra("nutrients", Nutrient::class.java) ?: arrayListOf()
        val ingredients: ArrayList<Ingredient> = intent.getParcelableArrayListExtra("ingredients", Ingredient::class.java) ?: arrayListOf()

        val ingredientAdapter = IngredientAdapter(ingredients)
        binding.ingredientsRecycler.layoutManager = LinearLayoutManager(this)
        binding.ingredientsRecycler.adapter = ingredientAdapter

        val nutrientAdapter = NutrientAdapter(nutrients)
        binding.nutritionRecycler.layoutManager = LinearLayoutManager(this)
        binding.nutritionRecycler.adapter = nutrientAdapter


        binding.ingredientsRecycler.layoutManager = LinearLayoutManager(this)
        binding.ingredientsRecycler.adapter = IngredientAdapter(ingredients)

        binding.mealNameTextView.text = mealName
        binding.caloriesTextView.text = "$calories kcal (100g)"
        binding.mealDescription.text = description


        val imageUrl = "${RetrofitClient.BASE_URL}image/get_by_mealId/${mealId}"
        if (!imageUrl.isNullOrBlank()) {
            Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.no_meal)
                .into(binding.mealImageView)
        }


    }


}
