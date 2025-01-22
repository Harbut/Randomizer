package den.harbut.randomizer

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")

class DataStoreManager(private val context: Context) {

    companion object {
        val DICE_COUNT_KEY = stringPreferencesKey("dice_count")
        val ANIMATION_DURATION_KEY = stringPreferencesKey("animation_duration")
        val SHOW_SUM_KEY = booleanPreferencesKey("show_sum")
    }

    // Save preferences
    suspend fun saveDiceCount(diceCount: String) {
        context.dataStore.edit { preferences ->
            preferences[DICE_COUNT_KEY] = diceCount
        }
    }

    suspend fun saveAnimationDuration(animationDuration: String) {
        context.dataStore.edit { preferences ->
            preferences[ANIMATION_DURATION_KEY] = animationDuration
        }
    }

    suspend fun saveShowSum(showSum: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[SHOW_SUM_KEY] = showSum
        }
    }

    // Read preferences
    val diceCount: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[DICE_COUNT_KEY] ?: "1"
    }

    val animationDuration: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[ANIMATION_DURATION_KEY] ?: "1000"
    }

    val showSum: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[SHOW_SUM_KEY] ?: false
    }
}