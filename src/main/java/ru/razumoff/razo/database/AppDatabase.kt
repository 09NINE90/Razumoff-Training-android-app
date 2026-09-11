package ru.razumoff.razo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.razumoff.razo.database.daos.*
import ru.razumoff.razo.database.entities.*

@Database(
    entities = [
        UserEntity::class,
        ExerciseEntity::class,
        WorkoutTemplateEntity::class,
        TemplateExerciseEntity::class,
        WorkoutSessionEntity::class,
        SessionExerciseEntity::class,
        WorkoutSetEntity::class,
        UserMeasurementEntity::class
    ],
    version = 8,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun exerciseDao(): ExerciseDao
    abstract fun workoutTemplateDao(): WorkoutTemplateDao
    abstract fun templateExerciseDao(): TemplateExerciseDao
    abstract fun workoutSessionDao(): WorkoutSessionDao      
    abstract fun sessionExerciseDao(): SessionExerciseDao    
    abstract fun workoutSetDao(): WorkoutSetDao
    abstract fun userMeasurementDao(): UserMeasurementDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "gym_diary_database"
                )
                    .addMigrations(
                        MIGRATION_1_2,
                        MIGRATION_2_3,
                        MIGRATION_3_4,
                        MIGRATION_4_5,
                        MIGRATION_5_6,
                        MIGRATION_6_7,
                        MIGRATION_7_8
                    )
                    .fallbackToDestructiveMigration(false)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}