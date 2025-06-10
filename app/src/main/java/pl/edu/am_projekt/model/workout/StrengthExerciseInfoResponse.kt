package pl.edu.am_projekt.model.workout

import pl.edu.am_projekt.model.BasicDictResponse

data class StrengthExerciseInfoResponse(
    var muscles: List<BasicDictResponse>,
    var id: Int,
    var name: String
)
