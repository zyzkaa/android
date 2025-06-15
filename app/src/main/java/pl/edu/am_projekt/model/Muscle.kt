package pl.edu.am_projekt.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "muscles")
data class Muscle(
    @PrimaryKey val id: Int,
    val name: String,
)
