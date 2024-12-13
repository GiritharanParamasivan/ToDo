package uk.ac.tees.mad.S3216191

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import uk.ac.tees.mad.S3216191.pages.HomePage
import uk.ac.tees.mad.S3216191.pages.LoginPage
import uk.ac.tees.mad.S3216191.pages.SignupPage
import uk.ac.tees.mad.S3216191.pages.SplashScreen // Import the SplashScreen

@Composable
fun MyAppNavigation(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    todoViewModel: TodoViewModel,
    themePreferenceManager: ThemePreferenceManager
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "splash") { // Start at splash
        // Splash Screen
        composable("splash") {
            SplashScreen(navController = navController)
        }

        // Login Screen
        composable("login") {
            LoginPage(
                modifier = modifier,
                navController = navController,
                authViewModel = authViewModel
            )
        }

        // Signup Screen
        composable("signup") {
            SignupPage(
                modifier = modifier,
                navController = navController,
                authViewModel = authViewModel
            )
        }

        // Home Screen
        composable("home") {
            HomePage(
                modifier = modifier,
                navController = navController
            )
        }

        // Todo List Screen
        composable("TodoListPage") {
            TodoListPage(
                viewModel = todoViewModel,
                authViewModel = authViewModel,
                navController = navController,
                themePreferenceManager = themePreferenceManager
            )
        }
    }
}