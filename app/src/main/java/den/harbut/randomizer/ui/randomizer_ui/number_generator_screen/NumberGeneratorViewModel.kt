package den.harbut.randomizer.ui.randomizer_ui.number_generator_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import den.harbut.randomizer.SettingsManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import den.harbut.randomizer.utils.generateRandomNumbers // Імпортуємо функцію генерації
import kotlinx.coroutines.delay

class NumberGeneratorViewModel(private val settingsManager: SettingsManager) : ViewModel() {

    private val _randomNumbers = MutableStateFlow<List<Int>>(emptyList())
    val randomNumbers: StateFlow<List<Int>> = _randomNumbers

    private val _minNumber = MutableStateFlow(settingsManager.getMinNumber())
    val minNumber: StateFlow<String> = _minNumber

    private val _maxNumber = MutableStateFlow(settingsManager.getMaxNumber())
    val maxNumber: StateFlow<String> = _maxNumber

    private val _numbersToGenerate = MutableStateFlow(settingsManager.getNumberToGenerate())
    val numbersToGenerate: StateFlow<String> = _numbersToGenerate

    private val _animationDuration = MutableStateFlow(settingsManager.getAnimationDuration())
    val animationDuration: StateFlow<String> = _animationDuration

    private val _avoidDuplicates = MutableStateFlow(settingsManager.getAvoidDuplicates())
    val avoidDuplicates: StateFlow<Boolean> = _avoidDuplicates

    private val _showSum = MutableStateFlow(settingsManager.getShowSum())
    val showSum: StateFlow<Boolean> = _showSum

    private val _isGenerating = MutableStateFlow(false)
    val isGenerating: StateFlow<Boolean> = _isGenerating

    private val _errorText = MutableStateFlow("")
    val errorText: StateFlow<String> = _errorText

    fun generateRandomNumbers() {
        viewModelScope.launch {
            _isGenerating.value = true
            try {
                _randomNumbers.value = generateRandomNumbers(
                    _minNumber.value,
                    _maxNumber.value,
                    _numbersToGenerate.value,
                    _avoidDuplicates.value
                )
                _errorText.value = "" // Очищаємо текст помилки
                delay(animationDuration.value.toLongOrNull()?:10L)
            } catch (e: Exception) {
                _errorText.value = e.message.toString()
            } finally {
                _isGenerating.value = false
            }
        }
    }

    fun updateMinNumber(minNumber: String) {
        _minNumber.value = minNumber
        viewModelScope.launch {
            settingsManager.setMinNumber(minNumber)
        }
    }

    fun updateMaxNumber(maxNumber: String) {
        _maxNumber.value = maxNumber
        viewModelScope.launch {
            settingsManager.setMaxNumber(maxNumber)
        }
    }

    fun updateNumbersToGenerate(numbersToGenerate: String) {
        _numbersToGenerate.value = numbersToGenerate
        viewModelScope.launch {
            settingsManager.setNumberToGenerate(numbersToGenerate)
        }
    }

    fun updateAnimationDuration(animationDuration: String) {
        _animationDuration.value = animationDuration
        viewModelScope.launch {
            settingsManager.setAnimationDuration(animationDuration)
        }
    }

    fun updateAvoidDuplicates(avoidDuplicates: Boolean) {
        _avoidDuplicates.value = avoidDuplicates
        viewModelScope.launch {
            settingsManager.setAvoidDuplicates(avoidDuplicates)
        }
    }

    fun updateShowSum(showSum: Boolean) {
        _showSum.value = showSum
        viewModelScope.launch {
            settingsManager.setShowSum(showSum)
        }
    }
}