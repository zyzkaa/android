package pl.edu.am_projekt.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercises")
data class Exercise(
    @PrimaryKey val id: Int,
    val name: String,
    val muscles: List<Muscle>?,
    val type: ExerciseType
)
