package com.example.easy_life.ui.screen

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.easy_life.R
import com.example.easy_life.data.local.Theme
import com.example.easy_life.data.model.TaskNode
import com.example.easy_life.functions.deleteTask
import com.example.easy_life.functions.markTaskDone
import com.example.easy_life.functions.toMonthName
import com.example.easy_life.ui.components.StatusIndicator
import com.example.easy_life.ui.model.StatusType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Composable
fun TaskInfoScreen(
    theme: Theme = Theme.LIGHTTHEME,
    taskNode: TaskNode,
    navController: NavController,
) {
    val context = LocalContext.current

    Scaffold (
        modifier = Modifier.statusBarsPadding(),
        topBar = {
            TaskInfoNavBar(
                theme =  theme,
                backFunction = { navController.popBackStack() },
            )
        }
    ) { innerPadding ->
        TaskInfoBody(
            theme = theme,
            modifier = Modifier.padding(innerPadding),
            taskNode = taskNode,
            context = context,
            navController = navController
        )
    }
}

@Composable
fun TaskInfoNavBar(
    theme: Theme,
    backFunction: () -> Unit, // Back callback function
    modifier: Modifier = Modifier
) {
    Row (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = modifier
            .background(theme.backgroundColor)
            .padding(horizontal = 15.dp)
            .fillMaxWidth()
    ) {
        var clickOnce by remember { mutableStateOf(true) }
        // Back button
        Button (
            onClick = {
                if (!clickOnce) {return@Button}
                clickOnce = false
                backFunction()
            }, // Use back function
            colors = buttonColors(
                contentColor = Color.Transparent,
                containerColor = Color.Transparent,
                disabledContentColor = Color.Transparent,
                disabledContainerColor = Color.Transparent
            ),
            enabled = clickOnce,
            contentPadding = PaddingValues(15.dp),
            shape = RoundedCornerShape(0.dp),
            modifier = Modifier.size(50.dp)
        ) {
            // Back icon
            Image (
                painter = painterResource(theme.backIcon),
                contentDescription = stringResource(R.string.back_icon_desc_txt),
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(30.dp)
            )
        }
    }
}

@Composable
fun TaskInfoBody(
    theme: Theme,
    taskNode: TaskNode,
    context: Context,
    navController: NavController,
    modifier: Modifier = Modifier
) {

    // Screen container, vertically placed
    // Container for task info, vertically placed
    Column (
        modifier = modifier
            .background(theme.backgroundColor)
            .padding(
                vertical = 60.dp,
                horizontal = 15.dp
            )
            .fillMaxSize()
    ) {
        Header(
            theme = theme,
            taskNode = taskNode)
        Spacer(Modifier.height(30.dp))
        DescriptionBox(
            theme = theme,
            taskNode = taskNode)
        BottomButtons(
            theme = theme,
            taskNode = taskNode,
            markDone = {
                markTaskDone(
                    id = taskNode.id.toInt(),
                    context = context
                )
                navController.popBackStack()
            },
            deleteTask = {
                deleteTask(
                    id = taskNode.id,
                    context = context
                )
                navController.popBackStack()
            },
            toEditTaskScreen = { taskData ->
                navController.navigate("editTaskScreen/${taskData}")
            }
        )
    }
}

// Contains title, deadline, and status
@Composable
fun Header(
    theme: Theme,
    taskNode: TaskNode,
) {
    Row (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Title and deadline
        Column(
            modifier = Modifier.weight(1.4f)
        ) {
            Text(
                text = taskNode.title,
                color = theme.fontColor,
                fontSize = 35.sp,
                maxLines = 2,
                overflow = TextOverflow.Clip,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(10.dp))
            Text (
                text = "Until ${toMonthName(taskNode.deadline.split("/")[0])} ${
                    taskNode.deadline.split("/")[1]} ${
                    taskNode.deadline.split("/")[2]}",
                fontSize = 22.sp,
               color = theme.fontColor
            )
        }
        // Current status
        Column (
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.weight(0.6f)

        ) {
            StatusIndicator(
                theme = theme,
                statusType = StatusType.valueOf(taskNode.status)
            )
        }
    }
}

@Composable
fun DescriptionBox(
    theme: Theme,
    taskNode: TaskNode,
) {
    Column(
        modifier = Modifier.padding(15.dp)
    ) {
        Text(
            text = stringResource(R.string.description_header_txt),
            color = theme.fontColor,
            fontSize = 22.sp
        )
        Spacer(Modifier.height(20.dp))
        Text(
            text = taskNode.description,
            color = theme.fontColor,
            fontSize = 14.sp,
            modifier = Modifier
                .heightIn(min = 150.dp, max = 450.dp) // set min and max height
                .padding(horizontal = 10.dp)
                .verticalScroll(rememberScrollState())
        )
    }
}

// Contains delete, done, edit
@Composable
fun BottomButtons(
    theme: Theme,
    toEditTaskScreen: (String) -> Unit,
    taskNode: TaskNode,
    markDone: () -> Unit,
    deleteTask: () -> Unit,
    modifier: Modifier = Modifier
) {
    var clickOnce by remember { mutableStateOf(true) }

    Row (
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .padding(horizontal = 10.dp)
            .fillMaxWidth()
    ) {
        // Delete Button
        Button (
            onClick = { deleteTask() },
            colors = buttonColors(
                contentColor = Color(0xFFC93334),
                containerColor = Color(0xFFC93334),
                disabledContainerColor = Color.Transparent,
                disabledContentColor = Color.Transparent
            ),
            contentPadding = PaddingValues(10.dp)
        ) {
            Text(
                text = stringResource(R.string.delete_button_txt),
                color = theme.fontColor,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
            )
        }

        // Done Button
        Button(
            onClick = {
                if (!clickOnce) {return@Button}
                clickOnce = false
                markDone()
            },
            colors = buttonColors(
                contentColor = Color(0xFF547A3d),
                containerColor = Color(0xFF547A3d),
                disabledContainerColor = Color.Transparent,
                disabledContentColor = Color.Transparent
            ),
            enabled = taskNode.status == "ONGOING" && clickOnce,
            contentPadding = PaddingValues(10.dp)
        ) {
            // Back icon
            Text (
                text = stringResource(
                    if (taskNode.status == "ONGOING") { R.string.done_button_txt }
                    else {R.string.blank_txt}
                ),
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Edit Button
        Button (
            onClick = {
                if (!clickOnce) {return@Button}
                clickOnce = false
                val taskData = Uri.encode(Json.encodeToString(taskNode))
                toEditTaskScreen(taskData) // Function to go to TaskInfoScreen
            },
            colors = buttonColors(
                contentColor = Color(0xFF347AA5),
                containerColor = Color(0xFF347AA5),
                disabledContainerColor = Color.Transparent,
                disabledContentColor = Color.Transparent
            ),
            contentPadding = PaddingValues(10.dp)
        ) {
            Text(
                text = stringResource(R.string.edit_button_txt),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
            )
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    name = "Add task screen"
)
@Composable
fun TaskInfoScreenPreview() {
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