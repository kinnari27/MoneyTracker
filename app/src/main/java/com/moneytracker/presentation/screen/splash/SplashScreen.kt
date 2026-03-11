package com.moneytracker.presentation.screen.splash

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moneytracker.ui.theme.MoneyTrackerTheme
import com.moneytracker.ui.theme.Primary
import com.moneytracker.ui.theme.PrimaryDark
import com.moneytracker.ui.theme.PrimaryLight

// ── Entry point ───────────────────────────────────────────────────────────────
@Composable
fun SplashScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToHome: () -> Unit,
    viewModel: SplashViewModel = hiltViewModel()
) {
    val destination by viewModel.destination.collectAsStateWithLifecycle()

    LaunchedEffect(destination) {
        when (destination) {
            SplashDestination.Login -> onNavigateToLogin()
            SplashDestination.Register -> onNavigateToRegister()
            SplashDestination.Home -> onNavigateToHome()
            SplashDestination.Loading -> Unit
        }
    }

    SplashScreenContent()
}

// ── Stateless content ─────────────────────────────────────────────────────────
@Composable
fun SplashScreenContent() {
    val infiniteTransition = rememberInfiniteTransition(label = "splash")

    // Logo pulse
    val logoScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            tween(1200, easing = EaseInOut), RepeatMode.Reverse
        ),
        label = "logo_scale"
    )

    // Outer ring pulse
    val ringScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.18f,
        animationSpec = infiniteRepeatable(
            tween(1600, easing = EaseInOut), RepeatMode.Reverse
        ),
        label = "ring_scale"
    )

    // Tagline shimmer
    val shimmerAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(900, easing = EaseInOut), RepeatMode.Reverse
        ),
        label = "shimmer"
    )

    // Three bouncing loading dots — staggered delays
    val dotOffset1 by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = -10f,
        animationSpec = infiniteRepeatable(
            tween(500, easing = EaseInOut, delayMillis = 0), RepeatMode.Reverse
        ),
        label = "dot1"
    )
    val dotOffset2 by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = -10f,
        animationSpec = infiniteRepeatable(
            tween(500, easing = EaseInOut, delayMillis = 160), RepeatMode.Reverse
        ),
        label = "dot2"
    )
    val dotOffset3 by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = -10f,
        animationSpec = infiniteRepeatable(
            tween(500, easing = EaseInOut, delayMillis = 320), RepeatMode.Reverse
        ),
        label = "dot3"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(PrimaryDark, Primary, PrimaryLight)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // ── Decorative background circles ─────────────────────────────────────
        Box(
            modifier = Modifier
                .size(280.dp)
                .align(Alignment.TopEnd)
                .offset(x = 60.dp, y = (-60).dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.05f))
        )
        Box(
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.BottomStart)
                .offset(x = (-50).dp, y = 50.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.04f))
        )

        // ── Centre content ────────────────────────────────────────────────────
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            // Logo with pulsing ring
            Box(contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .scale(ringScale)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.08f))
                )
                Box(
                    modifier = Modifier
                        .size(112.dp)
                        .scale(logoScale)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(Color.White.copy(0.25f), Color.White.copy(0.10f))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "₹",
                        fontSize = 52.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                }
            }

            Spacer(Modifier.height(32.dp))

            Text(
                text = "MoneyTracker",
                fontSize = 34.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                letterSpacing = 0.5.sp
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Smart spending. Better living.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = shimmerAlpha),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(16.dp))

            // Version badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(99.dp))
                    .background(Color.White.copy(alpha = 0.15f))
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "v 1.0.0",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(0.8f),
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(Modifier.height(80.dp))

            // Bouncing loading dots
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                listOf(dotOffset1, dotOffset2, dotOffset3).forEach { offset ->
                    Box(
                        modifier = Modifier
                            .offset(y = offset.dp)
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.8f))
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Loading…",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(0.5f)
            )
        }

        // Bottom branding
        Text(
            text = "MoneyTracker v1.0.0",
            style = MaterialTheme.typography.labelSmall,
            color = Color.White.copy(0.3f),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
        )
    }
}

// ── Previews ──────────────────────────────────────────────────────────────────
@Preview(name = "Splash — Light", showBackground = true)
@Composable
private fun SplashPreviewLight() {
    MoneyTrackerTheme(darkTheme = false) { SplashScreenContent() }
}

@Preview(name = "Splash — Dark", showBackground = true)
@Composable
private fun SplashPreviewDark() {
    MoneyTrackerTheme(darkTheme = true) { SplashScreenContent() }
}