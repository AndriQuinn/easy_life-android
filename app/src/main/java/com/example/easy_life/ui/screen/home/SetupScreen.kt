package com.example.easy_life.ui.screen.home


import android.R.attr.value
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
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.easy_life.data.local.setFontSize
import com.example.easy_life.data.local.setTheme
import com.example.easy_life.functions.addTaskFile
import com.example.easy_life.functions.toMonthName
import com.example.easy_life.ui.screen.AddTaskScreen
import kotlinx.coroutines.launch
import java.util.Calendar
import kotlin.math.roundToInt

@Composable
fun SetupScreen(
    theme: Theme = Theme.LIGHTTHEME,
    onFinish: () -> Unit,
    modifier: Modifier = Modifier) {
    Scaffold(
        modifier = Modifier.statusBarsPadding()
    ) { innerPadding  ->
        val scope = rememberCoroutineScope()
        val context = LocalContext.current
        SetupScreenBody(
            theme = theme,
            setFontSize = { size ->
                scope.launch {
                    setFontSize(context, size )
                }
            },
            onFinish = { onFinish() },
            modifier = Modifier.padding(innerPadding))

    }
}

@Composable
fun SetupScreenBody(
    theme: Theme,
    onFinish: () -> Unit,
    setFontSize: (Int) -> Unit,
    modifier: Modifier = Modifier) {
    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(theme.backgroundColor)
            .fillMaxSize()
    ) {


        AppName(theme = theme)
        Spacer(Modifier.height(50.dp))
        Text(
            text = "Set Up Preference",
            color = theme.fontColor,
            fontSize = 35.sp,
            fontWeight = FontWeight.Bold
        )
        FontSizeOption(
            setFontSize = setFontSize,
            theme = theme)
        ThemeOption(theme = theme)
        Button(
            onClick = { onFinish() }
        ) {
            Text(
                text = "Save"
            )
        }
    }
}

@Composable
fun AppName(
    theme: Theme,
    modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = stringResource(R.string.logo_image_desc_txt),
            modifier = Modifier.size(80.dp)
        )
        Text (
            text = stringResource(R.string.tasklist_header_txt),
            color = theme.fontColor,
            fontSize = 35.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun FontSizeOption(
    setFontSize: (Int) -> Unit,
    theme: Theme,
    modifier: Modifier = Modifier) {
    Column (
        modifier = Modifier.padding(horizontal = 40.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val labels = listOf("Small", "Medium", "Large")
        var index by remember { mutableStateOf(0) }
        var value by remember { mutableIntStateOf(14) }

        Text(
            text = "Font Size",
            color = theme.fontColor,
            fontSize = 22.sp,
        )

        Text(
            text = "Sample Text",
            color = theme.fontColor,
            fontSize = (value).sp,
        )


        Slider(
            value = index.toFloat(),
            onValueChange = {
                index = it.roundToInt()
                setFontSize(it.roundToInt())
                value = it.roundToInt()
            },
            valueRange = 14f..22f,
            steps = 1
        )
        Text(
            text = labels[index],
            fontSize = 14.sp
        )
    }
}

@Composable
fun ThemeOption(
    theme: Theme,
    modifier: Modifier = Modifier) {
    Column (
        modifier = Modifier.padding(horizontal = 40.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val scope = rememberCoroutineScope()
        val context = LocalContext.current


        Text(
            text = "Theme",
            color = theme.fontColor,
            fontSize = 22.sp,
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button (
                contentPadding = PaddingValues(horizontal = 50.dp),
                shape = RoundedCornerShape(0.dp),
                onClick = {
                    scope.launch {
                        setTheme(context,  "dark")
                    }
                }
            ) {
                Text(
                    text = "Dark"
                )
            }
            Button (
                contentPadding = PaddingValues(horizontal = 50.dp),
                shape = RoundedCornerShape(0.dp),
                onClick = { scope.launch {
                    setTheme(context,  "light")
                }}
            ) {
                Text(
                    text = "Light"
                )
            }
        }
    }
}



@Preview
@Composable
fun SetupScreenPreview() {
    SetupScreen(
        onFinish = {}
    )
}