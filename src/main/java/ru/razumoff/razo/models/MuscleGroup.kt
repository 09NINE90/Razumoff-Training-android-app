package ru.razumoff.razo.models

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ru.razumoff.razo.R

enum class MuscleGroup {
    CHEST,
    BACK,
    SHOULDERS,
    BICEPS,
    TRICEPS,
    LEGS,
    GLUTES,
    CORE,
    FOREARMS,
    TRAPS,
    LATS,
    CALVES,
    HAMSTRINGS,
    QUADRICEPS,
    FULL_BODY
}

@Composable
fun MuscleGroup.localizedName(): String {
    return when (this) {
        MuscleGroup.CHEST -> stringResource(R.string.muscle_chest)
        MuscleGroup.BACK -> stringResource(R.string.muscle_back)
        MuscleGroup.SHOULDERS -> stringResource(R.string.muscle_shoulders)
        MuscleGroup.BICEPS -> stringResource(R.string.muscle_biceps)
        MuscleGroup.TRICEPS -> stringResource(R.string.muscle_triceps)
        MuscleGroup.LEGS -> stringResource(R.string.muscle_legs)
        MuscleGroup.GLUTES -> stringResource(R.string.muscle_glutes)
        MuscleGroup.CORE -> stringResource(R.string.muscle_core)
        MuscleGroup.FOREARMS -> stringResource(R.string.muscle_forearms)
        MuscleGroup.TRAPS -> stringResource(R.string.muscle_traps)
        MuscleGroup.LATS -> stringResource(R.string.muscle_lats)
        MuscleGroup.CALVES -> stringResource(R.string.muscle_calves)
        MuscleGroup.HAMSTRINGS -> stringResource(R.string.muscle_hamstrings)
        MuscleGroup.QUADRICEPS -> stringResource(R.string.muscle_quadriceps)
        MuscleGroup.FULL_BODY -> stringResource(R.string.muscle_full_body)
    }
}

enum class BodyPart {
    UPPER,
    LOWER,
    FULL
}

@Composable
fun BodyPart.localizedName(): String {
    return when (this) {
        BodyPart.UPPER -> stringResource(R.string.body_part_upper)
        BodyPart.LOWER -> stringResource(R.string.body_part_lower)
        BodyPart.FULL -> stringResource(R.string.body_part_full)
    }
}

enum class MovementType {
    PUSH,
    PULL,
    SQUAT,
    LUNGE,
    ROTATION,
    ISOLATION,
    COMPOUND,
    CARDIO
}

@Composable
fun MovementType.localizedName(): String {
    return when (this) {
        MovementType.PUSH -> stringResource(R.string.movement_push)
        MovementType.PULL -> stringResource(R.string.movement_pull)
        MovementType.SQUAT -> stringResource(R.string.movement_squat)
        MovementType.LUNGE -> stringResource(R.string.movement_lunge)
        MovementType.ROTATION -> stringResource(R.string.movement_rotation)
        MovementType.ISOLATION -> stringResource(R.string.movement_isolation)
        MovementType.COMPOUND -> stringResource(R.string.movement_compound)
        MovementType.CARDIO -> stringResource(R.string.movement_cardio)
    }
}