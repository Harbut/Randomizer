package den.harbut.randomizer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import den.harbut.randomizer.ui.randomizer_ui.MainScreen
import den.harbut.randomizer.ui.randomizer_ui.ViewModelFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModelFactory = remember { ViewModelFactory(applicationContext) }
            MainScreen(viewModelFactory)
        }
    }
}

