package com.maloy.muzza.ui.theme

import android.graphics.Bitmap
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.palette.graphics.Palette
import com.materialkolor.hct.Hct
import com.materialkolor.scheme.DynamicScheme
import com.materialkolor.scheme.SchemeTonalSpot
import com.materialkolor.score.Score

val DefaultThemeColor = Color(0xFFED5564)

@Composable
fun MuzzaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    pureBlack: Boolean = false,
    themeColor: Color = DefaultThemeColor,
    glassMode: Boolean = false,
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val baseColorScheme =
        remember(darkTheme, pureBlack, themeColor) {
            if (themeColor == DefaultThemeColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (darkTheme) {
                    dynamicDarkColorScheme(context).pureBlack(pureBlack)
                } else {
                    dynamicLightColorScheme(context)
                }
            } else {
                SchemeTonalSpot(Hct.fromInt(themeColor.toArgb()), darkTheme, 0.0)
                    .toColorScheme()
                    .pureBlack(darkTheme && pureBlack)
            }
        }
    val colorScheme = remember(baseColorScheme, glassMode) {
        if (!glassMode) {
            baseColorScheme
        } else {
            baseColorScheme.copy(
                surface = baseColorScheme.surface.copy(alpha = 0.84f),
                surfaceVariant = baseColorScheme.surfaceVariant.copy(alpha = 0.66f),
                background = baseColorScheme.background.copy(alpha = 0.90f),
                surfaceDim = baseColorScheme.surfaceDim.copy(alpha = 0.76f),
                surfaceBright = baseColorScheme.surfaceBright.copy(alpha = 0.88f),
                surfaceContainerLowest = baseColorScheme.surfaceContainerLowest.copy(alpha = 0.48f),
                surfaceContainerLow = baseColorScheme.surfaceContainerLow.copy(alpha = 0.56f),
                surfaceContainer = baseColorScheme.surfaceContainer.copy(alpha = 0.64f),
                surfaceContainerHigh = baseColorScheme.surfaceContainerHigh.copy(alpha = 0.72f),
                surfaceContainerHighest = baseColorScheme.surfaceContainerHighest.copy(alpha = 0.78f),
                outlineVariant = baseColorScheme.outlineVariant.copy(alpha = 0.42f),
            )
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = MaterialTheme.typography,
        content = content,
    )
}

@Composable
fun MuzzaGlassBackdrop(
    pureBlack: Boolean = false,
    modifier: Modifier = Modifier,
) {
    val colorScheme = MaterialTheme.colorScheme
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(if (pureBlack) Color.Black else colorScheme.background.copy(alpha = 1f))
    ) {
        if (!pureBlack) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset(x = (-170).dp, y = 90.dp)
                    .size(440.dp)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(colorScheme.primary.copy(alpha = 0.34f), Color.Transparent)
                        ),
                        CircleShape
                    )
            )
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = 170.dp, y = 140.dp)
                    .size(420.dp)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(colorScheme.tertiary.copy(alpha = 0.24f), Color.Transparent)
                        ),
                        CircleShape
                    )
            )
        }
    }
}

fun Bitmap.extractThemeColor(): Color {
    val colorsToPopulation =
        Palette
            .from(this)
            .maximumColorCount(8)
            .generate()
            .swatches
            .associate { it.rgb to it.population }
    val rankedColors = Score.score(colorsToPopulation)
    return Color(rankedColors.first())
}

fun Bitmap.extractGradientColors(): List<Color> {
    val extractedColors = Palette.from(this)
        .maximumColorCount(64)
        .generate()
        .swatches
        .associate { it.rgb to it.population }

    val orderedColors = Score.score(extractedColors, 2, 0xff4285f4.toInt(), true)
        .sortedByDescending { Color(it).luminance() }

    return if (orderedColors.size >= 2)
        listOf(Color(orderedColors[0]), Color(orderedColors[1]))
    else
        listOf(Color(0xFF595959), Color(0xFF0D0D0D))
}

fun DynamicScheme.toColorScheme() =
    ColorScheme(
        primary = Color(primary),
        onPrimary = Color(onPrimary),
        primaryContainer = Color(primaryContainer),
        onPrimaryContainer = Color(onPrimaryContainer),
        inversePrimary = Color(inversePrimary),
        secondary = Color(secondary),
        onSecondary = Color(onSecondary),
        secondaryContainer = Color(secondaryContainer),
        onSecondaryContainer = Color(onSecondaryContainer),
        tertiary = Color(tertiary),
        onTertiary = Color(onTertiary),
        tertiaryContainer = Color(tertiaryContainer),
        onTertiaryContainer = Color(onTertiaryContainer),
        background = Color(background),
        onBackground = Color(onBackground),
        surface = Color(surface),
        onSurface = Color(onSurface),
        surfaceVariant = Color(surfaceVariant),
        onSurfaceVariant = Color(onSurfaceVariant),
        surfaceTint = Color(primary),
        inverseSurface = Color(inverseSurface),
        inverseOnSurface = Color(inverseOnSurface),
        error = Color(error),
        onError = Color(onError),
        errorContainer = Color(errorContainer),
        onErrorContainer = Color(onErrorContainer),
        outline = Color(outline),
        outlineVariant = Color(outlineVariant),
        scrim = Color(scrim),
        surfaceBright = Color(surfaceBright),
        surfaceDim = Color(surfaceDim),
        surfaceContainer = Color(surfaceContainer),
        surfaceContainerHigh = Color(surfaceContainerHigh),
        surfaceContainerHighest = Color(surfaceContainerHighest),
        surfaceContainerLow = Color(surfaceContainerLow),
        surfaceContainerLowest = Color(surfaceContainerLowest),
        primaryFixed = Color(primaryContainer),
        onPrimaryFixed = Color(onPrimaryContainer),
        primaryFixedDim = Color(primaryContainer),
        onPrimaryFixedVariant = Color(onPrimaryContainer),
        secondaryFixed = Color(secondaryContainer),
        onSecondaryFixed = Color(onSecondaryContainer),
        secondaryFixedDim = Color(secondaryContainer),
        onSecondaryFixedVariant = Color(onSecondaryContainer),
        tertiaryFixed = Color(tertiaryContainer),
        onTertiaryFixed = Color(onTertiaryContainer),
        tertiaryFixedDim = Color(tertiaryContainer),
        onTertiaryFixedVariant = Color(onTertiaryContainer),
    )

fun ColorScheme.pureBlack(apply: Boolean) =
    if (apply) copy(
        surface = Color.Black,
        background = Color.Black
    ) else this

val ColorSaver = object : Saver<Color, Int> {
    override fun restore(value: Int): Color = Color(value)
    override fun SaverScope.save(value: Color): Int = value.toArgb()
}