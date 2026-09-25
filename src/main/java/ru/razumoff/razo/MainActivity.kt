package ru.razumoff.razo

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.razumoff.razo.ui.navigation.AnimatedBottomNavBar
import ru.razumoff.razo.ui.navigation.NavGraph
import ru.razumoff.razo.ui.navigation.Screen
import ru.razumoff.razo.ui.screens.user.UserViewModel
import ru.razumoff.razo.ui.theme.RazumoffTrainingTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            RazumoffTrainingTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CompositionLocalProvider(
                        LocalRippleConfiguration provides null
                    ) {
                        MainApp()
                    }
                }
            }
        }
    }

    @Composable
    fun MainApp() {
        val navController = rememberNavController()

        val application = application as RazoApplication
        val appContainer = application.appContainer
        val repository = appContainer.repository

        val userViewModel: UserViewModel = viewModel(
            factory = UserViewModel.Factory(repository, appContainer)
        )

        val user by userViewModel.user.collectAsState()
        val isLoading by userViewModel.isLoading.collectAsState()

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        val bottomBarRoutes = setOf(
            Screen.Steps.route,
            Screen.Workout.route,
            Screen.UserProfile.route
        )


        when {
            isLoading -> {
                LoadingScreen()
            }

            user != null -> {
                Box(modifier = Modifier.fillMaxSize()) {

                    NavGraph(
                        viewModelUser = userViewModel,
                        navController = navController,
                        userId = user!!.id,
                        repository = repository,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 50.dp)
                    )

                    if (currentRoute in bottomBarRoutes) {
                        AnimatedBottomNavBar(
                            navController = navController,
                            visible = currentRoute in bottomBarRoutes,
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .fillMaxWidth()
                        )
                    }
                }
            }

            else -> {
                val errorMessage by userViewModel.errorMessage.collectAsState()
                Log.d("ERROR", errorMessage.toString())
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Ошибка загрузки пользователя",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
            Text(stringResource(R.string.loading_data))
        }
    }
}