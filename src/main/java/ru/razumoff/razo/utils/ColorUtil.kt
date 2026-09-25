package ru.razumoff.razo.utils

import androidx.compose.ui.graphics.Color
import kotlin.math.max
import kotlin.math.min

private data class Hsl(
    val hue: Float,
    val saturation: Float,
    val lightness: Float
)

private fun Color.toHsl(): Hsl {
    val r = red
    val g = green
    val b = blue

    val max = max(r, max(g, b))
    val min = min(r, min(g, b))
    val delta = max - min

    val lightness = (max + min) / 2f

    if (delta == 0f) {
        return Hsl(
            hue = 0f,
            saturation = 0f,
            lightness = lightness
        )
    }

    val saturation = delta / (1f - kotlin.math.abs(2f * lightness - 1f))

    val hue = when (max) {
        r -> 60f * (((g - b) / delta) % 6f)
        g -> 60f * (((b - r) / delta) + 2f)
        else -> 60f * (((r - g) / delta) + 4f)
    }.let {
        if (it < 0f) it + 360f else it
    }

    return Hsl(
        hue = hue,
        saturation = saturation,
        lightness = lightness
    )
}

fun Color.withWeightIntensity(
    weight: Float,
    minWeight: Float,
    maxWeight: Float,
    minSaturation: Float = 0.20f,
    minLightnessFactor: Float = 0.70f
): Color {
    if (maxWeight <= minWeight) return this

    val ratio = ((weight - minWeight) / (maxWeight - minWeight))
        .coerceIn(0f, 1f)

    // Усиление различий.
    val intensity = ratio * ratio

    val hsl = toHsl()

    val saturation = minSaturation +
            (hsl.saturation - minSaturation) * intensity

    val lightnessFactor = minLightnessFactor +
            (1f - minLightnessFactor) * intensity

    val lightness = hsl.lightness * lightnessFactor

    return Color.hsl(
        hue = hsl.hue,
        saturation = saturation,
        lightness = lightness,
        alpha = alpha
    )
}