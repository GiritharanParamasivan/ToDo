package uk.ac.tees.mad.s3216191.db

import androidx.lifecycle.LiveData
import androidx.room.*
import uk.ac.tees.mad.s3216191.Todo


@Dao
interface TodoDao {
    @Query("SELECT * FROM Todo ORDER BY id DESC")
    fun getAllTodo(): LiveData<List<Todo>>

    @Query("SELECT * FROM Todo WHERE id = :id LIMIT 1")
    suspend fun getTodoById(id: Int): Todo?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodo(todo: Todo)

    @Update
    suspend fun updateTodo(todo: Todo)

    @Query("DELETE FROM Todo WHERE id = :id")
    suspend fun deleteTodo(id: Int)
}