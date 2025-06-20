package pl.edu.am_projekt.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.badge.ExperimentalBadgeUtils
import com.google.android.material.floatingactionbutton.FloatingActionButton
import pl.edu.am_projekt.R
import pl.edu.am_projekt.fragment.meal.BrowseMealPlansFragment
import pl.edu.am_projekt.fragment.meal.BrowseMealsFragment
import pl.edu.am_projekt.manager.SelectedMealsManager
import pl.edu.am_projekt.ui.SelectedMealsBottomSheet

class MealActivity : AppCompatActivity() {

    private lateinit var listFab: FloatingActionButton
    private lateinit var backFab: FloatingActionButton
    private lateinit var mealPlanFab: FloatingActionButton

    @OptIn(ExperimentalBadgeUtils::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meal)
        listFab = findViewById(R.id.globalFab)
        listFab.setOnClickListener {
            Toast.makeText(this, "Global FAB clicked", Toast.LENGTH_SHORT).show()
        }

        // Load initial fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, BrowseMealsFragment())

            .commit()

        MealBadgeManager.attachBadge(listFab)
        MealBadgeManager.setMealCount(SelectedMealsManager.getMeals().size)

        backFab = findViewById(R.id.backFab)
        backFab.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        mealPlanFab = findViewById(R.id.mealPlanFab)
        mealPlanFab.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.enter_from_right,  // enter
                    R.anim.exit_to_left,      // exit
                    R.anim.enter_from_left,   // popEnter (back stack)
                    R.anim.exit_to_right      // popExit (back stack)
                )
                .replace(R.id.fragmentContainer, BrowseMealPlansFragment())
                .addToBackStack(null)
                .commit()
        }



        listFab.setOnClickListener {
            val bottomSheet = SelectedMealsBottomSheet().apply {
                setMeals(SelectedMealsManager.getMeals())
            }
            bottomSheet.show(supportFragmentManager, "SelectedMeals")
        }


    }

    fun showBackFab(show: Boolean) {
        backFab.visibility = if (show) View.VISIBLE else View.GONE
    }
    fun showListFab(show: Boolean) {
        listFab.visibility = if (show) View.VISIBLE else View.GONE
    }
    fun showMealPlanFab(show: Boolean) {
        mealPlanFab.visibility = if (show) View.VISIBLE else View.GONE
    }


}
