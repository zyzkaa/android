package pl.edu.am_projekt

import pl.edu.am_projekt.room.Exercise

data class ExerciseWithTackData(
    val exercise: Exercise,
    val data : MutableList<InputData>
)
