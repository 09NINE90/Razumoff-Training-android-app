package ru.razumoff.razo.ui.screens.workouts.sessions.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import ru.razumoff.razo.R

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
            Icon(
                ImageVector.vectorResource(R.drawable.ic_arrow_left),
                contentDescription = stringResource(R.string.back)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(stringResource(R.string.back))
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
                Icon(
                    Icons.Default.Check,
                    contentDescription = stringResource(R.string.done)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(stringResource(R.string.done))
            } else {
                Text(stringResource(R.string.next))
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    ImageVector.vectorResource(R.drawable.ic_arrow_right),
                    contentDescription = stringResource(R.string.next)
                )
            }
        }
    }
}
