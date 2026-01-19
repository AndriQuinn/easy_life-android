package com.example.easy_life.ui.screen.home

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.easy_life.data.model.TaskNode
import org.json.JSONArray
import java.io.File
import kotlinx.serialization.json.Json

class HomeViewModel : ViewModel() {
    private val _taskList = mutableStateListOf<TaskNode>()

    val taskList: List<TaskNode> = _taskList

    fun refresh(currentDate: String,context: Context) {
        _taskList.clear()
        updatePastDeadlines(currentDate,context)
        removeFileTask(currentDate,context)
        readFile(context)
    }

    private fun updatePastDeadlines(currentDate: String, context: Context) {
        val filePath = File(context.filesDir, "task-list.json")

        // Store the content
        val updatedList = if (filePath.exists()) {
            JSONArray(filePath.readText())
        } else {
            JSONArray()
        }

        // Check if empty, if empty skip the function
        if (updatedList.length() > 1) {
            // Iterate through the task
            for (task in 1 until updatedList.length()) {
                // Check all tasks that are not missed yet
                if (updatedList.getJSONObject(task).getString("status") != "MISSED") {
                    val deadline = updatedList.getJSONObject(task).getString("deadline")
                        .split("/") // Get the deadline
                    val date = currentDate.split("/") // Get the current date

                    if (date[1].toInt() > deadline[0].toInt()) { // Check if the current date is 1 month late than the deadline
                        updatedList.getJSONObject(task).put("status", "MISSED") // Update the status
                    } else if (date[1].toInt() == deadline[0].toInt() && date[2].toInt() > deadline[1].toInt()) { // Check if the current date if the day is past deadline
                        updatedList.getJSONObject(task).put("status", "MISSED") // Update status
                    }
                }
            }

            // Overwrites the file with the updated one
            filePath.writeText(updatedList.toString())
        }
    }

    // Automatically removes file task after a month over the deadline
    private fun removeFileTask(currentDate: String,context: Context) {

        val date = currentDate.split("/") // date array, e.g. E/MM/dd/yyyy
        val filePath = File(context.filesDir, "task-list.json") // Declare file path
        // Checks if file exists
        val file = if (filePath.exists()) {
            JSONArray(filePath.readText())
        } else { JSONArray() }

        // If file holds a task
        if (file.length() > 1) {
            for (task in 1 until file.length()) {
                // Get task deadline, e.g. mm/dd/yyyy
                val deadline = file.getJSONObject(task).get("deadline").toString().split("/")

                // Checks if the deadline is 1 month over the deadline
                if (
                    (date[1].toInt() - deadline[0].toInt() > 1) ||
                    (date[1].toInt() > deadline[0].toInt() && date[2].toInt() > deadline[1].toInt())
                ) {
                    file.remove(task)
                }
            }

            // Update the file
            filePath.writeText(file.toString())
        }
    }

    private fun readFile(context: Context) {
        val filePath = File(context.filesDir,"task-list.json") // File path

        // Check if the file exist
        val fileJson = if (filePath.exists()) {
            JSONArray(filePath.readText()) // Read the file as JSON array
        } else {JSONArray()} // Return a empty JSON array

        if (fileJson.length() > 1) {
            val fileList = mutableListOf<TaskNode>() // holds the list of tasks found in file
            for (task in (fileJson.length()-1) downTo 1) {
                fileList.add(
                    Json.decodeFromString<TaskNode>(fileJson.getString(task)) // Load the content
                )
            }

            _taskList.addAll(fileList) // Load all the contents
        }
    }
}