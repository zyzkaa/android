package pl.edu.am_projekt.room

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class MuscleWithExercise(
    @Embedded val muscle: Muscle,
    @Relation(
        parentColumn = "id",
        entityColumn = "exerciseId",
        associateBy = Junction(
            ExerciseMuscle::class,
            parentColumn = "muscleId",
            entityColumn = "exerciseId"
        )
    )
    val exercises: List<Exercise>
)
