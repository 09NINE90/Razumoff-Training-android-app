package ru.razumoff.razo.ui.screens.user.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import ru.razumoff.razo.R
import ru.razumoff.razo.models.MeasurementType
import ru.razumoff.razo.models.localizedName
import ru.razumoff.razo.models.unit
import ru.razumoff.razo.ui.components.cards.SurfaceCard
import ru.razumoff.razo.ui.components.inputs.EditableField

@Composable
fun ProfileEditContent(
    name: String,
    birthDateString: String,
    measurementTypes: List<MeasurementType>,
    measurementInputs: Map<MeasurementType, String>,
    onNameChange: (String) -> Unit,
    onMeasurementChange: (MeasurementType, String) -> Unit,
    onSelectDate: () -> Unit
) {
    SurfaceCard {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            EditableField(
                label = stringResource(R.string.name),
                value = name,
                onValueChange = onNameChange
            )

            EditableField(
                label = stringResource(R.string.date_of_birth),
                value = birthDateString,
                onValueChange = {},
                placeholder = stringResource(R.string.date_of_birth),
                trailingIcon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(
                            R.drawable.ic_pencil_simple
                        ),
                        contentDescription = stringResource(
                            R.string.select_date
                        ),
                        modifier = Modifier.clickable(
                            onClick = onSelectDate
                        )
                    )
                }
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                measurementTypes
                    .chunked(2)
                    .forEach { rowTypes ->

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            rowTypes.forEach { type ->
                                EditableField(
                                    modifier = Modifier.weight(1f),
                                    label = "${type.localizedName()}, ${
                                        type.unit().localizedName()
                                    }",
                                    value = measurementInputs[type].orEmpty(),
                                    onValueChange = { newValue ->
                                        onMeasurementChange(type, newValue)
                                    },
                                    placeholder = type.localizedName()
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
}