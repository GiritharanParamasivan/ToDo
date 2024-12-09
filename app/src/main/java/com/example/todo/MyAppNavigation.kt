package com.example.todo

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todo.pages.HomePage
import com.example.todo.pages.LoginPage
import com.example.todo.pages.SignupPage
import com.example.todo.pages.SplashScreen // Import the SplashScreen
import com.example.todo.pages.TodoListPage
import com.example.todo.preferences.ThemePreferenceManager

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