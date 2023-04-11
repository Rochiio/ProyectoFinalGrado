package com.example.mappers

import com.example.dto.TaskCreateDto
import com.example.models.calendar.Task
import java.time.LocalDate

fun List<TaskCreateDto>.toListTasks(): List<Task>{
    val list: MutableList<Task> = mutableListOf()
    this.forEach {
        val task = Task(date = LocalDate.parse(it.date), task = it.task)
        list.add(task)
    }
    return list
}