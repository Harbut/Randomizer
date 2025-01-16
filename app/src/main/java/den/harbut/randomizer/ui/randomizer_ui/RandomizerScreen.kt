package den.harbut.randomizer.ui.randomizer_ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import den.harbut.randomizer.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()
    var selectedItemRoute by rememberSaveable { mutableStateOf(Screen.NumberGenerator.route) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                DrawerHeader()
                Spacer(Modifier.height(12.dp))
                HorizontalDivider()
                Spacer(Modifier.height(12.dp))
                NavigationDrawerItem(
                    icon = { Icon(painterResource(R.drawable.ic_number), contentDescription = stringResource(R.string.number_generator)) },
                    label = { Text(stringResource(R.string.number_generator)) },
                    selected = selectedItemRoute == Screen.NumberGenerator.route,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Screen.NumberGenerator.route)
                        selectedItemRoute = Screen.NumberGenerator.route
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
                NavigationDrawerItem(
                    icon = { Icon(painterResource(R.drawable.ic_dice), contentDescription = stringResource(R.string.dice_roller)) },
                    label = { Text(stringResource(R.string.dice_roller)) },
                    selected = selectedItemRoute == Screen.DiceRoller.route,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Screen.DiceRoller.route)
                        selectedItemRoute = Screen.DiceRoller.route
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
                NavigationDrawerItem(
                    icon = { Icon(painterResource(R.drawable.ic_coin), contentDescription = stringResource(R.string.coin_flipper)) },
                    label = { Text(stringResource(R.string.coin_flipper)) },
                    selected = selectedItemRoute == Screen.CoinFlipper.route,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Screen.CoinFlipper.route)
                        selectedItemRoute = Screen.CoinFlipper.route
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
            }
        },
        content = {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(stringResource(R.string.app_name)) },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Filled.Menu, contentDescription = "Menu")
                            }
                        }
                    )
                }
            ) { padding ->
                Box(modifier = Modifier.padding(padding)){
                    Navigation(navController = navController)
                }
            }
        }
    )
}

@Composable
fun DrawerHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = R.drawable.logo), // Ваша іконка
            contentDescription = stringResource(R.string.app_name),
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(id = R.string.app_name), // Назва додатку
            style = MaterialTheme.typography.headlineSmall
        )
    }
}

@Composable
fun NumberGenerator(modifier: Modifier = Modifier){

}
@Composable
fun DiceRoller(modifier: Modifier = Modifier){

}

@Composable
fun CoinFlipper(modifier: Modifier = Modifier){

}
