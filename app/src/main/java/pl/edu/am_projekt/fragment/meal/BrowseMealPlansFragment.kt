package pl.edu.am_projekt.fragment.meal

import MealResponseDAO
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import pl.edu.am_projekt.R
import pl.edu.am_projekt.activity.MealActivity
import pl.edu.am_projekt.adapter.MealSummaryAdapter
import pl.edu.am_projekt.databinding.FragmentBrowseMealPlanBinding
import pl.edu.am_projekt.model.MealSummary
import pl.edu.am_projekt.model.parcelable.Ingredient
import pl.edu.am_projekt.model.parcelable.Nutrient
import pl.edu.am_projekt.network.ApiService
import pl.edu.am_projekt.network.RetrofitClient
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class BrowseMealPlansFragment : Fragment() {

    private var _binding: FragmentBrowseMealPlanBinding? = null
    private val binding get() = _binding!!
    private lateinit var apiService: ApiService
    private lateinit var adapter: MealSummaryAdapter
    private var selectedDate: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBrowseMealPlanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        apiService = RetrofitClient.retrofit.create(ApiService::class.java)

        adapter = MealSummaryAdapter(
            mutableListOf(),
            onClick = { meal -> fetchMealDetailsAndOpen(meal.id) },
            showRemoveButton = false
        )

        binding.plannedMealsRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.plannedMealsRecycler.adapter = adapter

        selectedDate = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE)

        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val date = LocalDate.of(year, month + 1, dayOfMonth)
            selectedDate = date.format(DateTimeFormatter.ISO_LOCAL_DATE)
            fetchMealPlansForSelectedDate()
        }

        fetchMealPlansForSelectedDate()
    }

    private fun fetchMealPlansForSelectedDate() {
        lifecycleScope.launch {
            try {
                val response = apiService.getMealPlansForDate(selectedDate)
                if (response.isSuccessful && response.body() != null) {
                    val mealPlans = response.body()!!

                    if (mealPlans.isEmpty()) {
                        binding.noMealsText.visibility = View.VISIBLE
                        binding.plannedMealsRecycler.visibility = View.GONE
                    } else {
                        val allMeals = mealPlans.flatMap { it.meals }

                        adapter.updateData(allMeals.map {
                            MealSummary(
                                id = it.id,
                                name = it.name,
                                calories = it.calories,
                                imageUrl = it.imageURL
                            )
                        })

                        binding.noMealsText.visibility = View.GONE
                        binding.plannedMealsRecycler.visibility = View.VISIBLE
                    }
                } else {
                    Toast.makeText(requireContext(), "Błąd serwera", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("MealPlanView", "Błąd połączenia", e)
                Toast.makeText(requireContext(), "Wyjątek: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchMealDetailsAndOpen(mealId: Int) {
        lifecycleScope.launch {
            try {
                val meal: MealResponseDAO = apiService.getMealById(mealId)

                val ingredientsParcelable = ArrayList(
                    meal.ingredients.map { Ingredient(it.id, it.name, it.quantity) }
                )

                val nutrientsParcelable = arrayListOf(
                    Nutrient("Tłuszcze", meal.fat),
                    Nutrient("Węglowodany", meal.carbs),
                    Nutrient("Białko", meal.protein)
                )

                val fragment = MealDetailsFragment().apply {
                    arguments = Bundle().apply {
                        putInt("mealId", meal.id)
                        putInt("creatorID", meal.creatorID)
                        putString("mealName", meal.name)
                        putDouble("calories", meal.calories)
                        putString("description", meal.description ?: "")
                        putParcelableArrayList("nutrients", nutrientsParcelable)
                        putParcelableArrayList("ingredients", ingredientsParcelable)
                    }
                }

                parentFragmentManager.beginTransaction()
                    .setCustomAnimations(
                        R.anim.enter_from_right,
                        R.anim.exit_to_left,
                        R.anim.enter_from_left,
                        R.anim.exit_to_right
                    )
                    .replace(R.id.fragmentContainer, fragment)
                    .addToBackStack(null)
                    .commit()

            } catch (e: Exception) {
                Log.e("MealPlanDetailFetch", "Nie udało się pobrać szczegółów posiłku", e)
                Toast.makeText(requireContext(), "Błąd pobierania posiłku", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onResume() {
        super.onResume()
        (activity as? MealActivity)?.showMealPlanFab(false)
        (activity as? MealActivity)?.showListFab(false)

    }
}
