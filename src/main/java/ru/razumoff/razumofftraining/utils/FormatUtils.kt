package ru.razumoff.razumofftraining.utils

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ru.razumoff.razumofftraining.R
import ru.razumoff.razumofftraining.models.WorkoutSession
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import kotlin.collections.forEach

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

    @Composable
    @SuppressLint("DefaultLocale")
    fun formatVolume(volume: Double): String {
        return if (volume >= 1000) {
            String.format(
                "%.1f ${stringResource(R.string.t)}",
                volume / 1000
            )
        } else {
            String.format(
                "%.0f ${stringResource(R.string.kg)}",
                volume
            )
        }
    }

    fun formatTotalSessionWeight(
        session: WorkoutSession
    ): Float {
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