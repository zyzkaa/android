package pl.edu.am_projekt.fragment.workout

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.coroutines.launch
import pl.edu.am_projekt.R
import pl.edu.am_projekt.adapter.ExerciseAdapter
import pl.edu.am_projekt.databinding.ChooseExerciseFragmentBinding
import pl.edu.am_projekt.model.BasicDictResponse
import pl.edu.am_projekt.model.Exercise
import pl.edu.am_projekt.model.ExerciseType
import pl.edu.am_projekt.model.workout.response.BasicExerciseResponse
import pl.edu.am_projekt.model.workout.response.StrengthExerciseInfoResponse
import pl.edu.am_projekt.network.ApiService
import pl.edu.am_projekt.network.RetrofitClient

class ChooseExerciseFragment
    : Fragment() {

    private lateinit var adapter: ExerciseAdapter
    private val args: ChooseExerciseFragmentArgs by navArgs()
    private lateinit var apiService : ApiService
    private val currentMuscleId = MutableLiveData<Int?>()
    private val currentSearchInput = MutableLiveData<String?>()
    private lateinit var type: ExerciseType

    private lateinit var _binding : ChooseExerciseFragmentBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ChooseExerciseFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        apiService = RetrofitClient.retrofit.create(ApiService::class.java)
        type = ExerciseType.valueOf(args.exerciseType)

    }


    private fun initializeLayout() {
        if(type == ExerciseType.CARDIO) return
        addMuscleFilters()
        addSearchByMuscle()
    }

    private suspend fun fetchMuscles(): List<BasicDictResponse> {
        return apiService.getAllMuscles()
    }

    private fun addSearchByMuscle(){
        currentMuscleId.observe(viewLifecycleOwner) { id ->
            Log.d("currentMuscleName", "Nowy wybór: $id")
            searchExercises()
        }
    }

    private fun addSearchByInput(){
        currentSearchInput.observe(viewLifecycleOwner) {input ->
            Log.d("current search", "Nowy wybór: $input")
            searchExercises()
        }
    }

    private fun addMuscleFilters(){
        val chipGroup = ChipGroup(requireContext()).apply {
            id = View.generateViewId()
            isSingleSelection = true
            isSingleLine = true
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        chipGroup.setOnCheckedStateChangeListener{group, checkId ->
            if(checkId.isEmpty()){
                Log.d("current chip", "none")
                currentMuscleId.value = null
                return@setOnCheckedStateChangeListener
            }

            val checkedChip = group.findViewById<Chip>(checkId[0])
            currentMuscleId.value = checkedChip.tag as Int
            Log.d("current chip", checkedChip.tag.toString())
            searchExercises()
        }

        val horizontalScrollView = HorizontalScrollView(requireContext()).apply {
            id = View.generateViewId()
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            addView(chipGroup)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            val muscles = fetchMuscles()
            muscles.forEach { m ->
                val resId = requireContext().resources.getIdentifier(m.name, "string", requireContext().packageName)
                val muscleName = if (resId != 0) requireContext().getString(resId) else m.name
                val chip = Chip(requireContext()).apply {
                    text = muscleName
                    tag = m.id
                    isCheckable = true
                }
                chipGroup.addView(chip)
            }
        }

        val container = binding.container
        container.addView(horizontalScrollView)

        val set = ConstraintSet()
        set.clone(container)
        set.connect(
            horizontalScrollView.id, ConstraintSet.TOP,
            R.id.chipGroupAnchor, ConstraintSet.BOTTOM, 0
        )
        set.connect(
            horizontalScrollView.id, ConstraintSet.START,
            ConstraintSet.PARENT_ID, ConstraintSet.START
        )
        set.connect(
            horizontalScrollView.id, ConstraintSet.END,
            ConstraintSet.PARENT_ID, ConstraintSet.END
        )
        set.connect(
            binding.exerciseRecyclerView.id, ConstraintSet.TOP,
            horizontalScrollView.id, ConstraintSet.BOTTOM, 8
        )
        set.applyTo(container)
    }

    private fun initializeSearchBar(){
        binding.searchButton.setOnClickListener {
            val text = binding.searchEditText.text.toString()
            currentSearchInput.value = text
        }

        addSearchByInput()
    }

    private fun searchExercises(){
        Log.d("func", "search exercises")
        if (type == ExerciseType.CARDIO) searchCardioExercises()
        else searchStrengthExercises()
    }

    private fun searchCardioExercises(){
        Log.d("func", "search cardio exercises")
        val text = currentSearchInput.value
        viewLifecycleOwner.lifecycleScope.launch {
            val exercises = if(text.isNullOrEmpty()) fetchAllCardioExercises()
            else fetchCardioExercisesBySearch(text)

            exercises.forEach{e -> Log.d("exercise", e.namePl)}

//            val asd = exercises.map {
//                Exercise.fromBasicDictResponse(it, requireContext())
//            }
//
//            asd.forEach{
//                el -> Log.d("asd", el.name)
//            }

            adapter.updateData(exercises.map {
                Exercise.fromBasicDictResponse(it, requireContext())
            })
        }

    }

    private suspend fun fetchAllCardioExercises() : List<BasicExerciseResponse>{
        return apiService.getAllCardioExercises()
    }

    private suspend fun fetchCardioExercisesBySearch(text: String) : List<BasicExerciseResponse>{
        return apiService.getCardioExercisesBySearch(text)
    }

    private fun searchStrengthExercises(){
        val text = currentSearchInput.value
        val muscleId = currentMuscleId.value

        viewLifecycleOwner.lifecycleScope.launch {

            val exercises = when {
                text.isNullOrEmpty() && muscleId == null -> fetchRecentStrengthExercises()
                text.isNullOrEmpty() -> fetchStrExercisesByMuscle(muscleId!!)
                muscleId == null -> fetchStrExercisesBySearch(text)
                else -> fetchStrExercisesByMuscleAndSearch(muscleId, text)
            }

            adapter.updateData(exercises.map {
                Exercise.fromStrExercise(it, requireContext())
            })
        }

    }

    private suspend fun fetchStrExercisesByMuscle(id: Int) : List<StrengthExerciseInfoResponse>{
        return apiService.getExercisesByMuscle(id)
    }

    private suspend fun fetchStrExercisesBySearch(text: String) : List<StrengthExerciseInfoResponse>{
        return apiService.getStrExercisesBySearch(text)
    }

    private suspend fun fetchStrExercisesByMuscleAndSearch(id: Int, text: String) : List<StrengthExerciseInfoResponse>{
        return apiService.getStrExercisesByMuscleAndSearch(text, id)
    }

    private suspend fun fetchRecentStrengthExercises() : List<StrengthExerciseInfoResponse>{
         return apiService.getRecentStrengthExercises()
    }

    private fun initializeRecyclerView(){
        adapter = ExerciseAdapter(emptyList(), {
            selectedExercise ->
            val result = Bundle().apply {
                putParcelable("exercise", selectedExercise)
            }
            parentFragmentManager.setFragmentResult("exercise_key", result)
            parentFragmentManager.popBackStack()
        })

        binding.exerciseRecyclerView.adapter = adapter
        binding.exerciseRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeRecyclerView()
        initializeLayout()
        initializeSearchBar()
        currentSearchInput.value = null
        currentMuscleId.value = null
        searchExercises()
    }
}