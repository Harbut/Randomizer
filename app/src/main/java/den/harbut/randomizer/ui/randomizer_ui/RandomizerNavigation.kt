package den.harbut.randomizer.ui.randomizer_ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import den.harbut.randomizer.ui.randomizer_ui.coin_flipper_screen.CoinFlipperScreen
import den.harbut.randomizer.ui.randomizer_ui.dice_roller_screen.DiceRollerScreen
import den.harbut.randomizer.ui.randomizer_ui.number_generator_screen.NumberGeneratorScreen

sealed class Screen(val route: String) {
    object NumberGenerator : Screen("number_generator")
    object DiceRoller : Screen("dice_roller")
    object CoinFlipper : Screen("coin_flipper")
}

@Composable
fun Navigation(navController: NavHostController, viewModelFactory: ViewModelFactory) {
    NavHost(navController = navController, startDestination = Screen.NumberGenerator.route) {
        composable(Screen.NumberGenerator.route) { NumberGeneratorScreen(viewModelFactory) }
        composable(Screen.DiceRoller.route) { DiceRollerScreen(viewModelFactory) }
        composable(Screen.CoinFlipper.route) { CoinFlipperScreen(viewModelFactory) }
    }
}