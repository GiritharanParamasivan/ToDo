package uk.ac.tees.mad.s3216191

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodoViewModel : ViewModel() {

    private val todoDao = MainApplication.todoDatabase.todoDao()

    // Observables
    val todoList: LiveData<List<Todo>> = todoDao.getAllTodo()

    // LiveData for operation results
    private val _operationStatus = MutableLiveData<String>()
    val operationStatus: LiveData<String> get() = _operationStatus


    fun addTodo(title: String, imageBase64: String? = null) {
        if (title.isBlank()) {
            _operationStatus.postValue("Todo title cannot be blank.")
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val newTodo = Todo(
                    title = title,
                    isImportant = false,
                    imageBase64 = imageBase64
                )
                todoDao.insertTodo(newTodo)
                _operationStatus.postValue("Todo added successfully.")
            } catch (e: Exception) {
                Log.e(TAG, "Error adding Todo: ${e.message}")
                _operationStatus.postValue("Failed to add Todo.")
            }
        }
    }


    fun deleteTodo(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                todoDao.deleteTodo(id)
                _operationStatus.postValue("Todo deleted successfully.")
                Log.d(TAG, "Todo deleted successfully: ID $id")
            } catch (e: Exception) {
                Log.e(TAG, "Error deleting Todo: ${e.message}")
                _operationStatus.postValue("Failed to delete Todo.")
            }
        }
    }

    // Update importance level
    fun updateImportanceLevel(todo: Todo, newLevel: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                todoDao.updateTodo(todo.copy(isImportant = newLevel == 1))
                _operationStatus.postValue("Todo importance updated successfully.")
            } catch (e: Exception) {
                Log.e(TAG, "Error updating importance: ${e.message}")
                _operationStatus.postValue("Failed to update importance.")
            }
        }
    }


    fun updateTodoTitle(id: Int, newTitle: String) {
        if (newTitle.isBlank()) {
            _operationStatus.postValue("Todo title cannot be blank.")
            Log.e(TAG, "Todo title cannot be blank.")
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val todo = todoDao.getTodoById(id)
                if (todo != null) {
                    todoDao.updateTodo(todo.copy(title = newTitle))
                    _operationStatus.postValue("Todo updated successfully.")
                    Log.d(TAG, "Todo updated successfully: $todo")
                } else {
                    _operationStatus.postValue("Todo not found.")
                    Log.e(TAG, "Todo not found with ID: $id")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error updating Todo title: ${e.message}")
                _operationStatus.postValue("Failed to update Todo.")
            }
        }
    }

    // Delete all Todos (GDPR Compliance)
    fun deleteAllTodos() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                todoDao.deleteAll()
                _operationStatus.postValue("All Todos deleted successfully.")
            } catch (e: Exception) {
                Log.e(TAG, "Error deleting all Todos: ${e.message}")
                _operationStatus.postValue("Failed to delete all Todos.")
            }
        }
    }

    companion object {
        private const val TAG = "TodoViewModel"
    }
}
