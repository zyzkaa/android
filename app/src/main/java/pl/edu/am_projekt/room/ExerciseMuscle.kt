package pl.edu.am_projekt.room

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "exercise_muscles",
    primaryKeys = ["exerciseId", "muscleId"],
    foreignKeys = [
        ForeignKey(
            entity = Exercise::class,
            parentColumns = ["id"],
            childColumns = ["exerciseId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Muscle::class,
            parentColumns = ["id"],
            childColumns = ["muscleId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ExerciseMuscle(
    val exerciseId: Int,
    val muscleId: Int
)
