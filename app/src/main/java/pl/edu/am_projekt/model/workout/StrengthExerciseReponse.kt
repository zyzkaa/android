package pl.edu.am_projekt.model.workout

data class StrengthExerciseReponse(
    var strengthExercise: StrengthExerciseInfoResponse,
    var params: List<StrengthExerciseParamsResponse>,
    var totalVolume: Int
) : ExerciseResponse()
