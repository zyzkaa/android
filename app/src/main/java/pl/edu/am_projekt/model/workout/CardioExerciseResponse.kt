package pl.edu.am_projekt.model.workout

import pl.edu.am_projekt.model.BasicDictResponse

data class CardioExerciseResponse(
    var cardioExercise: BasicDictResponse,
    var params: List<CardioExerciseParamsResponse>,
    var calories: Double,
    var time: String
) : ExerciseResponse()
