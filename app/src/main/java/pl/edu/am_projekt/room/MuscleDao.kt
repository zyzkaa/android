package pl.edu.am_projekt.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface MuscleDao {
    @Query("SELECT * FROM muscles")
    suspend fun getAllMuscles(): List<Muscle>

//    @Query("SELECT * FROM muscles WHERE id = :id")
//    suspend fun getMuscleById(id: Int): Muscle?

    @Transaction
    @Query("SELECT * FROM muscles WHERE id = :muscleId")
    suspend fun getMuscleWithExercises(muscleId: Int): MuscleWithExercise?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMuscle(muscle: Muscle)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMuscles(muscles: List<Muscle>)

//    @Update
//    suspend fun updateMuscle(muscle: Muscle)
//
//    @Delete
//    suspend fun deleteMuscle(muscle: Muscle)
}