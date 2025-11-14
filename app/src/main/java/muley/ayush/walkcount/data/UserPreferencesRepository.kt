package muley.ayush.walkcount.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPreferencesRepository(context: Context) {

    private val dataStore = context.dataStore

    private object PreferencesKeys {
        val STEP_GOAL = intPreferencesKey("step_goal")
    }

    val stepGoal: Flow<Int> = dataStore.data
        .map {
            it[PreferencesKeys.STEP_GOAL] ?: 10000 // Default to 10,000 steps
        }

    suspend fun saveStepGoal(stepGoal: Int) {
        dataStore.edit {
            it[PreferencesKeys.STEP_GOAL] = stepGoal
        }
    }
}