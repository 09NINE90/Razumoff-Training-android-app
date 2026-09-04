package ru.razumoff.razumofftraining.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ru.razumoff.razumofftraining.database.repository.GymRepository
import ru.razumoff.razumofftraining.ui.screens.exercises.AddExerciseScreen
import ru.razumoff.razumofftraining.ui.screens.exercises.ExercisesScreen
import ru.razumoff.razumofftraining.ui.screens.steps.StepsScreen
import ru.razumoff.razumofftraining.viewmodel.ExerciseViewModel
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavType
import androidx.navigation.navArgument
import ru.razumoff.razumofftraining.ui.screens.templates.CreateTemplateScreen
import ru.razumoff.razumofftraining.ui.screens.templates.CreateTemplateViewModel
import ru.razumoff.razumofftraining.ui.screens.templates.TemplateDetailScreen
import ru.razumoff.razumofftraining.ui.screens.templates.TemplateDetailViewModel
import ru.razumoff.razumofftraining.ui.screens.user.UserProfileScreen
import ru.razumoff.razumofftraining.ui.screens.workouts.templates.TemplatesScreen
import ru.razumoff.razumofftraining.ui.screens.workouts.templates.TemplatesViewModel
import ru.razumoff.razumofftraining.ui.screens.workouts.WorkoutScreen
import ru.razumoff.razumofftraining.ui.screens.workouts.WorkoutViewModel
import ru.razumoff.razumofftraining.ui.screens.workouts.session.details.SessionDetailScreen
import ru.razumoff.razumofftraining.ui.screens.workouts.session.SessionDetailViewModel
import ru.razumoff.razumofftraining.ui.screens.workouts.session.WorkoutSessionScreen
import ru.razumoff.razumofftraining.ui.screens.workouts.session.WorkoutSessionViewModel
import ru.razumoff.razumofftraining.viewmodel.UserViewModel

@Composable
fun NavGraph(
    viewModelUser: UserViewModel = viewModel(),
    navController: NavHostController,
    userId: String,
    repository: GymRepository,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Steps.route,
        modifier = modifier,
        enterTransition = {
            // Анимация входа: слайд слева
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(300)
            )
        },
        exitTransition = {
            // Анимация выхода: слайд вправо
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(300)
            )
        },
        popEnterTransition = {
            // Анимация входа при возврате: слайд справа
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(300)
            )
        },
        popExitTransition = {
            // Анимация выхода при возврате: слайд влево
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(300)
            )
        }
    ) {
        composable(Screen.Exercises.route) {
            val viewModel: ExerciseViewModel = viewModel(
                factory = ExerciseViewModel.Factory(repository, userId)
            )

            ExercisesScreen(
                viewModel = viewModel,
                onAddExercise = { navController.navigate(Screen.AddExercise.route) },
                onExerciseClick = { exercise ->
                    // TODO: Перейти к деталям упражнения
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.AddExercise.route) {
            val viewModel: ExerciseViewModel = viewModel(
                factory = ExerciseViewModel.Factory(repository, userId)
            )

            AddExerciseScreen(
                onSave = { exercise ->
                    viewModel.addExercise(exercise)
                    navController.popBackStack()
                },
                onCancel = { navController.popBackStack() },
                isSaving = viewModel.isSaving.collectAsState().value
            )
        }

        composable(Screen.Steps.route) {
            StepsScreen()
        }

        composable(Screen.UserProfile.route) {
            UserProfileScreen(
                viewModel = viewModelUser,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.SessionDetail.route,
            arguments = listOf(navArgument("sessionId") { type = NavType.StringType })
        ) { backStackEntry ->
            val sessionId = backStackEntry.arguments?.getString("sessionId") ?: return@composable

            val viewModel: SessionDetailViewModel = viewModel(
                factory = SessionDetailViewModel.Factory(repository, userId, sessionId)
            )

            SessionDetailScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Workout.route) {
            val viewModelWorkout: WorkoutViewModel = viewModel(
                factory = WorkoutViewModel.Factory(repository, userId)
            )
            val viewModelExercise: ExerciseViewModel = viewModel(
                factory = ExerciseViewModel.Factory(repository, userId)
            )
            WorkoutScreen(
                viewModelWorkout = viewModelWorkout,
                viewModelExercise = viewModelExercise,
                onTemplatesClick = { navController.navigate(Screen.Templates.route) },
                onExerciseClick = { navController.navigate(Screen.Exercises.route) },
                onSessionClick = { session ->
                    navController.navigate(Screen.SessionDetail.passSessionId(session.id))
                }
            )
        }

        composable(Screen.Templates.route) {
            val viewModel: TemplatesViewModel = viewModel(
                factory = TemplatesViewModel.Factory(repository, userId)
            )
            TemplatesScreen(
                viewModel = viewModel,
                onAddTemplate = { navController.navigate(Screen.CreateTemplate.route) },
                onTemplateClick = { template ->
                    navController.navigate(Screen.TemplateDetail.passTemplateId(template.id))
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.TemplateDetail.route,
            arguments = listOf(navArgument("templateId") { type = NavType.StringType })
        ) { backStackEntry ->
            val templateId = backStackEntry.arguments?.getString("templateId") ?: return@composable

            // Создаем ViewModel с параметрами
            val viewModel: TemplateDetailViewModel = viewModel(
                factory = TemplateDetailViewModel.Factory(repository, userId, templateId)
            )

            TemplateDetailScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onStartWorkout = {
                    navController.navigate(Screen.WorkoutSession.passTemplateId(templateId))
                }
            )
        }

        composable(Screen.CreateTemplate.route) {
            val viewModel: CreateTemplateViewModel = viewModel(
                factory = CreateTemplateViewModel.Factory(repository, userId)
            )
            CreateTemplateScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onTemplateCreated = {
                    // Принудительно обновляем список шаблонов
                    // Можно использовать remember и State для триггера
                }
            )
        }

        composable(
            route = Screen.WorkoutSession.route,
            arguments = listOf(navArgument("templateId") { type = NavType.StringType })
        ) { backStackEntry ->
            val templateId = backStackEntry.arguments?.getString("templateId") ?: return@composable

            val viewModel: WorkoutSessionViewModel = viewModel(
                factory = WorkoutSessionViewModel.Factory(repository, userId, templateId)
            )

            WorkoutSessionScreen(
                viewModel = viewModel,
                onFinish = { navController.navigate(Screen.Workout.route) }
            )
        }
    }
}