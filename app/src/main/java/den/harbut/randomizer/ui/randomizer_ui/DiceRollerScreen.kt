package den.harbut.randomizer.ui.randomizer_ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import den.harbut.randomizer.R
import den.harbut.randomizer.utils.generateRandomNumbers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DiceRollerScreen(modifier: Modifier = Modifier){
    var randomDices by rememberSaveable { mutableStateOf(listOf(0)) }
    var showDialog by rememberSaveable { mutableStateOf(false) }
    val minNumber = "1"
    val maxNumber = "6"
    var diceCount by rememberSaveable { mutableStateOf("1") }
    var animationDuration by rememberSaveable { mutableStateOf("1000") }
    var showSum by rememberSaveable { mutableStateOf(false) }

    var isGenerating by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Scaffold (
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            Column(horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                if(showSum) {
                    Text("Sum: ${if(isGenerating) "" else randomDices.sum()}", fontSize = 16.sp)
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
                            scope.launch {
                                isGenerating = true
                                randomDices =
                                    generateRandomNumbers(minNumber, maxNumber, diceCount, false)
                                delay(animationDuration.toLongOrNull() ?: 0)
                                isGenerating = false
                            }
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
        }) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ){
            if(isGenerating){
                DicesGridRolling(randomDices)
            } else{
            DicesGrid(randomDices)
                }
        }
    }
    if(showDialog){
        ParametersDialog(
            onDismissRequest = {showDialog = false},
            diceCount = diceCount,
            onDiceCountChange = {diceCount = it},
            animationDuration = animationDuration,
            onAnimationDurationChange = {animationDuration = it},
            showSum = showSum,
            onShowSumChange = {showSum = it}
        )
    }
}

@Composable
fun DicesGrid(numbers: List<Int>, modifier: Modifier = Modifier){
    LazyVerticalGrid(
        columns = GridCells.Fixed(
            if(numbers.size < 3){
                numbers.size
            } else {
                3
            },
        ),
        modifier = Modifier.padding(12.dp)
    ) {
        items(numbers.size) { index ->
            Image(painter = painterResource(diceImage(numbers[index])),
                contentDescription = index.toString(),
                modifier
                    .size(150.dp)
                    .padding(bottom = 16.dp))
        }
    }
}

@Composable
fun ParametersDialog(
    onDismissRequest: () -> Unit,
    diceCount: String,
    onDiceCountChange: (String) -> Unit,
    animationDuration: String,
    onAnimationDurationChange: (String) -> Unit,
    showSum: Boolean,
    onShowSumChange: (Boolean) -> Unit
){

    var diceCountState by remember{ mutableStateOf(diceCount) }
    var animationDurationState by remember { mutableStateOf(animationDuration) }
    var isDiceCountFocused by remember{ mutableStateOf(false) }
    var isAnimationDurationFocused by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = {onDismissRequest()}) {
        Card(modifier = Modifier.padding(16.dp)) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ){
                Text(stringResource(R.string.parameters), style = MaterialTheme.typography.headlineSmall)

                OutlinedTextField(
                    value = diceCountState,
                    onValueChange = { newValue ->
                        if (newValue.length <= 3 && newValue.matches(Regex("\\d*"))) { // Обмеження до 8 символів
                            diceCountState = newValue
                            onDiceCountChange(newValue)
                        }
                    },
                    label = { Text(stringResource(R.string.numbers_to_generate)) },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.onFocusChanged { focusState ->
                        isDiceCountFocused = focusState.isFocused
                        if (!focusState.isFocused && diceCountState.isEmpty()) {
                            diceCountState = "1"
                            onDiceCountChange("1")
                        } else {
                            onDiceCountChange(diceCountState)
                        }
                    }
                )

                OutlinedTextField(
                    value = animationDurationState,
                    onValueChange = { newValue ->
                        if (newValue.length <= 5 && newValue.all { it.isDigit() } || newValue.isEmpty()) { // Обмеження довжини
                            animationDurationState = newValue
                            onAnimationDurationChange(newValue)
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
                    Checkbox(checked = showSum, onCheckedChange = onShowSumChange)
                    Text(stringResource(R.string.show_sum))
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

@Composable
fun DicesGridRolling(numbers: List<Int>, modifier: Modifier = Modifier){
    LazyVerticalGrid(
        columns = GridCells.Fixed(
            if(numbers.size < 3){
                numbers.size
            } else {
                3
            },
        ),
        modifier = Modifier.padding(12.dp)
    ) {
        items(numbers.size) { index ->
            ContinuouslyRotatingDice()
        }
    }
}


private fun diceImage(index: Int): Int{
    return when(index){
        1 -> R.drawable.ic_dice1
        2 -> R.drawable.ic_dice2
        3 -> R.drawable.ic_dice3
        4 -> R.drawable.ic_dice4
        5 -> R.drawable.ic_dice5
        else -> R.drawable.ic_dice6
    }
}

@Composable
fun ContinuouslyRotatingDice() {
    val infiniteTransition = rememberInfiniteTransition()
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 400, easing = LinearEasing)
        )
    )

    FlippableCardR(
        frontContent = {
            // Содержимое передней стороны
            Image(painter = painterResource(R.drawable.ic_dice6),
                contentDescription = null,
                modifier = Modifier
                    .padding(16.dp)
                    .size(150.dp))
        },
        backContent = {},
        rotationAngle = angle
    )
}



@Composable
fun FlippableCardR(
    frontContent: @Composable () -> Unit,
    backContent: @Composable () -> Unit,
    rotationAngle: Float
) {
    Box(contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                transformOrigin = TransformOrigin(0.5f, 0.5f)
                rotationY = rotationAngle
            }
    ) {
        Box(contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .zIndex(-1f) // Отправляем заднюю сторону назад
                .graphicsLayer {
                    transformOrigin = TransformOrigin(0.5f, 0.5f)
                    rotationY = -180f
                }
        ) {
            backContent()
        }
        frontContent()
    }
}
