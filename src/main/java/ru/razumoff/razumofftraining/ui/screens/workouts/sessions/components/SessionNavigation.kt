package ru.razumoff.razumofftraining.ui.screens.workouts.sessions.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SessionNavigation(
    currentIndex: Int,
    totalExercises: Int,
    hasSets: Boolean,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onFinish: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(
            onClick = onPrevious,
            modifier = Modifier.weight(1f),
            enabled = currentIndex > 0,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
            Spacer(modifier = Modifier.width(4.dp))
            Text("Назад")
        }

        Button(
            onClick = {
                if (currentIndex == totalExercises - 1) {
                    onFinish()
                } else {
                    onNext()
                }
            },
            modifier = Modifier.weight(1f),
            enabled = hasSets
        ) {
            if (currentIndex == totalExercises - 1) {
                Icon(Icons.Default.Check, contentDescription = "Завершить")
                Spacer(modifier = Modifier.width(4.dp))
                Text("Завершить")
            } else {
                Text("Далее")
                Spacer(modifier = Modifier.width(4.dp))
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Далее")
            }
        }
    }
}
