package pl.edu.am_projekt.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import pl.edu.am_projekt.WorkoutAdapter
import pl.edu.am_projekt.databinding.WorkoutListLayoutBinding
import pl.edu.am_projekt.model.workout.response.WorkoutShortResponse
import pl.edu.am_projekt.network.ApiService
import pl.edu.am_projekt.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WorkoutListActivity : AppCompatActivity() {
    private lateinit var binding : WorkoutListLayoutBinding
    private lateinit var apiService: ApiService
    private lateinit var adapter: WorkoutAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        apiService = RetrofitClient.retrofit.create(ApiService::class.java)
        binding = WorkoutListLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.topAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        binding.newWorkout.setOnClickListener{
            val intent = Intent(this@WorkoutListActivity, WorkoutTrackActivity::class.java)
            startActivity(intent)
        }

        initializeWorkouts()
    }

    private fun initializeWorkouts(){
        adapter = WorkoutAdapter(mutableListOf())
        binding.workoutsRecyclerView.layoutManager = LinearLayoutManager(this@WorkoutListActivity)
        binding.workoutsRecyclerView.adapter = adapter
        initializeSlideDelete()
        lifecycleScope.launch {
            getWorkouts()
        }
    }

    private suspend fun getWorkouts(){
        val workouts = apiService.getTrainings(1, 20)
        adapter.setWorkouts(workouts)
    }

    private fun initializeSlideDelete(){
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

    override fun onSupportNavigateUp(): Boolean {
        finish();
        return true;
    }
}