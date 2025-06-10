package pl.edu.am_projekt.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Exercise::class, Muscle::class, ExerciseMuscle::class],
    version = 1,
    exportSchema = false
)
abstract class ExerciseDatabase : RoomDatabase() {
    abstract fun exerciseDao(): ExerciseDao
    abstract fun muscleDao(): MuscleDao

    companion object {
        @Volatile
        private var INSTANCE: ExerciseDatabase? = null

        fun getDatabase(context: Context): ExerciseDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ExerciseDatabase::class.java,
                    "exercise_database"
                )
                    .fallbackToDestructiveMigration() // Only for development
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}