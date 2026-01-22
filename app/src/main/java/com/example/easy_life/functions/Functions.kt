package com.example.easy_life.functions

import android.content.Context
import com.example.easy_life.data.model.TaskNode
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

fun toMonthName(month: String): String  = when (month.toInt()) {
    // Convert month integers to month name
    1 ->  "Jan"
    2 ->  "Feb"
    3 ->  "Mar"
    4 ->  "Apr"
    5 ->  "May"
    6 ->  "Jun"
    7 ->  "Jul"
    8 ->  "Aug"
    9 ->  "Sep"
    10 ->  "Oct"
    11 ->  "Nov"
    12 ->  "Dec"
    else ->  ""
}

fun addTaskFile(
    context: Context,
    taskTitle: String,
    taskDeadline: String,
    taskDescription: String
) {
    val filePath = File(context.filesDir,"task-list.json") // Get the file path

    // Checks if the file exist in the path, if not create one
    if (!filePath.exists()) {
        val file = JSONArray()
        val lengthObj = JSONObject().put("length","0") // Create a json object length for id's
        file.put(lengthObj)
        filePath.writeText(file.toString())
    }

    val fileContent = JSONArray(filePath.readText()) // place the file content in the variable
    var length = fileContent.getJSONObject(0).getInt("length") // Get the value of length
    length++ // Iterate length
    val taskObject = JSONObject() // Create json object to hold the task data e.g. title

    taskObject.put("title", taskTitle)
    taskObject.put("deadline", taskDeadline)
    taskObject.put("description", taskDescription)
    taskObject.put("status", "ONGOING")
    taskObject.put("id", "$length") // Assign the id as the current length

    fileContent.put(taskObject) // put the task object to the json array
    fileContent.getJSONObject(0).put("length","$length") // Update the length on the file
    filePath.writeText(fileContent.toString()) // rewrite the file with the updated one
}

fun getTotal(list: List<TaskNode>, type: String): Int {
    var total = 0
    for (tasks in list) {
        if (tasks.status == type) {
            total += 1
        }
    }
    return total
}

fun markTaskDone(id: Int,context: Context) {
    val filePath = File(context.filesDir, "task-list.json") // Declare file path
    // Checks if file exists
    val file = if (filePath.exists()) {
        JSONArray(filePath.readText())
    } else { JSONArray() }

    if (file.length() > 1) {
        for (task in 1 until file.length()) {
            if (file.getJSONObject(task).getInt("id") == id) {
                file.getJSONObject(task).put("status","DONE")
                filePath.writeText(file.toString())
                break
            }
        }
    }
}

fun deleteTask(
    context: Context,
    id: String
) {
    val filePath = File(context.filesDir,"task-list.json")
    val fileContent = JSONArray(filePath.readText())

    for (i in fileContent.length() - 1 downTo 1) {
        val task = fileContent.getJSONObject(i)
        if (task.getInt("id") == id.toInt()) {
            fileContent.remove(i)
            break
        }
    }
    filePath.writeText(fileContent.toString())
}

fun saveEditedTask(
    context: Context,
    taskTitle: String,
    taskDeadline: String,
    taskDescription: String,
    taskNode: TaskNode
) {
    val filePath = File(context.filesDir,"task-list.json")
    val fileContent = JSONArray(filePath.readText()) // place the file content in the variable
    val taskObject = JSONObject() // Create json object to hold the task data e.g. title

    taskObject.put("title", taskTitle)
    taskObject.put("deadline", taskDeadline)
    taskObject.put("description", taskDescription)
    taskObject.put("status", taskNode.status)
    taskObject.put("id", taskNode.id)

    for (i in fileContent.length() - 1 downTo 1) {
        val task = fileContent.getJSONObject(i)
        if (task.getInt("id") == taskNode.id.toInt()) {
            fileContent.put(i,taskObject)
            break
        }
    }
    filePath.writeText(fileContent.toString())
}