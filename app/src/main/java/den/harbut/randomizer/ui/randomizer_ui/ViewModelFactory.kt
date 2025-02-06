package den.harbut.randomizer.ui.randomizer_ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import den.harbut.randomizer.ui.randomizer_ui.coin_flipper_screen.CoinFlipperViewModel
import den.harbut.randomizer.ui.randomizer_ui.dice_roller_screen.DiceRollerViewModel
import den.harbut.randomizer.ui.randomizer_ui.number_generator_screen.NumberGeneratorViewModel

class ViewModelFactory : ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            NumberGeneratorViewModel::class.java -> NumberGeneratorViewModel() as T
            DiceRollerViewModel::class.java -> DiceRollerViewModel() as T
            CoinFlipperViewModel::class.java -> CoinFlipperViewModel() as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}