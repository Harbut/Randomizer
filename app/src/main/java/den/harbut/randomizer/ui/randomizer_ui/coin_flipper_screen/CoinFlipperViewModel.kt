package den.harbut.randomizer.ui.randomizer_ui.coin_flipper_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CoinFlipperViewModel : ViewModel() {

    private val _coinSide = MutableStateFlow(0) // 0 - орел, 1 - решка
    val coinSide: StateFlow<Int> = _coinSide

    private val _animationDuration = MutableStateFlow("1000")
    val animationDuration: StateFlow<String> = _animationDuration

    private val _showDescriptor = MutableStateFlow(false)
    val showDescriptor: StateFlow<Boolean> = _showDescriptor

    private val _isGenerating = MutableStateFlow(false)
    val isGenerating: StateFlow<Boolean> = _isGenerating

    fun flipCoin() {
        viewModelScope.launch {
            _isGenerating.value = true
            _coinSide.value = (0..1).random()
            delay(animationDuration.value.toLongOrNull() ?: 10L)
            _isGenerating.value = false
        }
    }

    fun updateAnimationDuration(animationDuration: String) {
        _animationDuration.value = animationDuration
    }

    fun updateShowDescriptor(showDescriptor: Boolean) {
        _showDescriptor.value = showDescriptor
    }
}