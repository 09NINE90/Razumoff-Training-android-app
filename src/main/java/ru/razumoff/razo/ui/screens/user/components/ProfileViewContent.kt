package ru.razumoff.razo.ui.screens.user.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import ru.razumoff.razo.R
import ru.razumoff.razo.database.entities.UserMeasurementEntity
import ru.razumoff.razo.models.MeasurementType
import ru.razumoff.razo.models.MeasurementUnit
import ru.razumoff.razo.models.localizedName
import ru.razumoff.razo.ui.components.cards.SurfaceCard

@Composable
fun ProfileViewContent(
    userSummary: String,
    measurementTypes: List<MeasurementType>,
    measurements: Map<MeasurementType, UserMeasurementEntity>
) {

    var showUserInfoMenu by rememberSaveable {
        mutableStateOf(false)
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        SurfaceCard {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = userSummary.orEmpty(),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )

                Box {
                    IconButton(
                        onClick = {
                            showUserInfoMenu = true
                        }
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(
                                R.drawable.dots_three_vertical
                            ),
                            modifier = Modifier.size(24.dp),
                            contentDescription = stringResource(R.string.more)
                        )
                    }

                    UserInfoDropdown(
                        userSummary = userSummary,
                        measurements = measurements,
                        expanded = showUserInfoMenu,
                        onDismissRequest = {
                            showUserInfoMenu = false
                        }
                    )
                }
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            measurementTypes
                .chunked(2)
                .forEach { rowTypes ->

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        rowTypes.forEach { type ->

                            val measurement = measurements[type]

                            MeasurementCard(
                                modifier = Modifier.weight(1f),
                                title = type.localizedName(),
                                value = measurement?.let {
                                    val unit = MeasurementUnit.fromString(it.unit)

                                    buildString {
                                        append(it.value)

                                        unit?.let {
                                            append(" ")
                                            append(it.localizedName())
                                        }
                                    }
                                } ?: stringResource(R.string.not_specified)
                            )
                        }

                        if (rowTypes.size == 1) {
                            Spacer(
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
        }
    }
}