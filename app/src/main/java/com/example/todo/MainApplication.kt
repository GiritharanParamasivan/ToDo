package com.example.todo

import android.app.Application
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.todo.db.TodoDatabase

class MainApplication : Application() {

    companion object {
        lateinit var todoDatabase: TodoDatabase
            private set
    }

    override fun onCreate() {
        super.onCreate()

        // Initialize the Room database
        todoDatabase = Room.databaseBuilder(
            applicationContext,
            TodoDatabase::class.java,
            TodoDatabase.NAME
        )
            .addMigrations(MIGRATION_1_2) // Add migrations to handle schema changes
            .fallbackToDestructiveMigration() // Optional: Clear database if migration fails
            .build()
    }

    /**
     * Migration from version 1 to version 2.
     * Adds a new column `isImportant` to the `Todo` table.
     */
    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                "ALTER TABLE Todo ADD COLUMN isImportant INTEGER NOT NULL DEFAULT 0"
            )
        }
    }
}
