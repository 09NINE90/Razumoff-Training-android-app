package ru.razumoff.razumofftraining.utils

import android.annotation.SuppressLint
import ru.razumoff.razumofftraining.models.WorkoutSession
import ru.razumoff.razumofftraining.utils.FormatUtils.exercises
import ru.razumoff.razumofftraining.utils.FormatUtils.sets
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.Locale

object WorkoutFormatter {

    @SuppressLint("ConstantLocale")
    private val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

    fun formatToMarkdown(session: WorkoutSession): String {
        val sb = StringBuilder()

        // Заголовок
        sb.append("# Тренировка: ${session.templateName}\n\n")

        // Информация о тренировке
        sb.append("**Начало тренировки:** ${dateFormat.format(session.date)}\n")

        val roundedWeight = formatTotalSessionWeight(session)

        sb.append("\n**Объем веса всех упражнений:** $roundedWeight кг\n")

        session.duration?.let {
            sb.append("**Длительность:** $it мин\n")
        }

        session.feeling?.let {
            sb.append("**Самочувствие:** ⭐ $it/10\n")
        }

        session.notes?.takeIf { it.isNotEmpty() }?.let {
            sb.append("**Заметки:** $it\n")
        }

        sb.append("\n---\n\n")

        // Упражнения
        sb.append("## Упражнения\n\n")

        session.exercises.forEachIndexed { index, exercise ->
            sb.append("### ${index + 1}. ${exercise.exerciseName}\n\n")

            if (exercise.sets.isEmpty()) {
                sb.append("_Нет подходов_\n\n")
            } else {
                // Таблица подходов
                sb.append("| Подход | Повторения | Вес |\n")
                sb.append("|--------|------------|-----|\n")

                exercise.sets.forEach { set ->
                    val weightStr = if (set.weight > 0) "${set.weight} кг" else "—"
                    sb.append("| ${set.setNumber} | ${set.reps} | $weightStr |\n")
                }
                sb.append("\n")
            }
        }

        // Итоги
        val totalSets = session.exercises.sumOf { it.sets.size }
        val totalExercises = session.exercises.size

        sb.append("---\n\n")
        sb.append("**Итого:** ${totalExercises.exercises()}, ${totalSets.sets()}\n")
        sb.append("\n*Сгенерировано RazumoffTraining*\n")

        return sb.toString()
    }

    fun formatToSimpleText(session: WorkoutSession): String {
        val sb = StringBuilder()
        val roundedWeight = formatTotalSessionWeight(session)

        sb.append("Тренировка: ${session.templateName}\n")
        sb.append("Начало тренировки: ${dateFormat.format(session.date)}\n")
        sb.append("Объем веса всех упражнений: $roundedWeight кг\n")
        session.duration?.let { sb.append("Длительность: $it мин\n") }
        sb.append("\n")

        session.exercises.forEach { exercise ->
            sb.append("${exercise.order}. ${exercise.exerciseName}\n")
            exercise.sets.forEach { set ->
                val weightStr = if (set.weight > 0) "${set.weight} кг" else "без веса"
                val warmupStr = if (set.isWarmup) " (разминочный)" else ""
                sb.append("  - Подход ${set.setNumber}: ${set.reps} x $weightStr$warmupStr\n")
            }
            sb.append("\n")
        }

        val totalSets = session.exercises.sumOf { it.sets.size }
        val totalExercises = session.exercises.size

        sb.append("\nИтого: ${totalExercises.exercises()}, ${totalSets.sets()}\n")
        sb.append("Сгенерировано RazumoffTraining\n")

        return sb.toString()
    }

    fun formatTotalSessionWeight(session: WorkoutSession): Float {
        var totalWeight = 0f
        session.exercises.forEach { exercise ->
            exercise.sets.forEach { set ->
                totalWeight += set.weight * set.reps
            }
        }
        return BigDecimal(totalWeight.toDouble())
            .setScale(2, RoundingMode.HALF_UP)
            .toFloat()
    }
}