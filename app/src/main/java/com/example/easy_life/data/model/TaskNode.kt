package com.example.easy_life.data.model

import kotlinx.serialization.Serializable

@Serializable
data class TaskNode (
    val title: String,
    val description: String,
    val deadline: String,
    val status: String,
    val id: String
)