package com.example.easy_life

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.easy_life.data.local.Theme
import com.example.easy_life.data.local.getTheme
import com.example.easy_life.data.local.getfontSize
import com.example.easy_life.data.model.TaskNode
import com.example.easy_life.ui.screen.AddTaskScreen
import com.example.easy_life.ui.screen.AppStart
import com.example.easy_life.ui.screen.EditTaskScreen
import com.example.easy_life.ui.screen.home.HomeScreen
import com.example.easy_life.ui.screen.TaskInfoScreen
import com.example.easy_life.ui.theme.TaskListTheme
import kotlinx.serialization.json.Json

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TaskListTheme {
                TaskListApp()
            }
        }
    }
}

@Composable
fun TaskListApp(modifier: Modifier = Modifier) {
    val navController: NavHostController = rememberNavController()
    val context = LocalContext.current
    val themeMode by getTheme(context).collectAsState(initial = "system")
    val preferFontSize by getfontSize(context).collectAsState(initial = 14)

    val theme = when (themeMode) {
        "dark" -> Theme.DARKTHEME
        "light" -> Theme.LIGHTTHEME
        else -> Theme.LIGHTTHEME
    }

    // Navigation address and options
    NavHost(
        navController = navController,
        startDestination = "app_start",
        enterTransition = { slideInHorizontally(
            initialOffsetX = { -it },
            animationSpec = tween(250)
        )  },
        popEnterTransition = { slideInHorizontally(
            initialOffsetX = { -it },
            animationSpec = tween(250)
        ) },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = {-it},
                animationSpec = tween(250)
            )
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = {-it},
                animationSpec = tween(250)
            )
        },
        modifier = Modifier.background(Color(0xFF1E1E1E))
    ) {
        composable("app_start") { AppStart(
            theme = theme,
            navController = navController) }
        composable("home") { HomeScreen(
            theme = theme,
            navController = navController
        ) }
        composable("addTaskScreen") { AddTaskScreen(
            theme = theme,
            navController = navController)
        }
        composable(
            route = "taskInfoScreen/{taskData}",
            arguments = listOf(navArgument("taskData") { type = NavType.StringType })
        ) { backStackEntry ->
            val taskUri = backStackEntry.arguments?.getString("taskData")
            val taskUriDecoded = Uri.decode(taskUri.toString())
            val taskData = Json.decodeFromString<TaskNode>(taskUriDecoded)

            TaskInfoScreen(
                theme = theme,
                taskNode = taskData,
                navController = navController
            )
        }
        composable(
            route = "editTaskScreen/{taskData}",
            arguments = listOf(navArgument("taskData") { type = NavType.StringType })
        ) { backStackEntry ->
            val taskUri = backStackEntry.arguments?.getString("taskData")
            val taskUriDecoded = Uri.decode(taskUri.toString())
            val taskData = Json.decodeFromString<TaskNode>(taskUriDecoded)

            EditTaskScreen(
                theme = theme,
                taskNode = taskData,
                navController = navController
            )
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    name = "Task List HomeScreen"
)
@Composable
fun HomeScreenPreview() {
    TaskListTheme {
        HomeScreen(navController = rememberNavController())
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    name = "Task List HomeScreen")
@Composable
fun AddTaskScreenPreview() {
    TaskListTheme {
        AddTaskScreen(navController = rememberNavController())
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    name = "Task List HomeScreen")
@Composable
fun TaskInfoScreenPreview() {
    TaskListTheme {
        TaskInfoScreen(
            taskNode = TaskNode(
                title = "Sample Task",
                description = "Idk what to write here",
                deadline = "1/1/2026",
                id = "1",
                status = "ONGOING"
            ),
            navController = rememberNavController()
        )
    }
}