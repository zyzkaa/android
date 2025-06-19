package pl.edu.am_projekt.activity

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.badge.ExperimentalBadgeUtils
import com.google.android.material.floatingactionbutton.FloatingActionButton
import pl.edu.am_projekt.R
import pl.edu.am_projekt.fragment.BrowseMealsFragment
import pl.edu.am_projekt.manager.SelectedMealsManager
import pl.edu.am_projekt.ui.SelectedMealsBottomSheet
import java.util.Locale

class MealActivity : AppCompatActivity() {

    private lateinit var fab: FloatingActionButton


    @OptIn(ExperimentalBadgeUtils::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meal)
        val locale = Locale("pl")
        Locale.setDefault(locale)

        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)


        fab = findViewById(R.id.globalFab)
        fab.setOnClickListener {
            Toast.makeText(this, "Global FAB clicked", Toast.LENGTH_SHORT).show()
        }

        // Load initial fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, BrowseMealsFragment())

            .commit()

        MealBadgeManager.attachBadge(fab)
        MealBadgeManager.setMealCount(SelectedMealsManager.getMeals().size)




        fab.setOnClickListener {
            val bottomSheet = SelectedMealsBottomSheet().apply {
                setMeals(SelectedMealsManager.getMeals())
            }
            bottomSheet.show(supportFragmentManager, "SelectedMeals")
        }


    }


}
