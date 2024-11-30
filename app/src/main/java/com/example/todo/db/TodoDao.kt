package com.example.todo.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.todo.Todo

@Dao
interface TodoDao {

    /**
     * Retrieves all Todo items, ordered by creation date in descending order.
     *
     * @return LiveData containing a list of Todo items.
     */
    @Query("SELECT * FROM Todo ORDER BY createdAt DESC")
    fun getAllTodo(): LiveData<List<Todo>>

    /**
     * Inserts a new Todo item into the database.
     * If a Todo with the same ID exists, it will be replaced.
     *
     * @param todo The Todo item to be added.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTodo(todo: Todo)

    /**
     * Updates an existing Todo item in the database.
     *
     * @param todo The Todo item with updated values.
     */
    @Update
    suspend fun updateTodo(todo: Todo)

    /**
     * Deletes a Todo item from the database using its ID.
     *
     * @param id The ID of the Todo item to be deleted.
     */
    @Query("DELETE FROM Todo WHERE id = :id")
    suspend fun deleteTodo(id: Int)

    @Query("SELECT * FROM Todo WHERE id = :id LIMIT 1")
    fun getTodoById(id: Int): Todo?

}
