package com.example.easy_life.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.easy_life.functions.toMonthName
import com.example.easy_life.ui.model.StatusType

@Composable
fun DateBanner(
    modifier: Modifier = Modifier,
    date: String
) {
    // Container banner, horizontally placed
    Row (
        modifier = modifier
            .padding(
                horizontal = 20.dp
            )
            .fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        // Format: day (week) e.g. Tue, month, day (month) e.g. 01, year
        // day/mm/dd/yyyy
        // e.g. "Sat/8/2/2025" -> [Sat,8,2,2025]
        val extractDate = date.split("/")

        // Left Banner, vertically placed
        Column (
            modifier = modifier.weight(1f).fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = extractDate[0], // Day of the week e.g Mon
                color = Color.White,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,

            )
        }

        // Right Banner, vertically placed
        Column(
            modifier = modifier.weight(1f).fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Month, day e.g. Aug 03
            Row {
                Text(
                    text = "${toMonthName(extractDate[1])} ${extractDate[2]}",
                    color = Color.White,
                )
            }
            // Year
            Text(
                text = extractDate[3],
                color = Color.White,
            )
        }
    }
}

@Composable
fun StatusIndicatorBar(
    workDone: Int,
    workNotDone: Int,
    workOngoing: Int,
    modifier: Modifier = Modifier
) {
    // Container for the status indicators, vertically placed
    Column (
        modifier = modifier.padding(horizontal = 20.dp).fillMaxSize(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Tasks Done indicator
        StatusIndicator(
            status = workDone.toString(),
            statusType = StatusType.DONE
        )

        // Ongoing tasks indicator
        StatusIndicator(
            status = workOngoing.toString(),
            statusType = StatusType.ONGOING
        )

        // Missed Tasks indicator
        StatusIndicator(
            status = workNotDone.toString(),
            statusType = StatusType.MISSED
        )
    }
}

@Composable
fun StatusIndicator(
    statusType: StatusType,
    status: String = ""
) {
    // Container, horizontally placed
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Status value, empty by default - ""
        Text(
            text = status,
            color = Color.White
        )
        Spacer(Modifier.width(10.dp))
        // Status icon
        Image(
            painter = painterResource(statusType.icon),
            contentDescription = "status icon",
            modifier = Modifier.size(16.dp)
        )
        Spacer(Modifier.width(5.dp))
        // Status name e.g. Done
        Text(
            text = statusType.statusName,
            color = Color.White
        )
    }
}