package com.example.todo

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Todo(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var title: String,
    var createdAt: Date, // Ensure this field exists
    var isImportant: Boolean = false
)
