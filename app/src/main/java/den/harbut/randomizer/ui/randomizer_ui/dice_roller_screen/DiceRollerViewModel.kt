package den.harbut.randomizer.ui.randomizer_ui.dice_roller_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import den.harbut.randomizer.SettingsManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import den.harbut.randomizer.utils.generateRandomNumbers
import kotlinx.coroutines.delay

class DiceRollerViewModel(private val settingsManager: SettingsManager) : ViewModel() {

    private val _randomDices = MutableStateFlow(listOf(0))
    val randomDices: StateFlow<List<Int>> = _randomDices

    private val _diceCount = MutableStateFlow(settingsManager.getDiceCount())
    val diceCount: StateFlow<String> = _diceCount

    private val _animationDuration = MutableStateFlow(settingsManager.getDiceAnimationDuration())
    val animationDuration: StateFlow<String> = _animationDuration

    private val _showSum = MutableStateFlow(settingsManager.getDiceShowSum())
    val showSum: StateFlow<Boolean> = _showSum

    private val _isGenerating = MutableStateFlow(false)
    val isGenerating: StateFlow<Boolean> = _isGenerating

    fun rollDices() {
        viewModelScope.launch {
            _isGenerating.value = true
            _randomDices.value = generateRandomNumbers(
                "1",
                "6",
                _diceCount.value,
            )
            delay(animationDuration.value.toLongOrNull()?: 10L)
            _isGenerating.value = false
        }
    }

    fun updateDiceCount(diceCount: String) {
        _diceCount.value = diceCount
        viewModelScope.launch {
            settingsManager.setDiceCount(diceCount)
        }
    }

    fun updateAnimationDuration(animationDuration: String) {
        _animationDuration.value = animationDuration
        viewModelScope.launch {
            settingsManager.setDiceAnimationDuration(animationDuration)
        }
    }

    fun updateShowSum(showSum: Boolean) {
        _showSum.value = showSum
        viewModelScope.launch {
            settingsManager.setDiceShowSum(showSum)
        }
    }
}