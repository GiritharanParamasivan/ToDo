package com.example.todo.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.todo.Todo

@TypeConverters(Converters::class)
@Database(entities = [Todo::class], version = 6, exportSchema = false)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao

    companion object {
        val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Adjust the schema changes
                database.execSQL(
                    """
            CREATE TABLE Todo_new (
                id INTEGER PRIMARY KEY NOT NULL,
                title TEXT NOT NULL,
                isImportant INTEGER NOT NULL,
                imageBase64 TEXT
            )
            """.trimIndent()
                )
                database.execSQL(
                    """
            INSERT INTO Todo_new (id, title, isImportant, imageBase64)
            SELECT id, title, isImportant, imageBase64 FROM Todo
            """.trimIndent()
                )
                database.execSQL("DROP TABLE Todo")
                database.execSQL("ALTER TABLE Todo_new RENAME TO Todo")
            }
        }
    }
}
