package den.harbut.randomizer.ui.randomizer_ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import den.harbut.randomizer.SettingsManager
import den.harbut.randomizer.ui.randomizer_ui.coin_flipper_screen.CoinFlipperViewModel
import den.harbut.randomizer.ui.randomizer_ui.dice_roller_screen.DiceRollerViewModel
import den.harbut.randomizer.ui.randomizer_ui.number_generator_screen.NumberGeneratorViewModel

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory{

    private val settingsManager: SettingsManager = SettingsManager(context)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            NumberGeneratorViewModel::class.java -> NumberGeneratorViewModel(settingsManager) as T
            DiceRollerViewModel::class.java -> DiceRollerViewModel(settingsManager) as T
            CoinFlipperViewModel::class.java -> CoinFlipperViewModel(settingsManager) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}