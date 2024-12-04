package com.example.todo

import android.app.Application
import androidx.room.Room
import com.example.todo.db.TodoDatabase
import com.example.todo.db.TodoDatabase.Companion.MIGRATION_5_6

class MainApplication : Application() {

    companion object {
        lateinit var todoDatabase: TodoDatabase
            private set
    }

    override fun onCreate() {
        super.onCreate()

        // Initialize the Room database with migrations
        todoDatabase = Room.databaseBuilder(
            applicationContext,
            TodoDatabase::class.java,
            "todo_database"
        )
            .addMigrations(MIGRATION_5_6) // Add the new migration here
            .fallbackToDestructiveMigration() // Optional: Clears the database if migrations fail
            .build()
    }
}
