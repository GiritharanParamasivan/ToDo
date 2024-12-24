package uk.ac.tees.mad.s3216191

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import uk.ac.tees.mad.s3216191.utils.bitmapToBase64
import uk.ac.tees.mad.s3216191.utils.base64ToBitmap
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListPage(
    viewModel: TodoViewModel,
    authViewModel: AuthViewModel,
    navController: NavController,
    themePreferenceManager: ThemePreferenceManager
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val todoList by viewModel.todoList.observeAsState(emptyList())
    var inputText by remember { mutableStateOf("") }
    var editingTodo by remember { mutableStateOf<Todo?>(null) }
    var showThemeDialog by remember { mutableStateOf(false) }
    var capturedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var showPermissionDialog by remember { mutableStateOf(false) }
    var showGDPRDialog by remember { mutableStateOf(true) }
    val snackbarHostState = remember { SnackbarHostState() }

    val context = LocalContext.current
    val activity = context as Activity

    // Camera launcher
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview(),
        onResult = { bitmap ->
            if (bitmap != null) {
                capturedBitmap = bitmap
            }
        }
    )

    // Permission request
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                launcher.launch()
            } else {
                showPermissionDialog = true
            }
        }
    )

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
                onSettingsClick = { showThemeDialog = true },
                themePreferenceManager = themePreferenceManager
            )
        },
        drawerState = drawerState
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                TopAppBar(
                    title = { Text("Todo List") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Open Sidebar")
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    if (ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.CAMERA
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        launcher.launch()
                    } else {
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                }) {
                    Icon(Icons.Filled.Add, contentDescription = "Add Todo")
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
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
                            val base64Image = capturedBitmap?.let { bitmapToBase64(it) }
                            viewModel.addTodo(
                                title = inputText,
                                imageBase64 = base64Image
                            )
                            inputText = ""
                            capturedBitmap = null


                            scope.launch {
                                snackbarHostState.showSnackbar("Task Added")
                            }
                        }
                    }) {
                        Text("Add")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (capturedBitmap != null) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Captured Image:")
                        Image(
                            bitmap = capturedBitmap!!.asImageBitmap(),
                            contentDescription = "Captured Image",
                            modifier = Modifier.size(200.dp)
                        )
                        IconButton(onClick = { capturedBitmap = null }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete Image", tint = Color.Red)
                        }
                    }
                }

                LazyColumn {
                    items(todoList) { item ->
                        TodoItem(
                            item = item,
                            onDelete = { viewModel.deleteTodo(item.id) },
                            onEdit = { editingTodo = it },
                            onToggleImportance = {
                                viewModel.updateImportanceLevel(it, if (it.isImportant) 0 else 1)
                            }
                        )
                    }
                }

                if (editingTodo != null) {
                    EditTodoDialog(
                        todo = editingTodo!!,
                        onDismiss = { editingTodo = null },
                        onSave = { updatedTodo ->
                            viewModel.updateTodoTitle(updatedTodo.id, updatedTodo.title)
                            editingTodo = null
                        }
                    )
                }

                if (showThemeDialog) {
                    ThemeDialog(
                        onDismiss = { showThemeDialog = false },
                        onThemeSelected = { theme ->
                            scope.launch {
                                themePreferenceManager.setThemeMode(theme)
                            }
                            showThemeDialog = false
                        }
                    )
                }

                if (showPermissionDialog) {
                    AlertDialog(
                        onDismissRequest = { showPermissionDialog = false },
                        title = { Text("Permission Required") },
                        text = { Text("Camera permission is required to capture images.") },
                        confirmButton = {
                            TextButton(onClick = { showPermissionDialog = false }) {
                                Text("OK")
                            }
                        }
                    )
                }

                if (showGDPRDialog) {
                    AlertDialog(
                        onDismissRequest = { showGDPRDialog = false },
                        title = { Text("Data Privacy Notice") },
                        text = { Text("This app collects data for providing a personalized experience. Please agree to our privacy policy to continue.") },
                        confirmButton = {
                            TextButton(onClick = { showGDPRDialog = false }) {
                                Text("Agree")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { activity.finish() }) {
                                Text("Decline")
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SidebarContent(
    onSignOut: () -> Unit,
    onNavigateHome: () -> Unit,
    onSettingsClick: () -> Unit,
    themePreferenceManager: ThemePreferenceManager
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
    onEdit: (Todo) -> Unit,
    onToggleImportance: (Todo) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = item.title,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                item.imageBase64?.let {
                    val bitmap = base64ToBitmap(it)
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Todo Image",
                        modifier = Modifier
                            .size(100.dp)
                            .padding(4.dp)
                    )
                }
            }
            Row {
                // Importance toggle button
                IconButton(onClick = { onToggleImportance(item) }) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Toggle Importance",
                        tint = if (item.isImportant) Color.Blue else Color.Gray
                    )
                }

                // Edit button
                IconButton(onClick = { onEdit(item) }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Task",
                        tint = Color.Blue
                    )
                }

                // Delete button
                IconButton(onClick = { onDelete() }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Task",
                        tint = Color.Red
                    )
                }
            }
        }
    }
}

@Composable
fun EditTodoDialog(
    todo: Todo,
    onDismiss: () -> Unit,
    onSave: (Todo) -> Unit
) {
    var updatedTitle by remember { mutableStateOf(todo.title) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Edit Todo") },
        text = {
            OutlinedTextField(
                value = updatedTitle,
                onValueChange = { updatedTitle = it },
                label = { Text("Edit Task Title") }
            )
        },
        confirmButton = {
            TextButton(onClick = {
                onSave(todo.copy(title = updatedTitle))
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun ThemeDialog(
    onDismiss: () -> Unit,
    onThemeSelected: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Choose Theme") },
        text = {
            Column {
                TextButton(onClick = { onThemeSelected("light") }) {
                    Text("Light Theme")
                }
                TextButton(onClick = { onThemeSelected("dark") }) {
                    Text("Dark Theme")
                }
                TextButton(onClick = { onThemeSelected("system") }) {
                    Text("System Default")
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}