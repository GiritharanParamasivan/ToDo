package com.example.todo.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.todo.Todo

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
