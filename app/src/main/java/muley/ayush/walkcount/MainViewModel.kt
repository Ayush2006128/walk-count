package muley.ayush.walkcount

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow

/**
 * The ViewModel for the main screen.
 * It simply exposes the step count from the repository as a StateFlow
 * so the Compose UI can observe it.
 */

class MainViewModel : ViewModel() {
    val steps : StateFlow<Int> = WalkCountRepository.steps;
}