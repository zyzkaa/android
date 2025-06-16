package pl.edu.am_projekt.activity

import MealResponseDAO
import android.content.Intent
import android.os.Bundle
import android.util.Log

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import pl.edu.am_projekt.adapter.MealCardAdapter
import pl.edu.am_projekt.adapter.NutrientAdapter
import pl.edu.am_projekt.databinding.MealsStartLayoutBinding
import pl.edu.am_projekt.model.parcelable.Ingredient
import pl.edu.am_projekt.model.parcelable.Nutrient
import pl.edu.am_projekt.network.ApiService
import pl.edu.am_projekt.network.RetrofitClient

class MealsStartActivity : AppCompatActivity() {
    private lateinit var apiService : ApiService

    private lateinit var binding: MealsStartLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        apiService = RetrofitClient.retrofit.create(ApiService::class.java)
        super.onCreate(savedInstanceState)
        binding = MealsStartLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.section1Recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.section2Recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.section3Recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        lifecycleScope.launch {
            val meals = getAllMeals()
            populateSection(binding.section1Recycler, meals)
        }
        lifecycleScope.launch {
            val meals = getRecentMeals()
            populateSection(binding.section2Recycler, meals)
        }
        lifecycleScope.launch {
            val meals = getMyMeals()
            populateSection(binding.section3Recycler, meals)
        }
        binding.openCameraButton.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        }

    }

    private fun populateSection(recyclerView: RecyclerView, items: List<MealResponseDAO>) {
        val adapter = MealCardAdapter(items) { meal ->

            val ingredientsParcelable = ArrayList(
                meal.ingredients.map { I -> Ingredient(I.name, I.quantity) }
            )
            val nutrientsParcelable = ArrayList(listOf<Nutrient>(Nutrient("Tłuszcze", meal.fat),Nutrient("Węglowodany", meal.carbs),Nutrient("Białko", meal.protein)))

            val intent = Intent(this, MealDetailsActivity::class.java).apply {
                putExtra("mealId", meal.id)
                putExtra("mealName", meal.name)
                putExtra("calories", meal.calories.toString())
                putExtra("description", meal.description ?: "")
                putParcelableArrayListExtra("nutrients", nutrientsParcelable)
                putParcelableArrayListExtra("ingredients", ingredientsParcelable)
            }

            startActivity(intent)
        }

        recyclerView.adapter = adapter
    }





    private suspend fun getAllMeals(): List<MealResponseDAO> {
        return try {
            val response = apiService.getAllMeals()  // suspend function
            Log.d("MealsDebug", "Fetched ${response.size} meals")
            response
        } catch (e: Exception) {
            Log.e("MealsDebug", "Error fetching meals: ${e.localizedMessage}")
            emptyList()
        }
    }

    private suspend fun getRecentMeals(): List<MealResponseDAO> {
        return try {
            val response = apiService.getRecentMeals(10)  // suspend function
            Log.d("MealsDebug", "Fetched ${response.size} recent meals")
            response
        } catch (e: Exception) {
            Log.e("MealsDebug", "Error fetching recent meals: ${e.localizedMessage}")
            emptyList()
        }
    }

    private suspend fun getMyMeals(): List<MealResponseDAO> {
        return try {
            val response = apiService.getMyMeals()  // suspend function
            Log.d("MealsDebug", "Fetched ${response.size} recent meals")
            response
        } catch (e: Exception) {
            Log.e("MealsDebug", "Error fetching recent meals: ${e.localizedMessage}")
            emptyList()
        }
    }





}
