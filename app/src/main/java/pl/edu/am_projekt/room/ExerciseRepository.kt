package pl.edu.am_projekt.room

class ExerciseRepository(private val database: ExerciseDatabase) {
    private val exerciseDao = database.exerciseDao()
    private val muscleDao = database.muscleDao()

    suspend fun getAllExercises() = exerciseDao.getAllExercises()
    suspend fun getExercisesWithMuscles() = exerciseDao.getAllExercises()
    suspend fun getExerciseWithMuscles(id: Int) = exerciseDao.getExerciseById(id)
    suspend fun getAllMuscles() = muscleDao.getAllMuscles()

    suspend fun insertExerciseWithMuscles(
        exercise: Exercise,
        primaryMuscles: List<Int>,
    ) {
        exerciseDao.insertExercise(exercise)

        val exerciseMuscles = mutableListOf<ExerciseMuscle>()

        primaryMuscles.forEach { muscleId ->
            exerciseMuscles.add(ExerciseMuscle(exercise.id, muscleId))
        }
    }

    suspend fun insertMuscles(muscles: List<Muscle>) = muscleDao.insertMuscles(muscles)
    suspend fun insertExercises(exercises: List<Exercise>) = exerciseDao.insertExercises(exercises)

    suspend fun searchExercises(query: String) = exerciseDao.searchExercisesByName(query)
}