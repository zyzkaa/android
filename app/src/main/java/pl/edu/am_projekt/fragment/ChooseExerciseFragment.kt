package pl.edu.am_projekt.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pl.edu.am_projekt.ExerciseAdapter
import pl.edu.am_projekt.R
import pl.edu.am_projekt.room.Exercise

class ChooseExerciseFragment(private val onExerciseSelected: (Exercise) -> Unit)
    : Fragment() {

    private lateinit var adapter: ExerciseAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.choose_exercise_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recycler = view.findViewById<RecyclerView>(R.id.exerciseRecyclerView)

//        adapter = ExerciseAdapter { exercise ->
////            onExerciseSelected(exercise)
////            parentFragmentManager.popBackStack() // wróć do poprzedniego widoku
//        }

        recycler.layoutManager = LinearLayoutManager(requireContext())
//        recycler.adapter = adapter

        // Przykładowe dane – później z Room/API
//        val exercises = listOf(
//            Exercise(1, "Squat"), Exercise(2, "Push-up"), Exercise(3, "Deadlift")
//        )
//        adapter.submitList(exercises)
    }
}