package den.harbut.randomizer.ui.randomizer_ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import den.harbut.randomizer.R
import den.harbut.randomizer.utils.generateRandomNumbers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CoinFlipperScreen(modifier: Modifier = Modifier){
    var coinSide by rememberSaveable { mutableStateOf(0) }
    var showDialog by rememberSaveable { mutableStateOf(false) }
    var animationDuration by rememberSaveable { mutableStateOf("1000") }
    var showDescriptor by rememberSaveable { mutableStateOf(false) }
    var isGenerating by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                if (showDescriptor) {
                    Text("Value: ${if(coinSide == 0) "head" else "tails"}", fontSize = 16.sp)
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            coinSide = (0..1).random()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            if (isGenerating) stringResource(R.string.generated) else stringResource(
                                R.string.generate
                            )
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    IconButton(
                        onClick = { showDialog = true }
                    ) {
                        Icon(
                            painterResource(R.drawable.ic_tune),
                            contentDescription = null /* TODO */
                        )
                    }
                }
            }
        })
    { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ){
            Image(painter = painterResource(coinImage(coinSide)),
                contentDescription = null,
                modifier
                    .size(150.dp)
                    .padding(bottom = 16.dp))
        }
    }
    if(showDialog){
        ParameterDialog(
            onDismissRequest = {showDialog = false},
            animationDuration = animationDuration,
            onAnimationDurationChange = {animationDuration = it},
            showDescriptor = showDescriptor,
            onShowDescriptorChange = {showDescriptor = it}
        )
    }
}

private fun coinImage(index: Int): Int{
    return if(index == 0) R.drawable.ic_coin_head else R.drawable.ic_coin_tails
}

@Composable
fun ParameterDialog(
    onDismissRequest: () -> Unit,
    animationDuration: String,
    onAnimationDurationChange: (String) -> Unit,
    showDescriptor: Boolean,
    onShowDescriptorChange: (Boolean) -> Unit
){
    var animationDurationState by remember { mutableStateOf(animationDuration)}
    var isAnimationDurationFocused by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismissRequest) {
        Card(modifier = Modifier.padding(16.dp)) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(stringResource(R.string.parameters), style = MaterialTheme.typography.headlineSmall)

                OutlinedTextField(
                    value = animationDurationState,
                    onValueChange = { newValue ->
                        if (newValue.length <= 5 && newValue.all { it.isDigit() } || newValue.isEmpty()) { // Обмеження довжини
                            animationDurationState = newValue
                        }
                    },
                    label = { Text(stringResource(R.string.animation_duration)) },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    modifier = Modifier.onFocusChanged { focusState ->
                        isAnimationDurationFocused = focusState.isFocused
                        if (!focusState.isFocused) {
                            val parsedValue = animationDurationState.toIntOrNull() ?: 0
                            val correctedValue = parsedValue.coerceIn(0, 10000)
                            animationDurationState = correctedValue.toString()
                            onAnimationDurationChange(correctedValue.toString())
                        }
                    }
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = showDescriptor, onCheckedChange = onShowDescriptorChange)
                    Text("Show Descriptor text")
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = {onDismissRequest()}) {
                        Text(stringResource(R.string.done))
                    }
                }
            }
        }
    }
}