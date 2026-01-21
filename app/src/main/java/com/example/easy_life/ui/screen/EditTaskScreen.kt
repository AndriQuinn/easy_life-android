package com.example.easy_life.ui.screen

import android.app.DatePickerDialog
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
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.style.TextAlign
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
import java.util.Calendar

@Composable
fun EditTaskScreen(
    taskNode: TaskNode,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var checkFields by remember {mutableStateOf(false)}
    var isFieldCompleted by remember {mutableStateOf(false)}
    var taskTitle by remember { mutableStateOf(taskNode.title) } // Task title state holder
    var taskDescription by remember { mutableStateOf(taskNode.description) } // Task description state holder
    var taskDeadline by remember { mutableStateOf(taskNode.deadline) } // Task deadline state holder

    Scaffold (
        modifier = Modifier.statusBarsPadding(),
        topBar = {
            EditTaskNavBar(
                navController = navController
            )
        }
    ) { innerPadding ->
        EditTaskInfoBody(
            modifier = Modifier.padding(innerPadding),
            taskNode = taskNode,
            title = taskTitle, // Pass the state
            description = taskDescription, // Pass the state
            setTitleFunction = { title -> taskTitle = title }, // taskTitle setter
            setDeadlineFunction = { deadline -> taskDeadline = deadline }, // taskDeadline setter
            setDescriptionFunction = { description -> taskDescription = description }, // taskDescription setter
            checkField = checkFields,
        )
    }
}

@Composable
fun EditTaskNavBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Row (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .background(Color(0xFF1E1E1E))
            .padding(horizontal = 15.dp)
            .fillMaxWidth()
    ) {
        Button(
            onClick = {navController.popBackStack()},
            colors = buttonColors(
                contentColor = Color.Transparent,
                containerColor = Color.Transparent,
                disabledContentColor = Color.Transparent,
                disabledContainerColor = Color.Transparent
            )
        ) {
            Text (
                text = "CANCEL",
                color = Color(0xFFED4845),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
            )
        }
        Button(
            onClick = {navController.popBackStack()},
            colors = buttonColors(
                contentColor = Color(0xFF547A3d),
                containerColor = Color(0xFF547A3d),
                disabledContentColor = Color.Transparent,
                disabledContainerColor = Color.Transparent
            )
        ) {
            Text (
                text = "SAVE",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.White
            )
        }
    }
}


@Composable
fun EditTaskInfoBody(
    taskNode: TaskNode,
    title: String,
    description: String,
    checkField: Boolean,
    setTitleFunction: (String) -> Unit,
    setDeadlineFunction: (String) -> Unit,
    setDescriptionFunction: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val extractDateDeadline = taskNode.deadline.split("/")
    val context = LocalContext.current // Get app context
    val calendar = Calendar.getInstance() // Get calendar
    val year = extractDateDeadline[2].toInt() // Set default year
    val month = extractDateDeadline[1].toInt() // Set default month
    val day = extractDateDeadline[0].toInt() // Set default day


    // Holds the selected date as state
    var selectedDate by remember { mutableStateOf(taskNode.deadline) }


    // Date picker
    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _, y, m, d -> // year, month, day parameters
                selectedDate = "${m + 1}/$d/$y" // Set the selected date
                setDeadlineFunction(selectedDate) // Return the selected date tot the parent
            },
            // Default values if user didn't pick any dates:
            year,
            month,
            day
        )
    }

    // Header


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

        Text(
            text = "Edit Task",
            color = Color.White,
            fontSize = 25.sp,
            modifier = Modifier
                .padding(vertical = 30.dp)
                .fillMaxWidth()
        )
        Spacer(Modifier.height(10.dp))
        TextField(
            value = title,
            onValueChange = {setTitleFunction(it)}, // Return the value to parent
            label = {Text(stringResource(R.string.title_txt))},
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                unfocusedTextColor = Color.White,
                focusedTextColor = Color.White
            ),
            isError = if (checkField) {
                title.isBlank()
            } else {false},
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(10.dp))
        // set deadline field
        Button(
            onClick = {datePickerDialog.show()}, // Open calendar
            colors = buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Transparent,
            ),
            shape = RoundedCornerShape(0.dp),
            contentPadding = PaddingValues(10.dp)
        ) {
            if (selectedDate.isBlank()) {
                Text(
                    text = stringResource(R.string.pick_a_deadline_txt),
                    color = if (checkField) {
                        Color(0xFFFFAEB7)
                    } else {Color.White},
                    modifier = modifier
                        .padding(start = 10.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Start
                )
            } else {
                val deadlineDate = selectedDate.split("/")
                Text(
                    text = "Deadline: ${toMonthName(deadlineDate[0])} ${deadlineDate[1]} ${deadlineDate[2]}",
                    color = Color.White,
                    modifier = modifier
                        .padding(start = 10.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Start
                )
            }
        }
        Spacer(Modifier.height(10.dp))
        // set description field
        TextField(
            value = description,
            onValueChange = {setDescriptionFunction(it)}, // Return the value to parent
            label = {Text(stringResource(R.string.description_txt))},
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                unfocusedTextColor = Color.White,
                focusedTextColor = Color.White,
            ),
            isError = if (checkField) {
                description.isBlank()
            } else {false},
            singleLine = false,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    name = "Edit task screen"
)
@Composable
fun EditTaskScreenPreview() {
    EditTaskScreen(
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