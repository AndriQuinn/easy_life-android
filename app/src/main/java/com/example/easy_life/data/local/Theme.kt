package com.example.easy_life.data.local

import android.content.Context
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.easy_life.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

enum class Theme(
    val addIcon: Int,
    val backIcon: Int,
    val ongoingIcon: Int,
    val speakerIcon: Int,
    val fontColor: Color,
    val backgroundColor: Color,
) {


    DARKTHEME(
        addIcon = R.drawable.w_add_icon,
        backIcon = R.drawable.w_back_icon,
        ongoingIcon = R.drawable.w_ongoing_icon,
        speakerIcon = R.drawable.w_speaker_logo,
        fontColor = Color.White,
        backgroundColor = Color.Black,

    ),

    LIGHTTHEME(
        addIcon = R.drawable.b_add_icon,
        backIcon = R.drawable.b_back_icon,
        ongoingIcon = R.drawable.b_ongoing_icon,
        speakerIcon = R.drawable.b_speaker_logo,
        fontColor = Color.Black,
        backgroundColor = Color.White
    )
}