package den.harbut.randomizer.ui.randomizer_ui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import den.harbut.randomizer.ui.randomizer_ui.NumberGeneratorScreen

sealed class Screen(val route: String) {
    object NumberGenerator : Screen("number_generator")
    object DiceRoller : Screen("dice_roller")
    object CoinFlipper : Screen("coin_flipper")
}

@Composable
fun Navigation(navController: NavHostController, context: Context) {
    NavHost(navController = navController, startDestination = Screen.NumberGenerator.route) {
        composable(Screen.NumberGenerator.route) { NumberGeneratorScreen() }
        composable(Screen.DiceRoller.route) { DiceRollerScreen(context = context) }
        composable(Screen.CoinFlipper.route) { CoinFlipperScreen() }
    }
}