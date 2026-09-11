package ru.razumoff.razumofftraining.utils

import android.content.Context
import androidx.annotation.StringRes
import ru.razumoff.razumofftraining.R
import ru.razumoff.razumofftraining.models.WorkoutSession
import ru.razumoff.razumofftraining.utils.FormatUtils.formatTotalSessionWeight
import java.text.SimpleDateFormat
import java.util.Locale

class WorkoutFormatter(
    private val context: Context
) {

    private val dateFormat =
        SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

    private fun getString(@StringRes resId: Int): String {
        return context.getString(resId)
    }

    fun formatToMarkdown(session: WorkoutSession): String {
        val sb = StringBuilder()

        sb.append("# ${getString(R.string.workout)}: ${session.templateName}\n\n")

        sb.append(
            "**${getString(R.string.workout_start)}:** " +
                    "${dateFormat.format(session.date)}\n"
        )

        val roundedWeight = formatTotalSessionWeight(session)

        sb.append(
            "\n**${getString(R.string.total_weight_volume)}:** " +
                    "$roundedWeight ${getString(R.string.kg)}\n"
        )

        session.duration?.let {
            sb.append(
                "**${getString(R.string.duration)}:** " +
                        "$it ${getString(R.string.duration_minutes)}\n"
            )
        }

        session.feeling?.let {
            sb.append(
                "**${getString(R.string.feeling)}:** ⭐ $it/10\n"
            )
        }

        session.notes
            ?.takeIf { it.isNotEmpty() }
            ?.let {
                sb.append(
                    "**${getString(R.string.notes)}:** $it\n"
                )
            }

        sb.append("\n---\n\n")

        sb.append("## ${getString(R.string.exercises)}\n\n")

        session.exercises.forEachIndexed { index, exercise ->
            sb.append(
                "### ${index + 1}. ${exercise.exerciseName}\n\n"
            )

            if (exercise.sets.isEmpty()) {
                sb.append(
                    "_${getString(R.string.no_sets)}_\n\n"
                )
            } else {
                sb.append(
                    "| ${getString(R.string.sets)} | " +
                            "${getString(R.string.repetitions)} | " +
                            "${getString(R.string.weight)} |\n"
                )

                sb.append("|--------|------------|-----|\n")

                exercise.sets.forEach { set ->
                    val weightStr =
                        if (set.weight > 0) {
                            "${set.weight} ${getString(R.string.kg)}"
                        } else {
                            "—"
                        }

                    sb.append(
                        "| ${set.setNumber} | " +
                                "${set.reps} | " +
                                "$weightStr |\n"
                    )
                }

                sb.append("\n")
            }
        }

        val totalSets =
            session.exercises.sumOf { it.sets.size }

        val totalExercises =
            session.exercises.size

        sb.append("---\n\n")

        sb.append(
            "**${getString(R.string.total)}:** " +
                    "$totalExercises ${getString(R.string.exercises)}, " +
                    "$totalSets ${getString(R.string.sets)}\n"
        )

        sb.append(
            "\n*${getString(R.string.generated_by)}*\n"
        )

        return sb.toString()
    }

    fun formatToSimpleText(session: WorkoutSession): String {
        val sb = StringBuilder()

        val roundedWeight =
            formatTotalSessionWeight(session)

        sb.append(
            "${getString(R.string.workout)}: " +
                    "${session.templateName}\n"
        )

        sb.append(
            "${getString(R.string.workout_start)}: " +
                    "${dateFormat.format(session.date)}\n"
        )

        sb.append(
            "${getString(R.string.total_weight_volume)}: " +
                    "$roundedWeight ${getString(R.string.kg)}\n"
        )

        session.duration?.let {
            sb.append(
                "${getString(R.string.duration)}: " +
                        "$it ${getString(R.string.duration_minutes)}\n"
            )
        }

        sb.append("\n")

        session.exercises.forEach { exercise ->
            sb.append(
                "${exercise.order}. ${exercise.exerciseName}\n"
            )

            exercise.sets.forEach { set ->
                val weightStr =
                    if (set.weight > 0) {
                        "${set.weight} ${getString(R.string.kg)}"
                    } else {
                        getString(R.string.without_weight)
                    }

                val warmupStr =
                    if (set.isWarmup) {
                        " (${getString(R.string.warmup)})"
                    } else {
                        ""
                    }

                sb.append(
                    "  - ${getString(R.string.sets)} " +
                            "${set.setNumber}: " +
                            "${set.reps} x " +
                            "$weightStr$warmupStr\n"
                )
            }

            sb.append("\n")
        }

        val totalSets =
            session.exercises.sumOf { it.sets.size }

        val totalExercises =
            session.exercises.size

        sb.append(
            "\n${getString(R.string.total)}: " +
                    "$totalExercises ${getString(R.string.exercises)}, " +
                    "$totalSets ${getString(R.string.sets)}\n"
        )

        sb.append(
            "${getString(R.string.generated_by)}\n"
        )

        return sb.toString()
    }
}