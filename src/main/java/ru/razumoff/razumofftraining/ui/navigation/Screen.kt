package ru.razumoff.razumofftraining.ui.navigation

sealed class Screen(val route: String) {
    object UserProfile : Screen("user_profile")
    object Steps : Screen("steps")
    object Exercises : Screen("exercises")
    object AddExercise : Screen("add_exercise")
    object Workout : Screen("workout")
    object Templates : Screen("templates")

    data object Statistics : Screen("statistics")

    object CreateTemplate : Screen("create_template")

    object TemplateDetail : Screen("template_detail/{templateId}") {
        fun passTemplateId(templateId: String) = "template_detail/$templateId"
    }

    data object EditTemplate : Screen("edit_template/{templateId}") {
        fun passTemplateId(templateId: String) = "edit_template/$templateId"
    }

    object WorkoutSession : Screen("workout_session/{templateId}") {
        fun passTemplateId(templateId: String) = "workout_session/$templateId"
    }

    object SessionDetail : Screen("session_detail/{sessionId}") {
        fun passSessionId(sessionId: String) = "session_detail/$sessionId"
    }

}