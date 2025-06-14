package pl.edu.am_projekt.model.workout.request

import java.sql.Time

data class CardioParams(
    val interval: Int,
    val speed: Double,
    val time: String
)
