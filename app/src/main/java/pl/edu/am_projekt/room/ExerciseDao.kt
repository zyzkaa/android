package pl.edu.am_projekt.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface ExerciseDao {
    @Transaction
    @Query("SELECT * FROM exercises")
    suspend fun getAllExercises(): List<ExerciseWithMuscle>

    @Transaction
    @Query("SELECT * FROM exercises WHERE id = :exerciseId")
    suspend fun getExerciseById(exerciseId: Int): ExerciseWithMuscle?

    @Transaction
    @Query("SELECT * FROM exercises WHERE name LIKE '%' || :name || '%'")
    suspend fun searchExercisesByName(name: String): List<ExerciseWithMuscle>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercise(exercise: Exercise)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercises(exercises: List<Exercise>)
//
//    @Update
//    suspend fun updateExercise(exercise: Exercise)
//
//    @Delete
//    suspend fun deleteExercise(exercise: Exercise)
//
//    @Query("DELETE FROM exercises")
//    suspend fun deleteAllExercises()
}