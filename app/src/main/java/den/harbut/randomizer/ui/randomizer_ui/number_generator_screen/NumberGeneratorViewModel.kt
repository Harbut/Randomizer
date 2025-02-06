package den.harbut.randomizer.ui.randomizer_ui.number_generator_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import den.harbut.randomizer.utils.generateRandomNumbers // Імпортуємо функцію генерації
import kotlinx.coroutines.delay

class NumberGeneratorViewModel : ViewModel() {

    private val _randomNumbers = MutableStateFlow<List<Int>>(emptyList())
    val randomNumbers: StateFlow<List<Int>> = _randomNumbers

    private val _minNumber = MutableStateFlow("0")
    val minNumber: StateFlow<String> = _minNumber

    private val _maxNumber = MutableStateFlow("10")
    val maxNumber: StateFlow<String> = _maxNumber

    private val _numbersToGenerate = MutableStateFlow("1")
    val numbersToGenerate: StateFlow<String> = _numbersToGenerate

    private val _animationDuration = MutableStateFlow("1000")
    val animationDuration: StateFlow<String> = _animationDuration

    private val _avoidDuplicates = MutableStateFlow(false)
    val avoidDuplicates: StateFlow<Boolean> = _avoidDuplicates

    private val _showSum = MutableStateFlow(false)
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
    }

    fun updateMaxNumber(maxNumber: String) {
        _maxNumber.value = maxNumber
    }

    fun updateNumbersToGenerate(numbersToGenerate: String) {
        _numbersToGenerate.value = numbersToGenerate
    }

    fun updateAnimationDuration(animationDuration: String) {
        _animationDuration.value = animationDuration
    }

    fun updateAvoidDuplicates(avoidDuplicates: Boolean) {
        _avoidDuplicates.value = avoidDuplicates
    }

    fun updateShowSum(showSum: Boolean) {
        _showSum.value = showSum
    }
}