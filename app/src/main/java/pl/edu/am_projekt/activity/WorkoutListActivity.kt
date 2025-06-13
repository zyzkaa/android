package pl.edu.am_projekt.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import pl.edu.am_projekt.WorkoutAdapter
import pl.edu.am_projekt.fragment.WorkoutTrackFragment
import pl.edu.am_projekt.activity.WorkoutTrackActivity
import pl.edu.am_projekt.databinding.WorkoutListLayoutBinding
import pl.edu.am_projekt.model.workout.WorkoutShortResponse
import pl.edu.am_projekt.network.ApiService
import pl.edu.am_projekt.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WorkoutListActivity : AppCompatActivity() {
    private lateinit var binding : WorkoutListLayoutBinding
    private lateinit var apiService: ApiService

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

        getWorkouts()
    }

    private fun getWorkouts(){
        val call = apiService.getTrainings(1, 10)
        call.enqueue(object: Callback<List<WorkoutShortResponse>> {
            override fun onResponse(
                call: Call<List<WorkoutShortResponse>>,
                response: Response<List<WorkoutShortResponse>>
            ) {
                if(response.isSuccessful){
                    val workouts = response.body() ?: emptyList()
                    val adapter = WorkoutAdapter(workouts)
                    binding.workoutsRecyclerView.layoutManager = LinearLayoutManager(this@WorkoutListActivity)
                    binding.workoutsRecyclerView.adapter = adapter
                } else {
                    Log.d("HTTP", "Kod odpowiedzi: ${response.code()}")
                    Toast.makeText(this@WorkoutListActivity, "Error occurred", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<WorkoutShortResponse>>, t: Throwable) {
                Log.e("API_ERROR", "Failure: ${t.message}", t)
                Toast.makeText(this@WorkoutListActivity, "Connection error", Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        finish();
        return true;
    }
}