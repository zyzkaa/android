package pl.edu.am_projekt.room

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class ExerciseWithMuscle(
    @Embedded val exercise: Exercise,
    @Relation(
        parentColumn = "id",
        entityColumn = "muscleId",
        associateBy = Junction(
            ExerciseMuscle::class,
            parentColumn = "exerciseId",
            entityColumn = "muscleId"
        )
    )
    val muscles: List<Muscle>
)
