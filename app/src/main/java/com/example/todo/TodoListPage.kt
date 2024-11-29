package com.example.todo.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.todo.AuthViewModel
import com.example.todo.Todo
import com.example.todo.TodoViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListPage(
    viewModel: TodoViewModel,
    authViewModel: AuthViewModel,
    navController: NavController
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val todoList by viewModel.todoList.observeAsState(emptyList())
    var inputText by remember { mutableStateOf("") }

    ModalNavigationDrawer(
        drawerContent = {
            SidebarContent(
                onSignOut = {
                    authViewModel.signout()
                    navController.navigate("login") {
                        popUpTo("TodoListPage") { inclusive = true }
                    }
                },
                onNavigateHome = { navController.navigate("home") },
                onSettingsClick = { /* Add navigation here if required */ }
            )
        },
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Todo List") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Open Sidebar")
                        }
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                // Input for adding a new todo
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        label = { Text("Add Todo") },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        if (inputText.isNotBlank()) {
                            viewModel.addTodo(inputText)
                            inputText = ""
                        }
                    }) {
                        Text("Add")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Display the todo list
                LazyColumn {
                    items(todoList) { item ->
                        TodoItem(
                            item = item,
                            onDelete = { viewModel.deleteTodo(item.id) },
                            onLongPressImportance = {
                                viewModel.updateImportanceLevel(it, if (it.isImportant) 0 else 1)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SidebarContent(
    onSignOut: () -> Unit,
    onNavigateHome: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = "Todo App", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))

            TextButton(onClick = { onNavigateHome() }) {
                Text(text = "Home")
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(onClick = { onSettingsClick() }) {
                Text(text = "Settings")
            }
        }

        Column {
            TextButton(onClick = { onSignOut() }) {
                Text(text = "Sign Out")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "App Version 1.0.0",
                fontSize = 12.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun TodoItem(
    item: Todo,
    onDelete: () -> Unit,
    onLongPressImportance: (Todo) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(
                if (item.isImportant) Color.Blue else MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = item.title,
                fontSize = 16.sp,
                color = if (item.isImportant) Color.White else Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Added: ${item.createdAt}", // Display date & time
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
        Row {
            IconButton(onClick = { onLongPressImportance(item) }) {
                Icon(
                    imageVector = Icons.Default.Star, // Replace with appropriate icon
                    contentDescription = "Toggle Importance",
                    tint = if (item.isImportant) Color.Yellow else Color.Gray
                )
            }
            IconButton(onClick = { onDelete() }) {
                Icon(
                    imageVector = Icons.Default.Delete, // Replace with appropriate icon
                    contentDescription = "Delete",
                    tint = Color.Red
                )
            }
        }
    }
}
