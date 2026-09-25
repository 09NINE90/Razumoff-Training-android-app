package ru.razumoff.razo.models

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ru.razumoff.razo.R

enum class MeasurementType {
    WEIGHT,
    HEIGHT,
    CHEST,
    SHOULDERS,
    WAIST,
    HIPS,
    BICEPS,
    FOREARM,
    THIGH,
    CALF;

    companion object {
        fun fromString(value: String): MeasurementType? {
            return entries.find { it.name == value }
        }
    }
}

@Composable
fun MeasurementType.localizedName(): String {
    return when (this) {
        MeasurementType.WEIGHT -> stringResource(R.string.measurement_weight)
        MeasurementType.HEIGHT -> stringResource(R.string.measurement_height)
        MeasurementType.CHEST -> stringResource(R.string.measurement_chest)
        MeasurementType.WAIST -> stringResource(R.string.measurement_waist)
        MeasurementType.HIPS -> stringResource(R.string.measurement_hips)
        MeasurementType.BICEPS -> stringResource(R.string.measurement_biceps)
        MeasurementType.FOREARM -> stringResource(R.string.measurement_forearm)
        MeasurementType.THIGH -> stringResource(R.string.measurement_thigh)
        MeasurementType.CALF -> stringResource(R.string.measurement_calf)
        MeasurementType.SHOULDERS -> stringResource(R.string.measurement_shoulders)
    }
}

fun MeasurementType.localizedName(context: Context): String {
    return when (this) {
        MeasurementType.WEIGHT -> context.getString(R.string.measurement_weight)
        MeasurementType.HEIGHT -> context.getString(R.string.measurement_height)
        MeasurementType.CHEST -> context.getString(R.string.measurement_chest)
        MeasurementType.WAIST -> context.getString(R.string.measurement_waist)
        MeasurementType.HIPS -> context.getString(R.string.measurement_hips)
        MeasurementType.BICEPS -> context.getString(R.string.measurement_biceps)
        MeasurementType.FOREARM -> context.getString(R.string.measurement_forearm)
        MeasurementType.THIGH -> context.getString(R.string.measurement_thigh)
        MeasurementType.CALF -> context.getString(R.string.measurement_calf)
        MeasurementType.SHOULDERS -> context.getString(R.string.measurement_shoulders)
    }
}

fun MeasurementType.unit(): MeasurementUnit {
    return when (this) {
        MeasurementType.WEIGHT -> MeasurementUnit.KG
        MeasurementType.HEIGHT -> MeasurementUnit.CM
        MeasurementType.CHEST -> MeasurementUnit.CM
        MeasurementType.WAIST -> MeasurementUnit.CM
        MeasurementType.HIPS -> MeasurementUnit.CM
        MeasurementType.BICEPS -> MeasurementUnit.CM
        MeasurementType.FOREARM -> MeasurementUnit.CM
        MeasurementType.THIGH -> MeasurementUnit.CM
        MeasurementType.CALF -> MeasurementUnit.CM
        MeasurementType.SHOULDERS -> MeasurementUnit.CM
    }
}

enum class MeasurementUnit {
    KG,
    CM;

    companion object {
        fun fromString(value: String): MeasurementUnit? {
            return values().find { it.name == value.uppercase() }
        }
    }
}


@Composable
fun MeasurementUnit.localizedName(): String {
    return when (this) {
        MeasurementUnit.KG -> stringResource(R.string.kg)
        MeasurementUnit.CM -> stringResource(R.string.cm)
    }
}

fun MeasurementUnit.localizedName(context: Context): String {
    return when (this) {
        MeasurementUnit.KG -> context.getString(R.string.kg)
        MeasurementUnit.CM -> context.getString(R.string.cm)
    }
}
