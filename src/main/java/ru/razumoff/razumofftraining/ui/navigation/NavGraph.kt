package ru.razumoff.razumofftraining.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ru.razumoff.razumofftraining.database.repository.GymRepository
import ru.razumoff.razumofftraining.ui.screens.exercises.AddExerciseScreen
import ru.razumoff.razumofftraining.ui.screens.exercises.ExerciseViewModel
import ru.razumoff.razumofftraining.ui.screens.exercises.ExercisesScreen
import ru.razumoff.razumofftraining.ui.screens.statistics.StatisticsScreen
import ru.razumoff.razumofftraining.ui.screens.statistics.viewmodel.StatisticsViewModel
import ru.razumoff.razumofftraining.ui.screens.steps.StepsScreen
import ru.razumoff.razumofftraining.ui.screens.user.UserProfileScreen
import ru.razumoff.razumofftraining.ui.screens.workouts.WorkoutScreen
import ru.razumoff.razumofftraining.ui.screens.workouts.WorkoutViewModel
import ru.razumoff.razumofftraining.ui.screens.workouts.sessions.SessionDetailViewModel
import ru.razumoff.razumofftraining.ui.screens.workouts.sessions.WorkoutSessionScreen
import ru.razumoff.razumofftraining.ui.screens.workouts.sessions.WorkoutSessionViewModel
import ru.razumoff.razumofftraining.ui.screens.workouts.sessions.details.SessionDetailScreen
import ru.razumoff.razumofftraining.ui.screens.workouts.templates.TemplatesScreen
import ru.razumoff.razumofftraining.ui.screens.workouts.templates.TemplatesViewModel
import ru.razumoff.razumofftraining.ui.screens.workouts.templates.create.CreateTemplateScreen
import ru.razumoff.razumofftraining.ui.screens.workouts.templates.create.CreateTemplateViewModel
import ru.razumoff.razumofftraining.ui.screens.workouts.templates.details.TemplateDetailScreen
import ru.razumoff.razumofftraining.ui.screens.workouts.templates.details.TemplateDetailViewModel
import ru.razumoff.razumofftraining.ui.screens.workouts.templates.edit.EditTemplateScreen
import ru.razumoff.razumofftraining.ui.screens.workouts.templates.edit.EditTemplateViewModel
import ru.razumoff.razumofftraining.ui.screens.user.UserViewModel

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
            slideFadeEnter()
        },
        exitTransition = {
            slideFadeExit()
        },
        popEnterTransition = {
            slideFadePopEnter()
        },
        popExitTransition = {
            slideFadePopExit()
        }
    ) {
        composable(
            route = Screen.Exercises.route,
            enterTransition = {
                detailEnter()
            },
            exitTransition = {
                detailExit()
            },
            popEnterTransition = {
                detailPopEnter()
            },
            popExitTransition = {
                detailPopExit()
            }
        ) {
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

        composable(
            route = Screen.AddExercise.route,
            enterTransition = {
                detailEnter()
            },
            exitTransition = {
                detailExit()
            },
            popEnterTransition = {
                detailPopEnter()
            },
            popExitTransition = {
                detailPopExit()
            }
        ) {
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
            )
        }

        composable(
            route = Screen.SessionDetail.route,
            arguments = listOf(navArgument("sessionId") { type = NavType.StringType }),
            enterTransition = {
                detailEnter()
            },
            exitTransition = {
                detailExit()
            },
            popEnterTransition = {
                detailPopEnter()
            },
            popExitTransition = {
                detailPopExit()
            }
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
            enterTransition = {
                detailEnter()
            },
            exitTransition = {
                detailExit()
            },
            popEnterTransition = {
                detailPopEnter()
            },
            popExitTransition = {
                detailPopExit()
            }
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
            enterTransition = {
                detailEnter()
            },
            exitTransition = {
                detailExit()
            },
            popEnterTransition = {
                detailPopEnter()
            },
            popExitTransition = {
                detailPopExit()
            }
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
            enterTransition = {
                detailEnter()
            },
            exitTransition = {
                detailExit()
            },
            popEnterTransition = {
                detailPopEnter()
            },
            popExitTransition = {
                detailPopExit()
            }
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
            enterTransition = {
                detailEnter()
            },
            exitTransition = {
                detailExit()
            },
            popEnterTransition = {
                detailPopEnter()
            },
            popExitTransition = {
                detailPopExit()
            }
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
            enterTransition = {
                detailEnter()
            },
            exitTransition = {
                detailExit()
            },
            popEnterTransition = {
                detailPopEnter()
            },
            popExitTransition = {
                detailPopExit()
            }
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
            enterTransition = {
                detailEnter()
            },
            exitTransition = {
                detailExit()
            },
            popEnterTransition = {
                detailPopEnter()
            },
            popExitTransition = {
                detailPopExit()
            }
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