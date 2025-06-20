package pl.edu.am_projekt.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import pl.edu.am_projekt.R
import pl.edu.am_projekt.activity.MainActivity
import pl.edu.am_projekt.activity.MealActivity
import pl.edu.am_projekt.adapter.MealSummaryAdapter
import pl.edu.am_projekt.databinding.FragmentSelectedMealsBottomsheetBinding
import pl.edu.am_projekt.fragment.meal.MealPlanFragment
import pl.edu.am_projekt.manager.SelectedMealsManager
import pl.edu.am_projekt.model.MealSummary

class SelectedMealsBottomSheet : BottomSheetDialogFragment() {

    private var _binding: FragmentSelectedMealsBottomsheetBinding? = null
    private val binding get() = _binding!!

    private var selectedMeals: List<MealSummary> = emptyList()

    fun setMeals(meals: List<MealSummary>) {
        this.selectedMeals = meals
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectedMealsBottomsheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = MealSummaryAdapter(
            selectedMeals.toMutableList(),
            onClick = {},
            onRemove = { meal ->
                SelectedMealsManager.removeMeal(meal.id, requireContext())

                val updatedMeals = SelectedMealsManager.getMeals()
                (binding.recyclerView.adapter as MealSummaryAdapter).updateData(updatedMeals)

                if (updatedMeals.isEmpty()) {
                    binding.noMealsText.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.GONE
                }
            }
        )

        if (selectedMeals.isEmpty()) {
            binding.noMealsText.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.GONE
        } else {
            binding.noMealsText.visibility = View.GONE
            binding.recyclerView.apply {
                visibility = View.VISIBLE
                layoutManager = LinearLayoutManager(requireContext())
                this.adapter = adapter
            }
        }
        binding.goToPlanButton.setOnClickListener {
            val fragment = MealPlanFragment()

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

            dismiss()
        }


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
