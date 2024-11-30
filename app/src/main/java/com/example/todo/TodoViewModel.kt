package com.example.todo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

class TodoViewModel : ViewModel() {

    // Data access object for Todo operations
    private val todoDao = MainApplication.todoDatabase.todoDao()

    // LiveData for observing the todo list
    val todoList: LiveData<List<Todo>> = todoDao.getAllTodo()

    /**
     * Adds a new Todo item to the database.
     *
     * @param title The title of the new Todo item.
     */
    fun addTodo(title: String) {
        if (title.isBlank()) {
            Log.e(TAG, "Todo title cannot be blank.")
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val newTodo = Todo(
                    title = title,
                    createdAt = Date(),
                    isImportant = false
                )
                todoDao.addTodo(newTodo)
                Log.d(TAG, "Todo added successfully: $newTodo")
            } catch (e: Exception) {
                Log.e(TAG, "Error adding Todo: ${e.message}")
            }
        }
    }

    /**
     * Deletes a Todo item from the database by its ID.
     *
     * @param id The ID of the Todo to be deleted.
     */
    fun deleteTodo(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                todoDao.deleteTodo(id)
                Log.d(TAG, "Todo deleted successfully: ID $id")
            } catch (e: Exception) {
                Log.e(TAG, "Error deleting Todo: ${e.message}")
            }
        }
    }

    /**
     * Updates the importance status of a Todo item.
     *
     * @param todo The Todo item to update.
     * @param newLevel The new importance status (0 = not important, 1 = important).
     */
    fun updateImportanceLevel(todo: Todo, newLevel: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                todoDao.updateTodo(todo.copy(isImportant = newLevel == 1))
            } catch (e: Exception) {
                Log.e(TAG, "Error updating importance: ${e.message}")
            }
        }
    }

    /**
     * Updates the title of a Todo item.
     *
     * @param id The ID of the Todo to update.
     * @param newTitle The new title for the Todo.
     */
    fun updateTodoTitle(id: Int, newTitle: String) {
        if (newTitle.isBlank()) {
            Log.e(TAG, "Todo title cannot be blank.")
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val todo = todoDao.getTodoById(id)
                if (todo != null) {
                    todoDao.updateTodo(todo.copy(title = newTitle))
                    Log.d(TAG, "Todo updated successfully: $todo")
                } else {
                    Log.e(TAG, "Todo not found with ID: $id")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error updating Todo title: ${e.message}")
            }
        }
    }


    companion object {
        private const val TAG = "TodoViewModel"
    }
}
