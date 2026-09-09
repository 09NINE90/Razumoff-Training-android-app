package ru.razumoff.razumofftraining.ui.components.headers

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import ru.razumoff.razumofftraining.R

@Composable
fun IslandWithButtonHeader(
    headerText: String,
    modifier: Modifier = Modifier,
    onBackClick: (() -> Unit)? = null,
    showBackButton: Boolean = false,
    actionIcon: ImageVector? = Icons.Default.Add,
    actionDescription: String = "Действие",
    onActionClick: (() -> Unit)? = null,
    showActionButton: Boolean = true,
    actionButtonColor: Color = MaterialTheme.colorScheme.primary,
    actionIconTint: Color = MaterialTheme.colorScheme.onPrimary
){
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Островок с заголовком
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Кнопка "Назад" (опционально)
            if (showBackButton && onBackClick != null) {
                Surface(
                    modifier = Modifier
                        .size(50.dp)
                        .clickable { onBackClick() },
                    shape = RoundedCornerShape(50.dp),
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
                    shadowElevation = 8.dp,
                    tonalElevation = 0.dp
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_left),
                            contentDescription = "Назад",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            // Островок с заголовком
            Surface(
                modifier = Modifier
                    .wrapContentWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(50.dp),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
                shadowElevation = 8.dp,
                tonalElevation = 0.dp
            ) {
                Row(
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = headerText,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }


        // Правая часть: кнопка действия (настраиваемая)
        if (showActionButton && onActionClick != null) {
            Surface(
                modifier = Modifier
                    .size(50.dp)
                    .clickable { onActionClick() },
                shape = RoundedCornerShape(50.dp),
                color = actionButtonColor,
                shadowElevation = 8.dp,
                tonalElevation = 0.dp
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    actionIcon?.let {
                        Icon(
                            imageVector = it,
                            contentDescription = actionDescription,
                            modifier = Modifier.size(28.dp),
                            tint = actionIconTint
                        )
                    }
                }
            }
        }
    }
}