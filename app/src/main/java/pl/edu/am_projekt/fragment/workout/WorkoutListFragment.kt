package pl.edu.am_projekt.fragment.workout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import pl.edu.am_projekt.R
import pl.edu.am_projekt.adapter.WorkoutAdapter
import pl.edu.am_projekt.databinding.WorkoutListFragmentBinding
import pl.edu.am_projekt.network.ApiService
import pl.edu.am_projekt.network.RetrofitClient

class WorkoutListFragment : Fragment() {

    private var _binding: WorkoutListFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var apiService: ApiService
    private lateinit var adapter: WorkoutAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = WorkoutListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        apiService = RetrofitClient.retrofit.create(ApiService::class.java)

        binding.newWorkout.setOnClickListener {
            findNavController().navigate(R.id.action_workoutList_to_workoutTrack)
        }

        initializeWorkouts()
    }

    private fun onWorkoutChosen(id: Int){
        val action = WorkoutListFragmentDirections
            .actionWorkoutListToWorkoutView(id)
        findNavController().navigate(action)
    }

    private fun initializeWorkouts() {
        adapter = WorkoutAdapter(mutableListOf(), { id -> onWorkoutChosen(id) })
        binding.workoutsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.workoutsRecyclerView.adapter = adapter
        initializeSlideDelete()

        lifecycleScope.launch {
            getWorkouts()
        }
    }

    private suspend fun getWorkouts() {
        val workouts = apiService.getTrainings(1, 20)
        adapter.setWorkouts(workouts)
    }

    private fun initializeSlideDelete() {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val id = adapter.removeData(position)
                lifecycleScope.launch {
                    apiService.deleteWorkout(id)
                    getWorkouts()
                }
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.workoutsRecyclerView)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
