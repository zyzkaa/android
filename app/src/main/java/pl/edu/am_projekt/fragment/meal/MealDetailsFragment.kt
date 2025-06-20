package pl.edu.am_projekt.fragment.meal

import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import pl.edu.am_projekt.R
import pl.edu.am_projekt.activity.MealActivity
import pl.edu.am_projekt.adapter.IngredientAdapter
import pl.edu.am_projekt.adapter.NutrientAdapter
import pl.edu.am_projekt.databinding.ActivityMealDetailsBinding
import pl.edu.am_projekt.manager.SelectedMealsManager
import pl.edu.am_projekt.model.CreateMealDto
import pl.edu.am_projekt.model.CreateMealDto.MealIngredientDto
import pl.edu.am_projekt.model.MealSummary
import pl.edu.am_projekt.model.parcelable.Ingredient
import pl.edu.am_projekt.model.parcelable.Nutrient
import pl.edu.am_projekt.network.ApiService
import pl.edu.am_projekt.network.RetrofitClient

class MealDetailsFragment : Fragment() {

    private var _binding: ActivityMealDetailsBinding? = null
    private val binding get() = _binding!!
    private var isFabExpanded = false
    private var editWiggleAnimator: ObjectAnimator? = null

    private var originalMealDto: CreateMealDto? = null
    private var editedMealDto: CreateMealDto? = null

    private lateinit var apiService: ApiService


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ActivityMealDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        apiService = RetrofitClient.retrofit.create(ApiService::class.java)

        val mealId = arguments?.getInt("mealId") ?: 0
        val creatorId = arguments?.getInt("creatorID") ?: 0
        val mealName = arguments?.getString("mealName") ?: "Brak nazwy"
        val calories = arguments?.getDouble("calories") ?: 0.0
        val description = arguments?.getString("description") ?: ""
        val nutrients = arguments?.getParcelableArrayList<Nutrient>("nutrients") ?: arrayListOf()
        val ingredients = arguments?.getParcelableArrayList<Ingredient>("ingredients") ?: arrayListOf()
        val mealImageUrl = "${RetrofitClient.BASE_URL}image/get_by_mealId/$mealId"

        binding.root.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val focused = activity?.currentFocus
                if (focused != null) {
                    val outRect = Rect()
                    focused.getGlobalVisibleRect(outRect)
                    if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                        focused.clearFocus()
                        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                        imm?.hideSoftInputFromWindow(focused.windowToken, 0)

                        // Dodaj performClick dla zgodności
                        v.performClick()
                    }
                }
            }
            false
        }



        // Set up adapters
        binding.ingredientsRecycler.layoutManager = LinearLayoutManager(requireContext())
        val ingredientAdapter = IngredientAdapter(
            ingredients = ingredients.toMutableList(),
            onDelete = { },
            onQuantityChanged = { ingredient, newQty ->
                ingredient.quantity = newQty
            }
        )

        binding.ingredientsRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.ingredientsRecycler.adapter = ingredientAdapter

        parentFragmentManager.setFragmentResultListener("product_selected", this) { _, bundle ->
            val productId = bundle.getInt("selectedProductId")
            val productName = bundle.getString("selectedProductName") ?: return@setFragmentResultListener

            // Dodajemy nowy Ingredient z domyślną ilością np. 100g
            val newIngredient = Ingredient(productId, productName, 0)
            ingredientAdapter.addIngredient(newIngredient)
        }

        binding.editMealButton.setOnClickListener {
            val isEditing = ingredientAdapter.getEditModeStatus()

            if (!isEditing) {
                binding.addIngredientButton.visibility = View.VISIBLE
                // Clone original meal data for comparison
                val ingredients = ingredientAdapter.getIngredients().map {
                    MealIngredientDto(it.productID, it.quantity)
                }

                originalMealDto = CreateMealDto(
                    name = mealName,
                    description = description,
                    imageURL = mealImageUrl,
                    isShared = true,
                    creatorID = creatorId,
                    ingredients = ingredients,
                    originalMealID = mealId
                )

                // Editable copy
                editedMealDto = originalMealDto?.copy()

                ingredientAdapter.setEditMode(true)
                binding.mealNameTextView.isEnabled = true
                binding.mealNameTextView.requestFocus()
                startWiggleAnimation(binding.editMealButton)

            } else {
                binding.addIngredientButton.visibility = View.GONE
                binding.mealNameTextView.isEnabled = false
                // Editing -> user clicked again
                val newName = binding.mealNameTextView.text.toString().trim()
                if (newName.isBlank()) {
                    Toast.makeText(requireContext(), "Nazwa posiłku nie może być pusta!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if(ingredientAdapter.getIngredients().isEmpty()){
                    Toast.makeText(requireContext(), "Lista składników nie moze być pusta!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                val newIngredients = ingredientAdapter.getIngredients().map {
                    MealIngredientDto(it.productID, it.quantity)
                }

                for (ing in newIngredients) {
                    if (ing.quantity == 0){
                        Toast.makeText(requireContext(), "Uzupełnij ilości składników!", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }

                }


                editedMealDto = editedMealDto?.copy(
                    name = newName,
                    ingredients = newIngredients
                )

                if (originalMealDto != editedMealDto) {
                    editedMealDto = editedMealDto?.copy(
                        isShared = false
                    )
                    showSaveChangesDialog {
                        viewLifecycleOwner.lifecycleScope.launch {
                            addNewMeal(editedMealDto!!)
                            ingredientAdapter.setEditMode(false)
                            stopWiggleAnimation(binding.editMealButton)
                            editedMealDto = null
                        }
                    }
                } else {
                    // No changes → just exit edit mode
                    ingredientAdapter.setEditMode(false)
                    stopWiggleAnimation(binding.editMealButton)
                    editedMealDto = null
                }
            }
        }

        binding.addIngredientButton.setOnClickListener {
            val fragment = AddProductFragment()

            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.enter_from_right,
                    R.anim.exit_to_left,
                    R.anim.enter_from_left,
                    R.anim.exit_to_right
                )
                .add(R.id.fragmentContainer, fragment)
                .hide(this)
                .addToBackStack(null)
                .commit()

        }





        binding.nutritionRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.nutritionRecycler.adapter = NutrientAdapter(nutrients)

        // Set texts
        binding.mealNameTextView.setText(mealName);

        binding.caloriesTextView.text = "${calories.toString()} kcal"
        binding.mealDescription.text = description

        // Load image
        Glide.with(this)
            .load(mealImageUrl)
            .placeholder(R.drawable.no_meal)
            .into(binding.mealImageView)

        binding.addMealButton.setOnClickListener {
            binding.addMealButton.post {
                val fabWidth = binding.addMealButton.width
                val gap = 20f

                if (!isFabExpanded) {
                    // Expand
                    binding.confirmAddMealButton.visibility = View.VISIBLE

                    binding.confirmAddMealButton.animate()
                        .translationX(-1f * (fabWidth + gap))
                        .alpha(1f)
                        .setDuration(200)
                        .start()

                    binding.addMealButton.animate()
                        .rotation(-180f)
                        .setDuration(200)
                        .start()

                    isFabExpanded = true
                } else {
                    // Collapse
                    binding.confirmAddMealButton.animate()
                        .translationX(0f).alpha(0f).setDuration(200)
                        .withEndAction { binding.confirmAddMealButton.visibility = View.GONE }
                        .start()

                    binding.addMealButton.animate()
                        .rotation(180f)
                        .setDuration(200)
                        .start()
                    isFabExpanded = false
                }
            }
        }

        binding.confirmAddMealButton.setOnClickListener {
            if (ingredientAdapter.getEditModeStatus() || SelectedMealsManager.hasThisMeal(mealId)) {
                shakeView(binding.confirmAddMealButton)
            } else {
                val meal = MealSummary(
                    id = mealId,
                    name = mealName,
                    imageUrl = "${RetrofitClient.BASE_URL}image/get_by_mealId/$mealId",
                    calories = calories
                )
                SelectedMealsManager.addMeal(meal, requireContext())
                confirmAnimation()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private suspend fun addNewMeal(meal : CreateMealDto) = try {
        apiService.addNewMeal(meal)
        true;
    } catch (e: Exception) {
        Log.e("MealsDebug", "Error: ${e.localizedMessage}")
        false;
    }

    private fun startWiggleAnimation(view: View) {
        editWiggleAnimator = ObjectAnimator.ofFloat(view, View.ROTATION, -5f, 5f).apply {
            duration = 100
            repeatMode = ObjectAnimator.REVERSE
            repeatCount = ObjectAnimator.INFINITE
            start()
        }
    }
    private fun stopWiggleAnimation(view: View) {
        editWiggleAnimator?.cancel()
        view.rotation = 0f
    }
    private fun shakeView(view: View) {
        val startX = view.translationX
        view.animate().translationX(startX + 10f).setDuration(50).withEndAction {
            view.animate().translationX(startX - 10f).setDuration(50).withEndAction {
                view.animate().translationX(startX + 6f).setDuration(40).withEndAction {
                    view.animate().translationX(startX).setDuration(30).start()
                }.start()
            }.start()
        }.start()
    }
    private fun confirmAnimation(){
        binding.confirmAddMealButton.animate()
            .translationYBy(-30f)
            .setDuration(100)
            .withEndAction {
                binding.confirmAddMealButton.animate()
                    .rotationBy(15f)
                    .setDuration(50)
                    .withEndAction {
                        binding.confirmAddMealButton.animate()
                            .rotationBy(-30f)
                            .setDuration(50)
                            .withEndAction {
                                binding.confirmAddMealButton.animate()
                                    .rotationBy(15f)
                                    .setDuration(50)
                                    .withEndAction {
                                        binding.confirmAddMealButton.animate()
                                            .translationYBy(30f)
                                            .setDuration(100)
                                            .withEndAction {
                                                binding.addMealButton.performClick()
                                            }
                                            .start()
                                    }
                                    .start()
                            }
                            .start()
                    }
                    .start()
            }
            .start()
    }
    private fun showSaveChangesDialog(onConfirm: () -> Unit) {
        AlertDialog.Builder(requireContext())
            .setTitle("Nowy posiłek")
            .setMessage("Stworzysz nowy posiłek z wprowadzonymi zmianami. Chcesz kontynuować?")
            .setPositiveButton("Tak") { _, _ -> onConfirm() }
            .setNegativeButton("Nie", null)
            .show()
    }
    override fun onResume() {
        super.onResume()
        (activity as? MealActivity)?.showMealPlanFab(false)
        (activity as? MealActivity)?.showListFab(false)

    }

}
