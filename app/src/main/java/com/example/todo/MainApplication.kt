package com.example.todo

import android.app.Application
import androidx.room.Room
import com.example.todo.db.TodoDatabase

class MainApplication : Application() {

    lateinit var todoDatabase: TodoDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        todoDatabase = Room.databaseBuilder(
            applicationContext,
            TodoDatabase::class.java,
            TodoDatabase.NAME
        ).build()
    }
}
