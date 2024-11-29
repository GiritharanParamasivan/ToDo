package com.example.todo

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todo.pages.HomePage
import com.example.todo.pages.LoginPage
import com.example.todo.pages.SignupPage
import com.example.todo.pages.TodoListPage

@Composable
fun MyAppNavigation(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    todoViewModel: TodoViewModel = TodoViewModel() // Pass the TodoViewModel
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginPage(
                modifier = modifier,
                navController = navController,
                authViewModel = authViewModel
            )
        }
        composable("signup") {
            SignupPage(
                modifier = modifier,
                navController = navController,
                authViewModel = authViewModel
            )
        }
        composable("home") {
            HomePage(
                modifier = modifier,
                navController = navController
            )
        }

        composable("TodoListPage") {
            TodoListPage(
                viewModel = todoViewModel,
                authViewModel = authViewModel,
                navController = navController
            )
        }
    }
}
