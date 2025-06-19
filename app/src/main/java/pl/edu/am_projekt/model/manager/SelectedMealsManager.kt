package pl.edu.am_projekt.manager

import MealResponseDAO
import android.content.Context
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import pl.edu.am_projekt.model.MealSummary

object SelectedMealsManager {
    private val selectedMeals = mutableListOf<MealSummary>()

    fun addMeal(meal: MealSummary, context: Context) {
        if (selectedMeals.none { it.id == meal.id }) {
            selectedMeals.add(meal)
            MealBadgeManager.increment()
//            Toast.makeText(context, "Dodano posiłek do listy", Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(context, "Dodałeś już ten posiłek!", Toast.LENGTH_SHORT).show()
        }
    }

    fun removeMeal(mealId: Int, context: Context) {
        selectedMeals.removeAll { it.id == mealId }
//        Toast.makeText(context, "Usunięto posiłek", Toast.LENGTH_SHORT).show()
        MealBadgeManager.decrement()


    }

    fun getMeals(): List<MealSummary> = selectedMeals.toList()

    fun hasThisMeal(id : Int) : Boolean{
        return selectedMeals.any { it.id == id }
    }



    fun clear() {
        selectedMeals.clear()
    }

    fun getSelectedMealIds() : List<Int> {
        return getMeals().map { it.id }
    }
}
