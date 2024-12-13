package uk.ac.tees.mad.S3216191

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import uk.ac.tees.mad.S3216191.ui.theme.ToDoTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val themePreferenceManager = ThemePreferenceManager(this)

        val authViewModel: AuthViewModel by viewModels()
        val todoViewModel: TodoViewModel by viewModels()

        setContent {
            ToDoTheme(themeMode = themePreferenceManager.themeMode.collectAsState(initial = "system").value) {
                MainContent(
                    authViewModel = authViewModel,
                    todoViewModel = todoViewModel,
                    themePreferenceManager = themePreferenceManager
                )
            }
        }
    }
}

@Composable
fun MainContent(
    authViewModel: AuthViewModel,
    todoViewModel: TodoViewModel,
    themePreferenceManager: ThemePreferenceManager
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = { innerPadding ->
            MyAppNavigation(
                modifier = Modifier.padding(innerPadding),
                authViewModel = authViewModel,
                todoViewModel = todoViewModel,
                themePreferenceManager = themePreferenceManager
            )
        }
    )
}