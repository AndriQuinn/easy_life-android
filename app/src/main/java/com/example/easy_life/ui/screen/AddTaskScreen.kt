package com.example.easy_life.ui.screen

import android.app.DatePickerDialog
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.easy_life.R
import com.example.easy_life.data.local.Theme
import com.example.easy_life.functions.addTaskFile
import com.example.easy_life.functions.toMonthName
import java.util.Calendar

@Composable
fun AddTaskScreen(
    preferFontSize: Int = 14,
    theme: Theme = Theme.LIGHTTHEME,
    navController: NavController
) {
    var checkFields by remember {mutableStateOf(false)}
    val context = LocalContext.current
    var taskTitle by remember { mutableStateOf("") } // Task title state holder
    var taskDescription by remember { mutableStateOf("") } // Task description state holder
    var taskDeadline by remember { mutableStateOf("") } // Task deadline state holder
    var isFieldCompleted by remember {mutableStateOf(false)}

    // Screen container, vertically placed
    Scaffold (
        topBar = {
            AddTaskNavBar(
                theme = theme,
                backFunction = { navController.popBackStack() },// Back button
            )
        },
        modifier = Modifier.statusBarsPadding()
    ) { innerPadding ->
        AddTaskBody(
            preferFontSize = preferFontSize,
            theme = theme,
            modifier = Modifier.padding(innerPadding),
            title = taskTitle, // Pass the state
            description = taskDescription, // Pass the state
            setTitleFunction = { title -> taskTitle = title }, // taskTitle setter
            setDeadlineFunction = { deadline -> taskDeadline = deadline }, // taskDeadline setter
            setDescriptionFunction = { description -> taskDescription = description }, // taskDescription setter
            checkField = checkFields,
            addFunction = {
                checkFields = true
                if (taskTitle.isNotBlank() && taskDeadline.isNotBlank() && taskDescription.isNotBlank()) {

                    isFieldCompleted = true
                    addTaskFile( // Function to add the task to json file
                        context = context,
                        taskTitle = taskTitle,
                        taskDescription = taskDescription,
                        taskDeadline = taskDeadline
                    )
                    navController.popBackStack()
                }
            },
            backFunction = { navController.popBackStack() },
            isFieldCompleted = isFieldCompleted
        )
    }
}

@Composable
fun AddTaskNavBar(
    theme: Theme,
    backFunction: () -> Unit, // Back function callback
    modifier: Modifier = Modifier
) {
    // Container for navigation, horizontally placed
    Row (
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(theme.backgroundColor)
            .padding(horizontal = 15.dp)
            .fillMaxWidth()
    ) {
        // Back button
        var clickOnce by remember {mutableStateOf(true)}

        Button (
            onClick = {
                if (!clickOnce) {return@Button}
                clickOnce = false
                backFunction() }
            , // Use back function
            colors = buttonColors(
                contentColor = Color.Transparent,
                containerColor = Color.Transparent
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
fun AddTaskBody(
    preferFontSize: Int = 14,
    theme: Theme,
    title: String,
    description: String,
    checkField: Boolean,
    setTitleFunction: (String) -> Unit,
    setDeadlineFunction: (String) -> Unit,
    setDescriptionFunction: (String) -> Unit,
    modifier: Modifier = Modifier,
    backFunction: () -> Unit,
    addFunction: () -> Unit,
    isFieldCompleted: Boolean,

) {
    val context = LocalContext.current // Get app context
    val calendar = Calendar.getInstance() // Get calendar
    val year = calendar.get(Calendar.YEAR) // Set default year
    val month = calendar.get(Calendar.MONTH) // Set default month
    val day = calendar.get(Calendar.DAY_OF_MONTH) // Set default day

    // Holds the selected date as state
    var selectedDate by remember { mutableStateOf("") }

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

    // Container for task info field e.g. title, vertically placed
    Column (
        modifier = modifier
            .background(theme.backgroundColor)
            .padding(30.dp)
            .fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.new_task_header_txt),
            color = theme.fontColor,
            fontSize = if (preferFontSize <= 22) {
                (preferFontSize * 1.6).sp
            } else { (preferFontSize * 1.6 * 1.6).sp },
            modifier = Modifier
                .padding(vertical = 30.dp)
                .fillMaxWidth()
        )
        // Header
        // set title field
        TextField(
            value = title,
            onValueChange = {setTitleFunction(it)}, // Return the value to parent
            label = {Text(stringResource(R.string.title_txt))},
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFD3D3D3),
                unfocusedContainerColor = Color(0xFFD3D3D3),
                unfocusedTextColor = theme.fontColor,
                focusedTextColor = theme.fontColor
            ),
            textStyle = TextStyle(
                fontSize = 22.sp
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
                    } else {theme.fontColor},
                    modifier = modifier
                        .padding(start = 10.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Start,
                    fontSize = 14.sp
                )
            } else {
                val deadlineDate = selectedDate.split("/")
                Text(
                    text = "Deadline: ${toMonthName(deadlineDate[0])} ${deadlineDate[1]} ${deadlineDate[2]}",
                    color = theme.fontColor,
                    modifier = modifier
                        .padding(start = 10.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Start,
                    fontSize = 22.sp
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
                focusedContainerColor = Color(0xFFD3D3D3),
                unfocusedContainerColor = Color(0xFFD3D3D3),
                unfocusedTextColor = theme.fontColor,
                focusedTextColor = theme.fontColor
            ),
            textStyle = TextStyle(
                fontSize = 22.sp
            ),
            isError = if (checkField) {
                description.isBlank()
            } else {false},
            singleLine = false,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(50.dp))
        // Buttons - ADD and CANCEL
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            var lockButton by remember { mutableStateOf(false) }
            Button(
                onClick = {
                    if (lockButton) {return@Button}
                    lockButton = isFieldCompleted == true
                    backFunction()
                },
                colors = buttonColors(
                    contentColor = Color.Transparent,
                    containerColor = Color.Transparent,
                    disabledContentColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent
                )
            ) {
                Text (
                    text = stringResource(R.string.cancel_txt),
                    color = Color(0xFFED4845),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                )
            }

            Button(
                onClick = {
                    if (lockButton) {return@Button}
                    lockButton = isFieldCompleted == true
                    addFunction() // Use add function
                },
                colors = buttonColors(
                    contentColor = Color(0xFF547A3d),
                    containerColor = Color(0xFF547A3d),
                    disabledContentColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent
                )
            ) {
                Text (
                    text = stringResource(R.string.add_txt_button),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    name = "Add task screen"
)
@Composable
fun AddTaskScreenPreview() {
    AddTaskScreen(

        navController = rememberNavController())
}