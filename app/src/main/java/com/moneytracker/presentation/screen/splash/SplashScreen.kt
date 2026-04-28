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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moneytracker.ui.theme.Indigo400
import com.moneytracker.ui.theme.Indigo600
import com.moneytracker.ui.theme.Slate400
import com.moneytracker.ui.theme.Slate900
import com.moneytracker.ui.theme.White
import com.moneytracker.ui.theme.moneytrackerTheme

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
            SplashDestination.Login    -> onNavigateToLogin()
            SplashDestination.Register -> onNavigateToRegister()
            SplashDestination.Home     -> onNavigateToHome()
            SplashDestination.Loading  -> Unit
        }
    }

    SplashScreenContent()
}

// ── Stateless content ─────────────────────────────────────────────────────────
@Composable
fun SplashScreenContent() {

    val infiniteTransition = rememberInfiniteTransition(label = "splash")

    // Three loading bars animate with staggered delays
    val bar1 by infiniteTransition.animateFloat(
        initialValue = 0.3f, targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(700, easing = EaseInOut, delayMillis = 0), RepeatMode.Reverse
        ),
        label = "bar1"
    )
    val bar2 by infiniteTransition.animateFloat(
        initialValue = 0.3f, targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(700, easing = EaseInOut, delayMillis = 200), RepeatMode.Reverse
        ),
        label = "bar2"
    )
    val bar3 by infiniteTransition.animateFloat(
        initialValue = 0.3f, targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(700, easing = EaseInOut, delayMillis = 400), RepeatMode.Reverse
        ),
        label = "bar3"
    )

    // Tagline subtle fade
    val taglineAlpha by infiniteTransition.animateFloat(
        initialValue = 0.45f, targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            tween(1400, easing = EaseInOut), RepeatMode.Reverse
        ),
        label = "tagline"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Slate900),   // flat navy — no gradient, no circles
        contentAlignment = Alignment.Center
    ) {

        // ── Centre content ────────────────────────────────────────────────────
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 40.dp)
        ) {

            // Logo mark — rounded square, not a circle bubble
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Indigo600),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "₹",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = White
                )
            }

            Spacer(Modifier.height(28.dp))

            Text(
                text = "MoneyTracker",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = White,
                letterSpacing = 0.sp
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Track smarter. Save better.",
                style = MaterialTheme.typography.bodyMedium,
                color = White.copy(alpha = taglineAlpha),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(12.dp))

            // Version badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(White.copy(alpha = 0.08f))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "v 1.0.0",
                    style = MaterialTheme.typography.labelSmall,
                    color = Slate400
                )
            }

            Spacer(Modifier.height(72.dp))

            // Animated bar loader — unique, not dots or spinner
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AnimatedBar(fraction = bar1, color = Indigo600)
                AnimatedBar(fraction = bar2, color = Indigo400)
                AnimatedBar(fraction = bar3, color = Indigo600)
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Loading…",
                style = MaterialTheme.typography.labelSmall,
                color = Slate400
            )
        }

        // Bottom branding
        Text(
            text = "MoneyTracker v1.0.0",
            style = MaterialTheme.typography.labelSmall,
            color = White.copy(alpha = 0.18f),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
        )
    }
}

// ── Reusable animated loading bar ─────────────────────────────────────────────
@Composable
private fun AnimatedBar(fraction: Float, color: Color) {
    Box(
        modifier = Modifier
            .width((24 * fraction).dp.coerceAtLeast(8.dp))
            .height(4.dp)
            .clip(RoundedCornerShape(2.dp))
            .background(color.copy(alpha = 0.35f + 0.65f * fraction))
    )
}

// ── Previews ──────────────────────────────────────────────────────────────────
@Preview(name = "Splash — Light", showBackground = true)
@Composable
private fun SplashPreviewLight() {
    moneytrackerTheme(darkTheme = false) { SplashScreenContent() }
}

@Preview(name = "Splash — Dark", showBackground = true)
@Composable
private fun SplashPreviewDark() {
    moneytrackerTheme(darkTheme = true) { SplashScreenContent() }
}