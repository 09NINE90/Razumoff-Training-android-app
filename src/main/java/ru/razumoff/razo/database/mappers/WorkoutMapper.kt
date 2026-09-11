//package ru.razumoff.razumofftraining.database.mappers
//
//import ru.razumoff.razumofftraining.database.entities.WorkoutEntity
//import ru.razumoff.razumofftraining.database.entities.WorkoutExerciseEntity
//import ru.razumoff.razumofftraining.database.entities.WorkoutSetEntity
//import ru.razumoff.razumofftraining.models.Workout
//import ru.razumoff.razumofftraining.models.WorkoutExercise
//import ru.razumoff.razumofftraining.models.WorkoutSet
//
//object WorkoutMapper {
//
//    fun toDomain(
//        workoutEntity: WorkoutEntity,
//        exerciseEntities: List<WorkoutExerciseEntity>,
//        setEntities: List<WorkoutSetEntity>,
//        exerciseNames: Map<String, String> // exerciseId -> exerciseName
//    ): Workout {
//        val exercises = exerciseEntities.map { exerciseEntity ->
//            val sets = setEntities
//                .filter { it.workoutExerciseId == exerciseEntity.id }
//                .map { setEntity ->
//                    WorkoutSet(
//                        id = setEntity.id,
//                        setNumber = setEntity.setNumber,
//                        reps = setEntity.reps,
//                        weight = setEntity.weight,
//                        isWarmup = setEntity.isWarmup
//                    )
//                }
//
//            WorkoutExercise(
//                id = exerciseEntity.id,
//                exerciseId = exerciseEntity.exerciseId,
//                exerciseName = exerciseNames[exerciseEntity.exerciseId] ?: "",
//                order = exerciseEntity.order,
//                sets = sets,
//                notes = exerciseEntity.notes
//            )
//        }
//
//        return Workout(
//            id = workoutEntity.id,
//            date = workoutEntity.date,
//            exercises = exercises,
//            duration = workoutEntity.duration,
//            notes = workoutEntity.notes,
//            feeling = workoutEntity.feeling
//        )
//    }
//
//    fun toEntity(domain: Workout, userId: String): WorkoutEntity {
//        return WorkoutEntity(
//            id = domain.id,
//            userId = userId,
//            date = domain.date,
//            duration = domain.duration,
//            notes = domain.notes,
//            feeling = domain.feeling
//        )
//    }
//}