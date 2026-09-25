package ru.razumoff.razo.ui.screens.workouts.sessions.details.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.compose.cartesian.data.ColumnCartesianLayerModel
import com.patrykandpatrick.vico.compose.cartesian.data.columnModel
import com.patrykandpatrick.vico.compose.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.Fill
import com.patrykandpatrick.vico.compose.common.component.LineComponent
import com.patrykandpatrick.vico.compose.common.component.TextComponent
import com.patrykandpatrick.vico.compose.common.data.ExtraStore
import ru.razumoff.razo.R
import ru.razumoff.razo.models.SessionExercise
import ru.razumoff.razo.ui.components.cards.SurfaceCard
import ru.razumoff.razo.utils.withWeightIntensity

@Composable
fun SessionExerciseSets(
    exercise: SessionExercise,
    modifier: Modifier = Modifier
) {
    val workingSets = exercise.sets.filterNot { it.isWarmup }

    if (workingSets.isEmpty()) {
        Text(
            text = stringResource(R.string.no_sets),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = modifier
        )
        return
    }


    // -------------------------------------------------------------------------
    // Chart data
    // -------------------------------------------------------------------------

    val modelProducer = remember {
        CartesianChartModelProducer()
    }

    LaunchedEffect(workingSets) {
        modelProducer.runTransaction {
            columnModel {
                series(
                    x = workingSets.indices.map { it.toDouble() },
                    y = workingSets.map { it.reps.toDouble() }
                )
            }
        }
    }

    // -------------------------------------------------------------------------
    // Axis labels
    // -------------------------------------------------------------------------

    val weightFormatter = remember(workingSets) {
        CartesianValueFormatter { _, value, _ ->
            val index = value.toInt()

            workingSets
                .getOrNull(index)
                ?.weight
                ?.let {
                    if (it > 0) {
                        "$it"
                    } else {
                        "${it.toInt()}"
                    }
                }
                ?: ""
        }
    }

    val repetitionsLabel = stringResource(R.string.repetitions)
    val weightLabel = stringResource(R.string.weight)
    val withOutWeightLabel = stringResource(R.string.without_weight)
    val kgLabel = stringResource(R.string.kg)

    val axisTitleComponent = TextComponent(
        textStyle = TextStyle(
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )

    // -------------------------------------------------------------------------
    // Column colors
    // -------------------------------------------------------------------------

    val primaryColor = MaterialTheme.colorScheme.primary

    val minWeight = workingSets.minOf { it.weight }
    val maxWeight = workingSets.maxOf { it.weight }

    // -------------------------------------------------------------------------
    // Column provider
    // -------------------------------------------------------------------------

    val columnProvider = object : ColumnCartesianLayer.ColumnProvider {

        override fun getColumn(
            entry: ColumnCartesianLayerModel.Entry,
            extraStore: ExtraStore
        ): LineComponent {
            val index = entry.x.toInt()
            val weight = workingSets[index].weight

            val color = primaryColor.withWeightIntensity(
                weight = weight,
                minWeight = minWeight,
                maxWeight = maxWeight
            )

            return LineComponent(
                fill = Fill(color),
                thickness = 20.dp
            )
        }

        override fun getWidestSeriesColumn(
            seriesKey: Any,
            seriesIndex: Int,
            extraStore: ExtraStore
        ): LineComponent {
            return LineComponent(
                fill = Fill(primaryColor),
                thickness = 20.dp
            )
        }
    }

    // -------------------------------------------------------------------------
    // Chart
    // -------------------------------------------------------------------------

    val chart = rememberCartesianChart(
        rememberColumnCartesianLayer(
            columnProvider = columnProvider,
        ),
        startAxis = VerticalAxis.rememberStart(
            titleComponent = axisTitleComponent,
            title = { repetitionsLabel }
        ),
        bottomAxis = HorizontalAxis.rememberBottom(
            valueFormatter = weightFormatter,
            titleComponent = axisTitleComponent,
            title = {
                if (maxWeight > 0f) {
                    "$weightLabel, $kgLabel"
                } else {
                    withOutWeightLabel
                }
            }
        )
    )

    // -------------------------------------------------------------------------
    // Content
    // -------------------------------------------------------------------------

    SurfaceCard(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 12.dp
                ),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(
                            R.drawable.ic_barbell
                        ),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        text = "${exercise.order}. ${exercise.exerciseName}",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            CartesianChartHost(
                chart = chart,
                modelProducer = modelProducer,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            )
        }
    }
}