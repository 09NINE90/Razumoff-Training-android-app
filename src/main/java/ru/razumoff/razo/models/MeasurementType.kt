package ru.razumoff.razo.models

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ru.razumoff.razo.R

enum class MeasurementType {
    WEIGHT,
    CHEST,
    WAIST,
    HIPS,
    BICEPS,
    FOREARM,
    THIGH,
    CALF,
    SHOULDERS,
    NECK;

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
        MeasurementType.CHEST -> stringResource(R.string.measurement_chest)
        MeasurementType.WAIST -> stringResource(R.string.measurement_waist)
        MeasurementType.HIPS -> stringResource(R.string.measurement_hips)
        MeasurementType.BICEPS -> stringResource(R.string.measurement_biceps)
        MeasurementType.FOREARM -> stringResource(R.string.measurement_forearm)
        MeasurementType.THIGH -> stringResource(R.string.measurement_thigh)
        MeasurementType.CALF -> stringResource(R.string.measurement_calf)
        MeasurementType.SHOULDERS -> stringResource(R.string.measurement_shoulders)
        MeasurementType.NECK -> stringResource(R.string.measurement_neck)
    }
}

enum class MeasurementUnit(val displayName: String) {
    KG("кг"),
    CM("см");

    companion object {
        fun fromString(value: String): MeasurementUnit? {
            return values().find { it.name == value }
        }
    }
}