package uk.ac.tees.mad.s3216191.pages

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import uk.ac.tees.mad.s3216191.R


@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    var showWelcomeMessage by remember { mutableStateOf(false) }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Welcome Message
            Text(text = "Welcome!", fontSize = 32.sp)

            Spacer(modifier = Modifier.height(16.dp))


            PulsingLogo(navController)
        }
    }
}
// Pulsing Logo
@Composable
fun PulsingLogo(navController: NavController) {

    val scale = rememberInfiniteTransition().animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )


    Image(
        painter = painterResource(id = R.drawable.todo_logo),
        contentDescription = "Todo App Logo",
        modifier = Modifier
            .size(150.dp)
            .scale(scale.value)
            .clickable {
                navController.navigate("TodoListPage")
            }
    )
}
