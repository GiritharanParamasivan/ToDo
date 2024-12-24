package uk.ac.tees.mad.s3216191

import android.app.Application
import androidx.room.Room
import uk.ac.tees.mad.s3216191.db.TodoDatabase
import uk.ac.tees.mad.s3216191.db.TodoDatabase.Companion.MIGRATION_6_7

class MainApplication : Application() {

    companion object {
        lateinit var todoDatabase: TodoDatabase
            private set
        lateinit var gdprPreferenceManager: GDPRPreferenceManager
            private set
    }

    override fun onCreate() {
        super.onCreate()

        // Initialize Room Database
        todoDatabase = Room.databaseBuilder(
            applicationContext,
            TodoDatabase::class.java,
            "todo_database"
        ).addMigrations(MIGRATION_6_7)
            .build()

        // Initialize GDPRPreferenceManager
        gdprPreferenceManager = GDPRPreferenceManager(this)
    }
}

