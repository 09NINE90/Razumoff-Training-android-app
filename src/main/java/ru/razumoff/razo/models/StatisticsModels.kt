package ru.razumoff.razo.models

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ru.razumoff.razo.R

enum class StatisticsPeriod {
    ALL_TIME,
    THIS_MONTH
}

@Composable
fun StatisticsPeriod.localizedName(): String {
    return when (this) {
        StatisticsPeriod.ALL_TIME ->
            stringResource(R.string.statistics_period_all_time)

        StatisticsPeriod.THIS_MONTH ->
            stringResource(R.string.statistics_period_this_month)
    }
}

enum class StatisticsWorkoutType(
    val workoutType: WorkoutType?
) {
    ALL(null),
    REGULAR(WorkoutType.REGULAR),
    STRENGTH(WorkoutType.STRENGTH);

    fun toWorkoutType(): String? {
        return when (this) {
            ALL -> null
            REGULAR -> WorkoutType.REGULAR.name
            STRENGTH -> WorkoutType.STRENGTH.name
        }
    }
}

@Composable
fun StatisticsWorkoutType.localizedName(): String {
    return when (this) {
        StatisticsWorkoutType.ALL ->
            stringResource(R.string.statistics_workout_type_all)

        StatisticsWorkoutType.REGULAR ->
            stringResource(R.string.regular_workout)

        StatisticsWorkoutType.STRENGTH ->
            stringResource(R.string.strength_workout)
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