package muley.ayush.walkcount

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import muley.ayush.walkcount.data.UserPreferencesRepository

class MainViewModelFactory(private val userPreferencesRepository: UserPreferencesRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(userPreferencesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}