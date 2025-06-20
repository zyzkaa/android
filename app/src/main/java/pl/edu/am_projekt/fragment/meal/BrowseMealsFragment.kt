package pl.edu.am_projekt.fragment.meal

import MealResponseDAO
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import pl.edu.am_projekt.R
import pl.edu.am_projekt.activity.MealActivity
import pl.edu.am_projekt.adapter.MealCardAdapter
import pl.edu.am_projekt.databinding.MealsStartLayoutBinding
import pl.edu.am_projekt.model.parcelable.Ingredient
import pl.edu.am_projekt.model.parcelable.Nutrient
import pl.edu.am_projekt.network.ApiService
import pl.edu.am_projekt.network.RetrofitClient

class BrowseMealsFragment : Fragment() {

    private lateinit var binding: MealsStartLayoutBinding
    private lateinit var apiService: ApiService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MealsStartLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        apiService = RetrofitClient.retrofit.create(ApiService::class.java)

        // LayoutManagers
        binding.section1Recycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.section2Recycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.section3Recycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.section4Recycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        // API Calls
        viewLifecycleOwner.lifecycleScope.launch {
            populateSection(binding.section1Recycler, getAllMeals())
            populateSection(binding.section2Recycler, getRecentMeals())
            populateSection(binding.section3Recycler, getMyMeals())
            populateSection(binding.section4Recycler, getMyEditedMeals())

        }

        // Camera Button
//        binding.openCameraButton.setOnClickListener {
//            val intent = Intent(requireContext(), CameraActivity::class.java)
//            startActivity(intent)
//        }
    }

    private fun populateSection(recyclerView: RecyclerView, items: List<MealResponseDAO>) {
        val adapter = MealCardAdapter(items) { meal ->
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
                    R.anim.enter_from_right,  // enter
                    R.anim.exit_to_left,      // exit
                    R.anim.enter_from_left,   // popEnter (back stack)
                    R.anim.exit_to_right      // popExit (back stack)
                )
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit()

        }

        recyclerView.adapter = adapter
    }

    // Suspend methods
    private suspend fun getAllMeals(): List<MealResponseDAO> = try {
        apiService.getAllMeals()
    } catch (e: Exception) {
        Log.e("MealsDebug", "Error: ${e.localizedMessage}")
        emptyList()
    }

    private suspend fun getRecentMeals(): List<MealResponseDAO> = try {
        apiService.getRecentMeals(10)
    } catch (e: Exception) {
        Log.e("MealsDebug", "Error: ${e.localizedMessage}")
        emptyList()
    }

    private suspend fun getMyMeals(): List<MealResponseDAO> = try {
        apiService.getMyMeals()
    } catch (e: Exception) {
        Log.e("MealsDebug", "Error: ${e.localizedMessage}")
        emptyList()
    }
    private suspend fun getMyEditedMeals(): List<MealResponseDAO> = try {
        apiService.getMyEditedMeals()
    } catch (e: Exception) {
        Log.e("MealsDebug", "Error: ${e.localizedMessage}")
        emptyList()
    }
    override fun onResume() {
        super.onResume()
        (activity as? MealActivity)?.showMealPlanFab(true)
        (activity as? MealActivity)?.showListFab(true)

    }
}
