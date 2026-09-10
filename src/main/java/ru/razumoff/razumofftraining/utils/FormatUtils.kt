package ru.razumoff.razumofftraining.utils

import android.annotation.SuppressLint
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

object FormatUtils {

    /**
     * Форматирует число с разделителями тысяч (пробел)
     * Пример: 10000 -> "10 000"
     */
    fun formatNumberWithSpaces(number: Number): String {
        return try {
            val symbols = DecimalFormatSymbols(Locale.getDefault()).apply {
                groupingSeparator = ' ' // Устанавливаем пробел как разделитель
            }
            val format = DecimalFormat("#,###", symbols)
            format.format(number)
        } catch (_: Exception) {
            number.toString()
        }
    }

    /**
     * Форматирует число с разделителями тысяч (пробел) для Int
     */
    fun Int.formatWithSpaces(): String = formatNumberWithSpaces(this)

    /**
     * Форматирует число с разделителями тысяч (пробел) для Long
     */
    fun Long.formatWithSpaces(): String = formatNumberWithSpaces(this)

    // --- Упражнения ---
    fun pluralizeExercise(count: Int): String {
        return when {
            count % 10 == 1 && count % 100 != 11 -> "упражнение"
            count % 10 in 2..4 && count % 100 !in 12..14 -> "упражнения"
            else -> "упражнений"
        }
    }

    fun Int.exercises(): String {
        return "$this ${pluralizeExercise(this)}"
    }

    // --- Шаблоны ---
    fun pluralizeTemplate(count: Int): String {
        return when {
            count % 10 == 1 && count % 100 != 11 -> "шаблон"
            count % 10 in 2..4 && count % 100 !in 12..14 -> "шаблона"
            else -> "шаблонов"
        }
    }

    fun Int.templates(): String {
        return "$this ${pluralizeTemplate(this)}"
    }

    // --- Подходы ---
    fun pluralizeSet(count: Int): String {
        return when {
            count % 10 == 1 && count % 100 != 11 -> "подход"
            count % 10 in 2..4 && count % 100 !in 12..14 -> "подхода"
            else -> "подходов"
        }
    }

    fun Int.sets(): String {
        return "$this ${pluralizeSet(this)}"
    }

    // --- Тренировки ---
    fun pluralizeWorkout(count: Int): String {
        return when {
            count % 10 == 1 && count % 100 != 11 -> "тренировка"
            count % 10 in 2..4 && count % 100 !in 12..14 -> "тренировки"
            else -> "тренировок"
        }
    }

    fun Int.workouts(): String {
        return "$this ${pluralizeWorkout(this)}"
    }

    // --- Годы (для возраста) ---
    fun pluralizeYears(count: Int): String {
        return when {
            count % 10 == 1 && count % 100 != 11 -> "год"
            count % 10 in 2..4 && count % 100 !in 12..14 -> "года"
            else -> "лет"
        }
    }

    fun Int.years(): String {
        return "$this ${pluralizeYears(this)}"
    }

    @SuppressLint("DefaultLocale")
    fun formatVolume(volume: Double): String {
        return if (volume >= 1000) {
            String.format(
                "%.1f т",
                volume / 1000
            )
        } else {
            String.format(
                "%.0f кг",
                volume
            )
        }
    }
}