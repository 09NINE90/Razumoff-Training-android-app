package ru.razumoff.razo.utils

import android.content.Context
import androidx.annotation.StringRes
import ru.razumoff.razo.R
import ru.razumoff.razo.database.entities.UserMeasurementEntity
import ru.razumoff.razo.models.MeasurementType
import ru.razumoff.razo.models.MeasurementUnit
import ru.razumoff.razo.models.localizedName

class UserFormatter(
    private val context: Context
) {

    private fun getString(@StringRes resId: Int): String {
        return context.getString(resId)
    }

    fun formatToMarkdown(
        userSummary: String,
        measurements: Map<MeasurementType, UserMeasurementEntity>
    ): String {
        val sb = StringBuilder()

        sb.append("# $userSummary\n\n")

        sb.append("## ${getString(R.string.measurements)}\n\n")

        measurements.forEach { (type, measurement) ->
            val unit = MeasurementUnit.fromString(measurement.unit)

            sb.append(
                "- **${type.localizedName(context)}**: " +
                        "${measurement.value}"
            )

            unit?.let {
                sb.append(" ${it.localizedName(context)}")
            }

            sb.append("\n")
        }

        sb.append("\n---\n\n")
        sb.append("*${getString(R.string.generated_by)}*\n")

        return sb.toString()
    }

    fun formatToSimpleText(
        userSummary: String,
        measurements: Map<MeasurementType, UserMeasurementEntity>
    ): String {
        val sb = StringBuilder()

        sb.append("$userSummary\n\n")

        sb.append("${getString(R.string.measurements)}:\n")

        measurements.forEach { (type, measurement) ->
            val unit = MeasurementUnit.fromString(measurement.unit)

            sb.append(
                "${type.localizedName(context)}: " +
                        "${measurement.value}"
            )

            unit?.let {
                sb.append(" ${it.localizedName(context)}")
            }

            sb.append("\n")
        }

        sb.append("\n")
        sb.append("${getString(R.string.generated_by)}\n")

        return sb.toString()
    }
}