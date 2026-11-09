package com.maloy.muzza.ui.screens.settings

import android.content.res.Configuration
import android.os.Build
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.materialkolor.PaletteStyle
import com.materialkolor.rememberDynamicColorScheme
import com.maloy.muzza.R
import com.maloy.muzza.constants.DarkMode
import com.maloy.muzza.constants.DarkModeKey
import com.maloy.muzza.constants.DynamicThemeKey
import com.maloy.muzza.constants.PureBlackKey
import com.maloy.muzza.constants.SelectedThemeColorKey
import com.maloy.muzza.ui.component.SwitchPreference
import com.maloy.muzza.ui.theme.DefaultThemeColor
import com.maloy.muzza.ui.theme.MuzzaTheme
import com.maloy.muzza.utils.rememberEnumPreference
import com.maloy.muzza.utils.rememberPreference

data class ThemePalette(
    val nameRes: Int,
    val seedColor: Color
)

/**
 * A compact, album-sleeve-inspired palette edit. Existing seed colors are retained where
 * possible so already-saved theme preferences continue to resolve to a named choice.
 */
val PaletteColors = listOf(
    ThemePalette(R.string.palette_dynamic, Color.Transparent),
    ThemePalette(R.string.palette_afterglow, Color(0xFFEC5464)),
    ThemePalette(R.string.palette_velvet, Color(0xFF5E35B1)),
    ThemePalette(R.string.palette_blue_note, Color(0xFF3949AB)),
    ThemePalette(R.string.palette_after_rain, Color(0xFF00897B)),
    ThemePalette(R.string.palette_golden_hour, Color(0xFFFFB300)),
    ThemePalette(R.string.palette_side_b, Color(0xFF546E7A))
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeScreen(
    navController: NavController,
) {
    val (darkMode, onDarkModeChange) = rememberEnumPreference(DarkModeKey, DarkMode.AUTO)
    val (pureBlack, onPureBlackChange) = rememberPreference(PureBlackKey, defaultValue = false)
    val (selectedThemeColorInt, onSelectedThemeColorChange) = rememberPreference(
        SelectedThemeColorKey,
        DefaultThemeColor.toArgb()
    )

    val selectedThemeColor = Color(selectedThemeColorInt)
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
    val (_, onDynamicThemeChange) = rememberPreference(DynamicThemeKey, defaultValue = true)
    val handleColorSelection: (Color) -> Unit = { color ->
        onSelectedThemeColorChange(color.toArgb())
        onDynamicThemeChange(color == DefaultThemeColor)
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.theme)) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            painter = painterResource(R.drawable.arrow_back),
                            contentDescription = stringResource(R.string.cd_back)
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        if (isLandscape) {
            LandscapeThemeLayout(
                innerPadding = innerPadding,
                darkMode = darkMode,
                onDarkModeChange = onDarkModeChange,
                pureBlack = pureBlack,
                onPureBlackChange = onPureBlackChange,
                selectedThemeColor = selectedThemeColor,
                onSelectedThemeColorChange = handleColorSelection
            )
        } else {
            PortraitThemeLayout(
                innerPadding = innerPadding,
                darkMode = darkMode,
                onDarkModeChange = onDarkModeChange,
                pureBlack = pureBlack,
                onPureBlackChange = onPureBlackChange,
                selectedThemeColor = selectedThemeColor,
                onSelectedThemeColorChange = handleColorSelection
            )
        }
    }
}

@Composable
fun PortraitThemeLayout(
    innerPadding: PaddingValues,
    darkMode: DarkMode,
    onDarkModeChange: (DarkMode) -> Unit,
    pureBlack: Boolean,
    onPureBlackChange: (Boolean) -> Unit,
    selectedThemeColor: Color,
    onSelectedThemeColorChange: (Color) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .verticalScroll(rememberScrollState())
            .padding(top = 8.dp, bottom = 28.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = stringResource(R.string.theme_preview),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
            ThemeMockupPortrait(
                darkMode = darkMode,
                pureBlack = pureBlack,
                themeColor = selectedThemeColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(270.dp)
            )
        }

        ThemeControls(
            darkMode = darkMode,
            onDarkModeChange = onDarkModeChange,
            pureBlack = pureBlack,
            onPureBlackChange = onPureBlackChange,
            selectedThemeColor = selectedThemeColor,
            onSelectedThemeColorChange = onSelectedThemeColorChange
        )
    }
}

@Composable
fun LandscapeThemeLayout(
    innerPadding: PaddingValues,
    darkMode: DarkMode,
    onDarkModeChange: (DarkMode) -> Unit,
    pureBlack: Boolean,
    onPureBlackChange: (Boolean) -> Unit,
    selectedThemeColor: Color,
    onSelectedThemeColorChange: (Color) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(0.42f)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.theme_preview),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 10.dp, start = 4.dp)
            )
            ThemeMockup(
                darkMode = darkMode,
                pureBlack = pureBlack,
                themeColor = selectedThemeColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 252.dp, max = 330.dp)
            )
        }

        Column(
            modifier = Modifier
                .weight(0.58f)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 20.dp)
        ) {
            ThemeControls(
                darkMode = darkMode,
                onDarkModeChange = onDarkModeChange,
                pureBlack = pureBlack,
                onPureBlackChange = onPureBlackChange,
                selectedThemeColor = selectedThemeColor,
                onSelectedThemeColorChange = onSelectedThemeColorChange,
                compact = true
            )
        }
    }
}

@Composable
fun ThemeControls(
    darkMode: DarkMode,
    onDarkModeChange: (DarkMode) -> Unit,
    pureBlack: Boolean,
    onPureBlackChange: (Boolean) -> Unit,
    selectedThemeColor: Color,
    onSelectedThemeColorChange: (Color) -> Unit,
    compact: Boolean = false
) {
    val (dynamicTheme, onDynamicThemeChange) = rememberPreference(DynamicThemeKey, defaultValue = true)
    val isUsingDynamicColor = selectedThemeColor.toArgb() == DefaultThemeColor.toArgb()
    val selectedPalette = PaletteColors.firstOrNull {
        it.seedColor != Color.Transparent && it.seedColor.toArgb() == selectedThemeColor.toArgb()
    }
    val selectedPaletteName = selectedPalette?.let { stringResource(it.nameRes) }
        ?: if (isUsingDynamicColor) stringResource(R.string.palette_dynamic)
        else stringResource(R.string.palette_custom)
    val paletteOptions = if (!isUsingDynamicColor && selectedPalette == null) {
        listOf(PaletteColors.first(), ThemePalette(R.string.palette_custom, selectedThemeColor)) +
            PaletteColors.drop(1)
    } else {
        PaletteColors
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = if (compact) 0.dp else 16.dp),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(22.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = stringResource(R.string.theme_mode),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 2.dp)
                ) {
                    item {
                        ModeCircle(
                            darkMode = darkMode,
                            pureBlack = pureBlack,
                            targetMode = DarkMode.AUTO,
                            targetPureBlack = false,
                            labelRes = R.string.theme_mode_system,
                            themeColor = selectedThemeColor,
                            onClick = { onDarkModeChange(DarkMode.AUTO); onPureBlackChange(false) }
                        )
                    }
                    item {
                        ModeCircle(
                            darkMode = darkMode,
                            pureBlack = pureBlack,
                            targetMode = DarkMode.OFF,
                            targetPureBlack = false,
                            labelRes = R.string.theme_mode_light,
                            themeColor = selectedThemeColor,
                            onClick = { onDarkModeChange(DarkMode.OFF); onPureBlackChange(false) }
                        )
                    }
                    item {
                        ModeCircle(
                            darkMode = darkMode,
                            pureBlack = pureBlack,
                            targetMode = DarkMode.ON,
                            targetPureBlack = false,
                            labelRes = R.string.theme_mode_dark,
                            themeColor = selectedThemeColor,
                            onClick = { onDarkModeChange(DarkMode.ON); onPureBlackChange(false) }
                        )
                    }
                    item {
                        ModeCircle(
                            darkMode = darkMode,
                            pureBlack = pureBlack,
                            targetMode = DarkMode.ON,
                            targetPureBlack = true,
                            labelRes = R.string.theme_mode_black,
                            themeColor = selectedThemeColor,
                            onClick = { onDarkModeChange(DarkMode.ON); onPureBlackChange(true) }
                        )
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = stringResource(R.string.color_palette),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = selectedPaletteName,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(vertical = 4.dp)
                ) {
                    items(paletteOptions) { palette ->
                        val isDynamicPalette = palette.seedColor == Color.Transparent
                        val isSelected = if (isDynamicPalette) {
                            isUsingDynamicColor
                        } else {
                            selectedThemeColor.toArgb() == palette.seedColor.toArgb()
                        }
                        PaletteItem(
                            palette = palette,
                            isSelected = isSelected,
                            onClick = {
                                onSelectedThemeColorChange(
                                    if (isDynamicPalette) DefaultThemeColor else palette.seedColor
                                )
                            }
                        )
                    }
                }
                if (isUsingDynamicColor) {
                    SwitchPreference(
                        title = { Text(stringResource(R.string.enable_dynamic_theme)) },
                        icon = { Icon(painterResource(R.drawable.palette), contentDescription = null) },
                        checked = dynamicTheme,
                        onCheckedChange = onDynamicThemeChange
                    )
                }
            }
        }
    }
}

@Composable
fun ModeCircle(
    darkMode: DarkMode,
    pureBlack: Boolean,
    targetMode: DarkMode,
    targetPureBlack: Boolean,
    labelRes: Int,
    themeColor: Color,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    val isSystemDark = isSystemInDarkTheme()
    val isSelected = darkMode == targetMode && pureBlack == targetPureBlack
    val effectiveDark = when (targetMode) {
        DarkMode.AUTO -> isSystemDark
        DarkMode.ON -> true
        DarkMode.OFF -> false
    }
    val paletteScheme = rememberDynamicColorScheme(
        seedColor = if (themeColor == DefaultThemeColor) DefaultThemeColor else themeColor,
        isDark = effectiveDark,
        style = PaletteStyle.TonalSpot
    )
    val modeColorScheme = if (
        targetMode == DarkMode.AUTO &&
        themeColor == DefaultThemeColor &&
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    ) {
        if (effectiveDark) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    } else {
        paletteScheme
    }
    val backgroundColor = if (targetPureBlack) Color.Black else modeColorScheme.surface
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.025f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "modeSelectionScale"
    )
    val label = stringResource(labelRes)
    val contentDescription = stringResource(R.string.cd_theme_mode, label)
    val shape = RoundedCornerShape(18.dp)
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = Modifier
            .width(76.dp)
            .height(82.dp)
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .clip(shape)
            .background(
                if (isSelected) MaterialTheme.colorScheme.secondaryContainer
                else MaterialTheme.colorScheme.surfaceContainerLow
            )
            .then(
                if (isSelected) Modifier.border(
                    BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary),
                    shape
                ) else Modifier
            )
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(),
                onClick = onClick
            )
            .semantics {
                this.contentDescription = contentDescription
                selected = isSelected
            }
            .padding(horizontal = 4.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(width = 34.dp, height = 28.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(backgroundColor)
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            when {
                targetMode == DarkMode.AUTO -> Icon(
                    painter = painterResource(R.drawable.sync),
                    contentDescription = null,
                    tint = if (effectiveDark) modeColorScheme.onSurface else modeColorScheme.primary,
                    modifier = Modifier.size(15.dp)
                )
                targetPureBlack -> Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                )
                isSelected -> Icon(
                    painter = painterResource(R.drawable.check),
                    contentDescription = null,
                    tint = modeColorScheme.primary,
                    modifier = Modifier.size(15.dp)
                )
            }
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun PaletteItem(
    palette: ThemePalette,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    val isDynamicPalette = palette.seedColor == Color.Transparent
    val isSystemDark = isSystemInDarkTheme()
    val colorScheme = if (
        isDynamicPalette &&
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    ) {
        if (isSystemDark) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    } else {
        rememberDynamicColorScheme(
            seedColor = if (isDynamicPalette) DefaultThemeColor else palette.seedColor,
            isDark = isSystemDark,
            style = PaletteStyle.TonalSpot
        )
    }
    val paletteName = stringResource(palette.nameRes)
    val contentDesc = stringResource(R.string.cd_palette_item, paletteName)
    val shape = RoundedCornerShape(20.dp)
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.015f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "paletteSelectionScale"
    )
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = Modifier
            .width(136.dp)
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .clip(shape)
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .then(
                if (isSelected) Modifier.border(
                    BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary),
                    shape
                ) else Modifier.border(1.dp, MaterialTheme.colorScheme.outlineVariant, shape)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(),
                onClick = onClick
            )
            .semantics {
                contentDescription = contentDesc
                selected = isSelected
            }
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(42.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(colorScheme.surfaceContainer)
                .clipToBounds()
        ) {
            Row(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .weight(1.15f)
                        .fillMaxHeight()
                        .background(colorScheme.primary)
                )
                Column(modifier = Modifier.weight(0.85f).fillMaxHeight()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .background(colorScheme.secondary)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .background(colorScheme.tertiary)
                    )
                }
            }
            Box(
                modifier = Modifier
                    .padding(7.dp)
                    .size(10.dp)
                    .align(Alignment.BottomStart)
                    .clip(CircleShape)
                    .background(colorScheme.primaryContainer)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = paletteName,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 16.sp
            )
            if (isSelected) {
                Icon(
                    painter = painterResource(R.drawable.check),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
fun ThemeMockup(
    darkMode: DarkMode,
    pureBlack: Boolean,
    themeColor: Color,
    modifier: Modifier = Modifier
) {
    ThemePreviewCard(darkMode, pureBlack, themeColor, modifier)
}

@Composable
fun ThemeMockupPortrait(
    darkMode: DarkMode,
    pureBlack: Boolean,
    themeColor: Color,
    modifier: Modifier = Modifier
) {
    ThemePreviewCard(darkMode, pureBlack, themeColor, modifier)
}

@Composable
private fun ThemePreviewCard(
    darkMode: DarkMode,
    pureBlack: Boolean,
    themeColor: Color,
    modifier: Modifier
) {
    val isSystemDark = isSystemInDarkTheme()
    val useDark = when (darkMode) {
        DarkMode.AUTO -> isSystemDark
        DarkMode.ON -> true
        DarkMode.OFF -> false
    }
    val paletteName = PaletteColors.firstOrNull {
        it.seedColor != Color.Transparent && it.seedColor.toArgb() == themeColor.toArgb()
    }?.let { stringResource(it.nameRes) }
        ?: if (themeColor == DefaultThemeColor) stringResource(R.string.palette_dynamic)
        else stringResource(R.string.palette_custom)
    val previewDescription = stringResource(R.string.cd_theme_preview, paletteName)

    MuzzaTheme(
        darkTheme = useDark,
        pureBlack = pureBlack,
        themeColor = themeColor
    ) {
        Card(
            modifier = modifier.semantics { contentDescription = previewDescription },
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                    )
                    Text(
                        text = stringResource(R.string.theme_preview_now_playing),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "AuraSound",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AlbumSleeveArtwork(
                        modifier = Modifier
                            .size(104.dp)
                            .clip(RoundedCornerShape(18.dp))
                    )
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.theme_preview_track),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = stringResource(R.string.theme_preview_artist),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = stringResource(R.string.theme_preview_album),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(3.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.42f)
                                .fillMaxHeight()
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary)
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("0:42", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("−2:18", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.theme_preview_queue),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        val waveColor = MaterialTheme.colorScheme.onPrimaryContainer
                        Canvas(modifier = Modifier.size(14.dp)) {
                            val barWidth = size.width * 0.16f
                            val gaps = size.width * 0.18f
                            val heights = listOf(0.38f, 0.82f, 0.58f)
                            heights.forEachIndexed { index, fraction ->
                                val x = index * (barWidth + gaps) + barWidth / 2
                                drawLine(
                                    color = waveColor,
                                    start = androidx.compose.ui.geometry.Offset(x, size.height * (1f - fraction) / 2f),
                                    end = androidx.compose.ui.geometry.Offset(x, size.height * (1f + fraction) / 2f),
                                    strokeWidth = barWidth,
                                    cap = androidx.compose.ui.graphics.StrokeCap.Round
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AlbumSleeveArtwork(modifier: Modifier = Modifier) {
    val scheme = MaterialTheme.colorScheme
    Canvas(modifier = modifier.background(scheme.primaryContainer)) {
        drawCircle(
            color = scheme.primary,
            radius = size.minDimension * 0.43f,
            center = center
        )
        drawCircle(
            color = scheme.tertiary,
            radius = size.minDimension * 0.27f,
            center = center
        )
        drawCircle(
            color = scheme.secondaryContainer,
            radius = size.minDimension * 0.11f,
            center = center
        )
        drawLine(
            color = scheme.onPrimaryContainer.copy(alpha = 0.72f),
            start = androidx.compose.ui.geometry.Offset(size.width * 0.12f, size.height * 0.8f),
            end = androidx.compose.ui.geometry.Offset(size.width * 0.88f, size.height * 0.2f),
            strokeWidth = size.minDimension * 0.035f,
            cap = androidx.compose.ui.graphics.StrokeCap.Round
        )
        drawRect(
            color = scheme.onPrimaryContainer.copy(alpha = 0.45f),
            topLeft = androidx.compose.ui.geometry.Offset(size.width * 0.12f, size.height * 0.12f),
            size = androidx.compose.ui.geometry.Size(size.width * 0.18f, size.height * 0.18f)
        )
    }
}
