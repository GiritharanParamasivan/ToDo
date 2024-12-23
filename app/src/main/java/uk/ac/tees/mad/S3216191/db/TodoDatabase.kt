package uk.ac.tees.mad.s3216191.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import uk.ac.tees.mad.s3216191.Todo



@Database(entities = [Todo::class], version = 6, exportSchema = false)
@TypeConverters(Converters::class)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao

    companion object {
        val MIGRATION_6_7 = object : Migration(6, 7) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE Todo ADD COLUMN new_column_name TEXT DEFAULT NULL")
            }
        }
    }
}
