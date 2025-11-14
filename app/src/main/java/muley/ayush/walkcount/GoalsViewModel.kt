package muley.ayush.walkcount

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import muley.ayush.walkcount.data.UserPreferencesRepository

class GoalsViewModel(private val userPreferencesRepository: UserPreferencesRepository) : ViewModel() {

    val stepGoal: StateFlow<Int> = userPreferencesRepository.stepGoal
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 10000
        )

    fun saveStepGoal(stepGoal: Int) {
        viewModelScope.launch {
            userPreferencesRepository.saveStepGoal(stepGoal)
        }
    }
}