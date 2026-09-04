package ru.razumoff.razumofftraining

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import ru.razumoff.razumofftraining.ui.navigation.BottomNavBar
import ru.razumoff.razumofftraining.ui.navigation.NavGraph
import ru.razumoff.razumofftraining.ui.theme.RazumoffTrainingTheme
import ru.razumoff.razumofftraining.viewmodel.UserViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            RazumoffTrainingTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainApp()
                }
            }
        }
    }

    @Composable
    fun MainApp() {
        val navController = rememberNavController()
        val repository = (application as RazumoffTrainingApplication).appContainer.repository
        val application = (application as RazumoffTrainingApplication)
        val appContainer = application.appContainer

        // Создаем и загружаем пользователя
        val userViewModel: UserViewModel = viewModel(
            factory = UserViewModel.Factory(repository, appContainer)
        )

        val user by userViewModel.user.collectAsState()
        val isLoading by userViewModel.isLoading.collectAsState()

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

                    BottomNavBar(
                        navController = navController,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 25.dp)
                    )
                }
            }
            else -> {
                val errorMessage by userViewModel.errorMessage.collectAsState()
                Log.d("ERROR", errorMessage.toString())
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize().padding(16.dp)
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
            Text("Загрузка...")
        }
    }
}