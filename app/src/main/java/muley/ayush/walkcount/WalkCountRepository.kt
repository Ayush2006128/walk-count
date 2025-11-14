package muley.ayush.walkcount

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * A simple singleton object to hold the step count state.
 * This allows the Service and the ViewModel to access the same data.
 */
object WalkCountRepository {

    // Holds the current step count. Starts at 0.
    private val _steps = MutableStateFlow(0)
    val steps = _steps.asStateFlow()

    /**
     * Called by the StepCounterService to update the step count.
     */
    fun updateSteps(count: Int) {
        _steps.value = count
    }
}