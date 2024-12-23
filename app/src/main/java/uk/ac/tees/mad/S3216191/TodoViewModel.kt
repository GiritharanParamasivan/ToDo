package uk.ac.tees.mad.s3216191

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
class TodoViewModel : ViewModel() {

    private val todoDao = MainApplication.todoDatabase.todoDao()
    val todoList: LiveData<List<Todo>> = todoDao.getAllTodo()

    fun addTodo(title: String, imageBase64: String? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            val newTodo = Todo(
                title = title,
                isImportant = false,
                imageBase64 = imageBase64
            )
            todoDao.insertTodo(newTodo)
        }
    }

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

    fun updateImportanceLevel(todo: Todo, newLevel: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                todoDao.updateTodo(todo.copy(isImportant = newLevel == 1))
            } catch (e: Exception) {
                Log.e(TAG, "Error updating importance: ${e.message}")
            }
        }
    }

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