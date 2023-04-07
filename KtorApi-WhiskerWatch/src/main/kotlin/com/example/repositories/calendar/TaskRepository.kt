package com.example.repositories.calendar

import com.example.models.calendar.Task
import com.example.repositories.ICrud
import java.util.*

interface TaskRepository: ICrud<Task, UUID> {
}