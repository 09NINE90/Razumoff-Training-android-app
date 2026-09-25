package ru.razumoff.razo.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ru.razumoff.razo.database.repository.GymRepository
import ru.razumoff.razo.ui.screens.exercises.AddExerciseScreen
import ru.razumoff.razo.ui.screens.exercises.ExerciseViewModel
import ru.razumoff.razo.ui.screens.exercises.ExercisesScreen
import ru.razumoff.razo.ui.screens.statistics.StatisticsScreen
import ru.razumoff.razo.ui.screens.statistics.viewmodel.StatisticsViewModel
import ru.razumoff.razo.ui.screens.steps.StepsScreen
import ru.razumoff.razo.ui.screens.user.UserProfileScreen
import ru.razumoff.razo.ui.screens.user.UserViewModel
import ru.razumoff.razo.ui.screens.workouts.WorkoutScreen
import ru.razumoff.razo.ui.screens.workouts.WorkoutViewModel
import ru.razumoff.razo.ui.screens.workouts.sessions.SessionDetailViewModel
import ru.razumoff.razo.ui.screens.workouts.sessions.WorkoutSessionScreen
import ru.razumoff.razo.ui.screens.workouts.sessions.WorkoutSessionViewModel
import ru.razumoff.razo.ui.screens.workouts.sessions.details.SessionDetailScreen
import ru.razumoff.razo.ui.screens.workouts.templates.TemplatesScreen
import ru.razumoff.razo.ui.screens.workouts.templates.TemplatesViewModel
import ru.razumoff.razo.ui.screens.workouts.templates.create.CreateTemplateScreen
import ru.razumoff.razo.ui.screens.workouts.templates.create.CreateTemplateViewModel
import ru.razumoff.razo.ui.screens.workouts.templates.details.TemplateDetailScreen
import ru.razumoff.razo.ui.screens.workouts.templates.details.TemplateDetailViewModel
import ru.razumoff.razo.ui.screens.workouts.templates.edit.EditTemplateScreen
import ru.razumoff.razo.ui.screens.workouts.templates.edit.EditTemplateViewModel


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
            when {
                isForwardHorizontalNavigation() -> horizontalEnter()

                isBackwardHorizontalNavigation() -> horizontalPopEnter()

                else -> verticalEnter()
            }
        },

        exitTransition = {
            when {
                isForwardHorizontalNavigation() -> horizontalExit()

                isBackwardHorizontalNavigation() -> horizontalPopExit()

                else -> verticalExit()
            }
        },

        popEnterTransition = {
            if (isHorizontalNavigation()) {
                horizontalPopEnter()
            } else {
                verticalPopEnter()
            }
        },

        popExitTransition = {
            if (isHorizontalNavigation()) {
                horizontalPopExit()
            } else {
                verticalPopExit()
            }
        }
    ) {
        composable(
            route = Screen.Exercises.route
        ) {
            val viewModel: ExerciseViewModel = viewModel(
                factory = ExerciseViewModel.Factory(repository, userId)
            )

            ExercisesScreen(
                viewModel = viewModel,
                navController = navController,
                onAddExercise = { navController.navigate(Screen.AddExercise.route) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.AddExercise.route,
        ) {
            val viewModel: ExerciseViewModel = viewModel(
                factory = ExerciseViewModel.Factory(repository, userId)
            )

            AddExerciseScreen(
                onSave = { exercise ->
                    viewModel.addExercise(
                        exercise = exercise,
                        onSuccess = {
                            navController.previousBackStackEntry
                                ?.savedStateHandle
                                ?.set("exercise_saved", true)

                            navController.popBackStack()
                        }
                    )
                },
                onCancel = {
                    navController.popBackStack()
                },
                onBack = {
                    navController.popBackStack()
                },
                isSaving = viewModel.isSaving.collectAsState().value
            )
        }

        composable(Screen.Steps.route) {
            StepsScreen()
        }

        composable(Screen.UserProfile.route) {
            UserProfileScreen(
                viewModel = viewModelUser,
            )
        }

        composable(
            route = Screen.SessionDetail.route,
            arguments = listOf(navArgument("sessionId") { type = NavType.StringType }),
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
                onTemplatesClick = {
                    navController.navigate(Screen.Templates.route)
                },
                onExerciseClick = {
                    navController.navigate(Screen.Exercises.route)
                },
                onSessionClick = { session ->
                    navController.navigate(
                        Screen.SessionDetail.passSessionId(session.id)
                    )
                },
                onStatisticsClick = {
                    navController.navigate(Screen.Statistics.route)
                },
            )
        }

        composable(
            route = Screen.Templates.route,
        ) {
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
            arguments = listOf(navArgument("templateId") { type = NavType.StringType }),
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
                },
                onEdit = {
                    navController.navigate(
                        Screen.EditTemplate.passTemplateId(templateId)
                    )
                }
            )
        }

        composable(
            route = Screen.EditTemplate.route,
            arguments = listOf(
                navArgument("templateId") {
                    type = NavType.StringType
                }
            ),
        ) { backStackEntry ->

            val templateId =
                backStackEntry.arguments?.getString("templateId")
                    ?: return@composable

            val viewModel: EditTemplateViewModel = viewModel(
                factory = EditTemplateViewModel.Factory(
                    repository = repository,
                    userId = userId,
                    templateId = templateId
                )
            )

            EditTemplateScreen(
                viewModel = viewModel,
                onBack = {
                    navController.popBackStack()
                },
                onSaved = {
                    navController.popBackStack()
                },
                onDeleted = {
                    navController.navigate(Screen.Templates.route)
                }
            )
        }

        composable(
            route = Screen.CreateTemplate.route,
        ) {
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
            arguments = listOf(navArgument("templateId") { type = NavType.StringType }),
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

        composable(
            route = Screen.Statistics.route,
        ) {
            val viewModel: StatisticsViewModel = viewModel(
                factory = StatisticsViewModel.Factory(repository, userId)
            )
            StatisticsScreen(
                viewModel = viewModel,
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}