package pl.edu.am_projekt.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.launch
import pl.edu.am_projekt.adapter.MealSummaryAdapter
import pl.edu.am_projekt.databinding.FragmentMealPlanBinding
import pl.edu.am_projekt.manager.SelectedMealsManager
import pl.edu.am_projekt.model.CreateMealPlanDto
import pl.edu.am_projekt.model.MealSummary
import pl.edu.am_projekt.network.ApiService
import pl.edu.am_projekt.network.RetrofitClient
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import kotlin.time.Duration.Companion.days

class MealPlanFragment : Fragment() {

    private var _binding: FragmentMealPlanBinding? = null
    private val binding get() = _binding!!
    private lateinit var apiService: ApiService


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMealPlanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        apiService = RetrofitClient.retrofit.create(ApiService::class.java)



        val adapter = MealSummaryAdapter(
            SelectedMealsManager.getMeals().toMutableList(),
            onClick = {},
            onRemove = { meal ->
                SelectedMealsManager.removeMeal(meal.id, requireContext())

                val updatedMeals = SelectedMealsManager.getMeals()
                (binding.selectedMealsRecycler.adapter as MealSummaryAdapter).updateData(updatedMeals)

                if (updatedMeals.isEmpty()) {
                    binding.noMealsText.visibility = View.VISIBLE
                    binding.selectedMealsRecycler.visibility = View.GONE
                }
            }
        )
        if (SelectedMealsManager.getMeals().isEmpty()) {
            binding.noMealsText.visibility = View.VISIBLE
            binding.selectedMealsRecycler.visibility = View.GONE
        } else {
            binding.noMealsText.visibility = View.GONE
            binding.selectedMealsRecycler.apply {
                visibility = View.VISIBLE
                layoutManager = LinearLayoutManager(requireContext())
                this.adapter = adapter
            }
        }

        var selectedDate : String = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            // Create LocalDateTime from selected date (hour/minute = 0)
            val dateTime = LocalDateTime.of(year, month + 1, dayOfMonth, 0, 0)

            // Format to ISO 8601 string (C# DateTime-compatible)
            val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
            selectedDate = dateTime.format(formatter)  // → "2025-06-20T00:00:00"
        }

        binding.createMealPlan.setOnClickListener {
            if (SelectedMealsManager.getMeals().isEmpty()){
                Toast.makeText(requireContext(), "Dodaj posiłki do planu!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val mealIds = SelectedMealsManager.getSelectedMealIds()

            val dto = CreateMealPlanDto(
                date = selectedDate,
                mealsID = mealIds
            )

            lifecycleScope.launch {
                try {
                    val response = apiService.addMealPlan(dto)
                    if (response.success) {
                        Toast.makeText(requireContext(), "Plan zapisany!", Toast.LENGTH_SHORT).show()
                        cleanUpAfterAdding()
                    } else {
                        Toast.makeText(requireContext(), "Błąd podczas dodawania}", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Wyjątek: ${e.message}", Toast.LENGTH_LONG).show()
                    Log.e("MealPlan", "Error", e)
                }
            }
        }



    }

    fun cleanUpAfterAdding(){
        SelectedMealsManager.clear()
        parentFragmentManager.popBackStack()
    }
}
