package com.thk.layoutsample

data class TodoItem(
    val content: String,
    var isCompleted: Boolean = false
)

val testItems = mutableListOf<TodoItem>(
    TodoItem("숨쉬기", true),
    TodoItem("산책가기"),
    TodoItem("얘들아 3년동안 수고했고")
)