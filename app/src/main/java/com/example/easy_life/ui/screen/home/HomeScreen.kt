package com.example.easy_life.ui.screen.home

import android.net.Uri
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.easy_life.R
import com.example.easy_life.data.model.TaskNode
import com.example.easy_life.functions.getTotal
import com.example.easy_life.functions.toMonthName
import com.example.easy_life.ui.components.DateBanner
import com.example.easy_life.ui.components.StatusIndicator
import com.example.easy_life.ui.components.StatusIndicatorBar
import com.example.easy_life.ui.model.StatusType
import com.example.easy_life.ui.theme.TaskListTheme
import kotlinx.coroutines.delay
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen (
    navController: NavController,
    homeViewModel: HomeViewModel = HomeViewModel()
) {
    // Formatter - e.g. Sat/08/10/2025
    val formatter = SimpleDateFormat("E/MM/dd/yyyy", Locale.getDefault())
    // Gets current date using the declared formatter
    val currentDate = formatter.format(Date())

    val context = LocalContext.current // Get the app context
    homeViewModel.refresh(currentDate,context) // Refresh the task container
    val listOfTask = homeViewModel.taskList // Load the content from viewmodel

    Scaffold (
        modifier = Modifier.statusBarsPadding(),
        topBar = {
            NavBar(
                toAddScreen = { navController.navigate("addTaskScreen") },
                modifier = Modifier
                    .background(Color(0xFF1E1E1E))
                    .fillMaxWidth()
            )
        }
    ) { innerPadding ->
        HomeScreenBody(
            currentDate = currentDate,
            listOfTask = listOfTask,
            navController = navController,
            Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun HomeScreenBody (
    currentDate: String,
    listOfTask: List<TaskNode>,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Column (
        modifier = modifier
            .background(Color(0xFF1E1E1E))
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        TopBanner(
            date = currentDate,
            workDone = getTotal(listOfTask,"DONE"),
            workNotDone = getTotal(listOfTask,"MISSED"),
            workOngoing = getTotal(listOfTask,"ONGOING"),
            modifier = Modifier.weight(1.2f)
        )
        TaskLists(
            toTaskInfoScreen = {
                    taskData ->
                navController.navigate("taskInfoScreen/${taskData}")

            },
            listOfTask = listOfTask,
            modifier = Modifier
                .background(Color.Transparent)
                .weight(8f)
        )
    }
}

@Composable
fun NavBar(
    toAddScreen: () -> Unit, // Function to go AddTaskScreen
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .padding(horizontal = 15.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Logo
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = stringResource(R.string.logo_image_desc_txt),
                modifier = Modifier.size(50.dp)
            )
            Text (
                text = stringResource(R.string.tasklist_header_txt),
                color = Color.White
            )
        }
        var clickOnce by remember {mutableStateOf(true)}
        // Button to go to AddTaskScreen
        Button(
            onClick = {
                if (!clickOnce) {return@Button}
                clickOnce = false
                toAddScreen()
            },
            colors = buttonColors(
                contentColor = Color.Transparent,
                containerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = Color.Transparent
            ),
            enabled = clickOnce,
            contentPadding = PaddingValues(15.dp),
            shape = RoundedCornerShape(0.dp),
            modifier = Modifier.size(50.dp)
        ) {
            // Add icon
            Image(
                painter = painterResource(R.drawable.add_icon),
                contentDescription = stringResource(R.string.add_icon_desc_txt),
                modifier = Modifier.size(30.dp)
            )
        }
    }
}

@Composable
fun TopBanner(
    date: String,
    workDone: Int,
    workNotDone: Int,
    workOngoing: Int,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .padding(vertical = 10.dp)
            .fillMaxSize()
    ) {
        DateBanner(
            date = date,
            modifier = Modifier.weight(1f)
        )
        StatusIndicatorBar(
            workDone = workDone,
            workNotDone = workNotDone,
            workOngoing = workOngoing,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun TaskLists(
    toTaskInfoScreen: (String) -> Unit,
    listOfTask: List<TaskNode>,
    modifier: Modifier = Modifier
) {
    var delay by remember { mutableLongStateOf(200) }

    Column (
        modifier = modifier
            .padding(30.dp)
            .fillMaxSize()
    ) {
        // Header
        // Container for header, horizontally arranged
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.tasks_txt), // Header title
                color = Color.White,
                fontSize = 20.sp
            )
            Text(
                text = "${listOfTask.size}", // Shows number of tasks
                color = Color.White
            )
        }

        // Tasks list
        // Container for lists of tasks, vertically arranged
        Column (
            modifier = Modifier
                .padding(
                    vertical = 30.dp,
                    horizontal = 5.dp
                )
                .verticalScroll(rememberScrollState())
        ) {
            // Load all the tasks
            if (listOfTask.isEmpty()) { // If no tasks found
                Text(
                    text = stringResource(R.string.no_ongoing_tasks_txt),
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth())
            } else {
                listOfTask.forEach { task ->
                    TaskTab(
                        delay = delay,
                        taskNode = task, // Compose tasks
                        toTaskInfoScreen = { toTaskInfoScreen(it) }
                    )
                    delay += 25
                }
            }
        }
    }
}

@Composable
fun TaskTab(
    delay: Long,
    toTaskInfoScreen: (String) -> Unit,
    taskNode: TaskNode,
    modifier: Modifier = Modifier
) {
    var fadeIn by remember {mutableStateOf(false)}
    val offsetX by animateDpAsState(
        targetValue = if (fadeIn) 0.dp else -(500.dp),
        animationSpec = tween(durationMillis = 400),
        label = "fade in"
    )

    LaunchedEffect(Unit) {
        delay(delay)
        fadeIn = true
    }

    var clickOnce by remember {mutableStateOf(true)}

    Surface (
        onClick = {
            if (!clickOnce) {return@Surface}
            clickOnce = false
            val taskData = Uri.encode(Json.encodeToString(taskNode))
            toTaskInfoScreen(taskData) // Function to go to TaskInfoScreen
        },
        enabled = clickOnce,
        color = Color.Transparent,
        modifier = modifier
            .offset {
                IntOffset(offsetX.roundToPx(), 0)
            }
            .padding(15.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 15.dp)
                .fillMaxWidth()
        ) {

            // Left Part
            // title and deadline
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start,
                modifier = modifier.weight(1f)
            ) {
                Text(
                    text = taskNode.title,
                    color = Color.White
                )
                Text(
                    text = "Until ${toMonthName(taskNode.deadline.split("/")[0])} ${
                        taskNode.deadline.split("/")[1]
                    } ${
                        taskNode.deadline.split("/")[2]
                    }",
                    color = Color.White,
                    fontSize = 12.sp
                )
            }

            // Right Part
            // Status
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.End,
                modifier = modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(R.string.status_txt),
                    color = Color.White
                )

                StatusIndicator(
                    statusType = StatusType.valueOf(taskNode.status)
                )
            }
        }
    }
    HorizontalDivider(
        thickness = 1.dp,
        color = Color.White
    )
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    name = "Homescreen"
)
@Composable
fun HomeScreenPreview() {
    TaskListTheme {
        HomeScreen(navController = rememberNavController())
    }
}