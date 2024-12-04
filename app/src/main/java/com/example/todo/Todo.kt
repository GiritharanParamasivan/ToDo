package com.example.todo

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Todo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val isImportant: Boolean = false,
    val createdAt: Date = Date(), // Ensure Date fields are correctly handled
    val imageBase64: String? = null
)
