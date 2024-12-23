package uk.ac.tees.mad.s3216191.pages

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RadialGradientShader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import uk.ac.tees.mad.s3216191.R


@Composable
fun SplashScreen(navController: NavController) {

    val infiniteRotation = rememberInfiniteTransition()
    val rotation by infiniteRotation.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    // Pulsating animation
    val infinitePulse = rememberInfiniteTransition()
    val pulseRadius by infinitePulse.animateFloat(
        initialValue = 100f,
        targetValue = 200f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )


    LaunchedEffect(key1 = true) {
        delay(2000)
        navController.navigate("login") {
            popUpTo("splash") { inclusive = true }
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        // Pulsating Lightning Effect
        Canvas(modifier = Modifier.fillMaxSize(), onDraw = {
            drawCircle(
                brush = ShaderBrush(
                    RadialGradientShader(
                        colors = listOf(Color(0xFFE3F2FD), Color.Transparent),
                        center = center,
                        radius = pulseRadius
                    )
                ),
                center = center,
                radius = pulseRadius
            )
        })

        // Rotating Logo
        Image(
            painter = painterResource(id = R.drawable.todo_logo),
            contentDescription = "Rotating Logo",
            modifier = Modifier
                .size(150.dp)
                .graphicsLayer(rotationZ = rotation)
        )
    }
}
