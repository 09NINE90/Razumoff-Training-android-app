package ru.razumoff.razumofftraining.models

enum class StatisticsPeriod(
    val displayName: String
) {
    ALL_TIME("За всё время"),
    THIS_MONTH("Этот месяц")
}

enum class StatisticsWorkoutType(
    val displayName: String,
    val workoutType: WorkoutType?
) {
    ALL("Все", null),
    REGULAR("Обычная", WorkoutType.REGULAR),
    STRENGTH("Силовая", WorkoutType.STRENGTH);

    fun toWorkoutType(): String? {
        return when (this) {
            ALL -> null
            REGULAR -> WorkoutType.REGULAR.name
            STRENGTH -> WorkoutType.STRENGTH.name
        }
    }
}

data class StatisticsSummary(
    val completedSessions: Int = 0,
    val totalSets: Int = 0,
    val totalReps: Int = 0,
    val totalVolume: Double = 0.0
)

data class ExerciseStatistics(
    val exerciseId: String,
    val exerciseName: String,
    val maxWeight: Float,
    val totalVolume: Double,
    val totalSets: Int,
    val totalReps: Int,
    val sessionsCount: Int
)

data class ExerciseStatisticsRow(
    val exerciseId: String,
    val exerciseName: String,
    val maxWeight: Float,
    val totalVolume: Double,
    val totalSets: Int,
    val totalReps: Int,
    val sessionsCount: Int
)

data class ExerciseProgressRow(
    val sessionId: String,
    val date: Long,
    val maxWeight: Float
)