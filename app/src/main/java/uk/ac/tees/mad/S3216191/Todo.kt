package uk.ac.tees.mad.S3216191

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Todo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val isImportant: Boolean,
    val imageBase64: String? = null // Ensure this matches
)

