package pl.edu.am_projekt.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import pl.edu.am_projekt.R
import pl.edu.am_projekt.databinding.WorkoutTrackLayoutBinding
import pl.edu.am_projekt.fragment.ChooseExerciseFragment
import pl.edu.am_projekt.fragment.WorkoutTrackFragment

class WorkoutTrackActivity : AppCompatActivity() {
    private lateinit var binding : WorkoutTrackLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = WorkoutTrackLayoutBinding.inflate(layoutInflater)

        val fragment = WorkoutTrackFragment()

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentView, fragment) // R.id.fragment_container to np. FrameLayout
            .addToBackStack(null)
            .commit()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish();
        return true;
    }
}