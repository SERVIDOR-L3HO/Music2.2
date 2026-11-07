package com.maloy.muzza.ui.component

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maloy.muzza.R

@Composable
fun AuraSoundBrandLockup(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(9.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.aurasound_icon),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(11.dp))
        )
        Column(verticalArrangement = Arrangement.Center) {
            Text(
                text = stringResource(R.string.app_name),
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 15.sp,
                lineHeight = 17.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "AUDIO",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 8.sp,
                lineHeight = 10.sp,
                letterSpacing = 2.4.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun AuraSoundSplashScreen(modifier: Modifier = Modifier) {
    val glowTransition = rememberInfiniteTransition(label = "AuraSound splash glow")
    val glowScale by glowTransition.animateFloat(
        initialValue = 0.94f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(1_700, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "AuraSound splash glow scale"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF080A20),
                        Color(0xFF11143B),
                        Color(0xFF211346)
                    )
                )
            )
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        awaitPointerEvent().changes.forEach { it.consume() }
                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = (-145).dp, y = 95.dp)
                .size(340.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color(0xFF596EFF).copy(alpha = 0.30f), Color.Transparent)
                    ),
                    CircleShape
                )
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = 145.dp, y = 110.dp)
                .size(360.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color(0xFF9A53FF).copy(alpha = 0.24f), Color.Transparent)
                    ),
                    CircleShape
                )
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 420.dp)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier
                        .size(286.dp)
                        .graphicsLayer {
                            scaleX = glowScale
                            scaleY = glowScale
                        }
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFF6F7BFF).copy(alpha = 0.25f),
                                    Color(0xFF8D54FF).copy(alpha = 0.10f),
                                    Color.Transparent
                                )
                            ),
                            CircleShape
                        )
                )
                Image(
                    painter = painterResource(R.drawable.aurasound_wordmark),
                    contentDescription = stringResource(R.string.app_name) + " Audio",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth(0.96f)
                        .height(220.dp)
                )
            }

            Spacer(Modifier.height(22.dp))
            AuraSoundWaveform()
            Spacer(Modifier.height(18.dp))
            Text(
                text = stringResource(R.string.aura_splash_tagline),
                color = Color.White.copy(alpha = 0.84f),
                fontSize = 11.sp,
                letterSpacing = 2.2.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun AuraSoundWaveform() {
    val transition = rememberInfiniteTransition(label = "AuraSound waveform")
    Row(
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(7) { index ->
            val scale by transition.animateFloat(
                initialValue = 0.32f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 540,
                        delayMillis = index * 85,
                        easing = FastOutSlowInEasing
                    ),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "AuraSound waveform bar $index"
            )
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height(24.dp)
                    .graphicsLayer { scaleY = scale }
                    .clip(CircleShape)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color(0xFF74D8FF), Color(0xFF9A66FF))
                        )
                    )
            )
        }
    }
}
