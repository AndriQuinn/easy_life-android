package com.example.easy_life.ui.screen

import android.content.Context
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
import androidx.compose.foundation.layout.width
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
import com.example.easy_life.data.model.TaskNode
import com.example.easy_life.functions.markTaskDone
import com.example.easy_life.functions.toMonthName
import com.example.easy_life.ui.components.StatusIndicator
import com.example.easy_life.ui.model.StatusType
import com.example.easy_life.functions.deleteTask

@Composable
fun TaskInfoScreen(
    taskNode: TaskNode,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Scaffold (
        modifier = Modifier.statusBarsPadding(),
        topBar = {
            TaskInfoNavBar(
                backFunction = { navController.popBackStack() },
            )
        }
    ) { innerPadding ->
        TaskInfoBody(
            modifier = Modifier.padding(innerPadding),
            taskNode = taskNode,
            context = context,
            navController = navController
        )
    }
}

@Composable
fun TaskInfoNavBar(
    backFunction: () -> Unit, // Back callback function
    modifier: Modifier = Modifier
) {
    Row (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = modifier
            .background(Color(0xFF1E1E1E))
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
                painter = painterResource(R.drawable.back_icon),
                contentDescription = stringResource(R.string.back_icon_desc_txt),
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(30.dp)
            )
        }
    }
}

@Composable
fun TaskInfoBody(
    taskNode: TaskNode,
    context: Context,
    navController: NavController,
    modifier: Modifier = Modifier
) {

    // Screen container, vertically placed
    // Container for task info, vertically placed
    Column (
        modifier = modifier
            .background(Color(0xFF1E1E1E))
            .padding(
                vertical = 60.dp,
                horizontal = 15.dp
            )
            .fillMaxSize()
    ) {
        Header(taskNode = taskNode)
        DescriptionBox(taskNode = taskNode)
        BottomButtons(
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

        )
    }
}

// Contains title, deadline, and status
@Composable
fun Header(
    taskNode: TaskNode,
    modifier: Modifier = Modifier
) {
    Row (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Title and deadline
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = taskNode.title,
                color = Color.White,
                fontSize = 30.sp,
                maxLines = 2,
                overflow = TextOverflow.Clip,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(10.dp))
            Text (
                text = "Until ${toMonthName(taskNode.deadline.split("/")[0])} ${
                    taskNode.deadline.split("/")[1]} ${
                    taskNode.deadline.split("/")[2]}",
                fontSize = 12.sp,
                color = Color.White
            )
        }
        // Current status
        Column (
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.weight(1f)

        ) {
            StatusIndicator(
                statusType = StatusType.valueOf(taskNode.status)
            )
        }
    }
}

@Composable
fun DescriptionBox(
    taskNode: TaskNode,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier.padding(15.dp)
    ) {
        Text(
            text = stringResource(R.string.description_header_txt),
            color = Color.White
        )
        Spacer(Modifier.height(20.dp))
        Text(
            text = taskNode.description,
            color = Color.White,
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
        Button (
            onClick = { deleteTask() },
            colors = buttonColors(
                contentColor = Color(0xFFC93334),
                containerColor = Color(0xFFC93334),
                disabledContainerColor = Color.Transparent,
                disabledContentColor = Color.Transparent
            ),
            contentPadding = PaddingValues(5.dp)
        ) {
            Text(
                text = "DELETE",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
            )
        }

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
            contentPadding = PaddingValues(5.dp)
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

        Button (
            onClick = {},
            colors = buttonColors(
                contentColor = Color(0xFF347AA5),
                containerColor = Color(0xFF347AA5),
                disabledContainerColor = Color.Transparent,
                disabledContentColor = Color.Transparent
            ),
            contentPadding = PaddingValues(10.dp)
        ) {
            Text(
                text = "EDIT",
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