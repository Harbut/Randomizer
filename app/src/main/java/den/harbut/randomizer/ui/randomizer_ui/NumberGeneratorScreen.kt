package den.harbut.randomizer.ui.randomizer_ui

import android.util.Log
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import den.harbut.randomizer.R
import den.harbut.randomizer.utils.generateRandomNumbers
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.Dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun NumberGeneratorScreen(modifier: Modifier = Modifier){
    var randomNumbers by rememberSaveable { mutableStateOf<List<Int>>(listOf(0)) }
    var showDialog by rememberSaveable { mutableStateOf(false) }
    var minNumber by rememberSaveable { mutableStateOf("0") }
    var maxNumber by rememberSaveable { mutableStateOf("10") }
    var numbersToGenerate by rememberSaveable { mutableStateOf("1") }
    var animationDuration by rememberSaveable { mutableStateOf("1000") }
    var avoidDuplicates by rememberSaveable { mutableStateOf(false) }
    var showSum by rememberSaveable { mutableStateOf(false) }
    var errorText by rememberSaveable { mutableStateOf("") }

    var isGenerating by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()


    val context = LocalContext.current

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                if (showSum) {
                    Text("Sum: ${randomNumbers.sum()}", fontSize = 16.sp)
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
                            isGenerating = true
                            Log.d("NumberGenerator", "isGeneration = $isGenerating")
                            scope.launch(Dispatchers.Default) {
                                try {
                                    // Логіка генерації чисел з розрахунком прогресу
                                    errorText = ""
                                    randomNumbers = generateRandomNumbersWithProgress(
                                        minNumber,
                                        maxNumber,
                                        numbersToGenerate,
                                        avoidDuplicates
                                    )
                                } catch (e: Exception) {
                                    errorText = e.message.toString()
                                } finally {
                                    isGenerating = false
                                    Log.d("NumberGenerator", "isGeneration = $isGenerating")
                                }
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
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (errorText.isNotEmpty()) {
                    ExceptionMessage(errorText)
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        RandomNumbersCards(randomNumbers)

                        if (randomNumbers.isEmpty()) {
                            Text(
                                stringResource(R.string.no_number_generated),
                                style = MaterialTheme.typography.displayLarge.copy(fontSize = 60.sp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
            if (isGenerating) {
                CircularProgressIndicator()
            }
        }

    }
    if(showDialog){
        ParametersDialog(
            onDismissRequest = { min, max -> // Лямбда з параметрами
                showDialog = false
                minNumber = min
                maxNumber = max
            },
            minNumber = minNumber,
            onMinNumberChange = {minNumber = it},
            maxNumber = maxNumber,
            onMaxNumberChange = {maxNumber = it},
            numbersToGenerate = numbersToGenerate,
            onNumbersToGenerateChange = {numbersToGenerate = it},
            animationDuration = animationDuration,
            onAnimationDurationChange = {animationDuration = it},
            avoidDuplicates = avoidDuplicates,
            onAvoidDuplicatesChange = {avoidDuplicates = it},
            showSum = showSum,
            onShowSumChange = {showSum = it}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParametersDialog(
    onDismissRequest: (min: String, max: String) -> Unit,
    minNumber: String,
    onMinNumberChange: (String) -> Unit,
    maxNumber: String,
    onMaxNumberChange: (String) -> Unit,
    numbersToGenerate: String,
    onNumbersToGenerateChange: (String) -> Unit,
    animationDuration: String,
    onAnimationDurationChange: (String) -> Unit,
    avoidDuplicates: Boolean,
    onAvoidDuplicatesChange: (Boolean) -> Unit,
    showSum: Boolean,
    onShowSumChange: (Boolean) -> Unit
) {
    var minNumberState by remember { mutableStateOf(minNumber) }
    var maxNumberState by remember { mutableStateOf(maxNumber) }
    var numbersToGenerateState by remember { mutableStateOf(numbersToGenerate) }
    var animationDurationState by remember { mutableStateOf(animationDuration) }
    var isMinNumberFocused by remember { mutableStateOf(false) }
    var isMaxNumberFocused by remember { mutableStateOf(false) }
    var isNumbersToGenerateFocused by remember { mutableStateOf(false) }
    var isAnimationDurationFocused by remember { mutableStateOf(false) }


    Dialog(onDismissRequest ={ onDismissRequest(minNumberState, maxNumberState)}) {
        Card(modifier = Modifier.padding(16.dp)) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(stringResource(R.string.parameters), style = MaterialTheme.typography.headlineSmall)

                OutlinedTextField(
                    value = minNumberState,
                    onValueChange = { newValue ->
                        if (newValue.length <= 9 && newValue.matches(Regex("-?\\d*"))) { // Обмеження до 8 символів
                            minNumberState = newValue
                        }
                    },
                    label = { Text(stringResource(R.string.min_number)) },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.onFocusChanged { focusState ->
                        isMinNumberFocused = focusState.isFocused
                        if (!focusState.isFocused && minNumberState.isEmpty()) {
                            minNumberState = "0"
                            onMinNumberChange("0")
                        } else {
                            onMinNumberChange(minNumberState)
                        }
                    }
                )

                OutlinedTextField(
                    value = maxNumberState,
                    onValueChange = { newValue ->
                        if (newValue.length <= 9 && newValue.matches(Regex("-?\\d*"))) { // Обмеження до 8 символів
                            maxNumberState = newValue
                        }
                    },
                    label = { Text(stringResource(R.string.max_number)) },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.onFocusChanged { focusState ->
                        isMaxNumberFocused = focusState.isFocused
                        if (!focusState.isFocused && maxNumberState.isEmpty()) {
                            maxNumberState = "0"
                            onMaxNumberChange("0")
                        } else {
                            onMaxNumberChange(maxNumberState)
                        }
                    }
                )

                OutlinedTextField(
                    value = numbersToGenerateState,
                    onValueChange = { newValue ->
                        if (newValue.length <= 3 && newValue.matches(Regex("\\d*"))) { // Обмеження до 8 символів
                            numbersToGenerateState = newValue
                            onNumbersToGenerateChange(newValue)
                        }
                    },
                    label = { Text(stringResource(R.string.numbers_to_generate)) },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.onFocusChanged { focusState ->
                        isNumbersToGenerateFocused = focusState.isFocused
                        if (!focusState.isFocused && numbersToGenerateState.isEmpty()) {
                            numbersToGenerateState = "1"
                            onNumbersToGenerateChange("1")
                        } else {
                            onNumbersToGenerateChange(numbersToGenerateState)
                        }
                    }
                )

                OutlinedTextField(
                    value = animationDurationState,
                    onValueChange = { newValue ->
                        if (newValue.length <= 5 && newValue.all { it.isDigit() } || newValue.isEmpty()) { // Обмеження довжини
                            animationDurationState = newValue
                            onAnimationDurationChange(newValue) // Оновлюємо зовнішнє значення
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
                            onAnimationDurationChange(correctedValue.toString()) // Додатковий виклик для фіналізації
                        }
                    }
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = avoidDuplicates, onCheckedChange = onAvoidDuplicatesChange)
                    Text(stringResource(R.string.avoid_duplicates))
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = showSum, onCheckedChange = onShowSumChange)
                    Text(stringResource(R.string.show_sum))
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = {onDismissRequest(minNumberState, maxNumberState)}) {
                        Text(stringResource(R.string.done))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RandomNumberCard(number: Int, order: Int = 0, size: Int = 130){
    val text = number.toString()

    Card(modifier =  Modifier
        .padding(4.dp)
        .size(size.dp)) {
        Box(modifier = Modifier
            .padding(8.dp)
            .fillMaxSize())
        {
                Text(text = (order + 1).toString(),
                    fontSize = 10.sp,
                    modifier = Modifier.align(Alignment.TopStart))
            ResponsiveText(
                text = number.toString(),
                modifier = Modifier.fillMaxWidth().padding(8.dp).align(Alignment.Center)
            )
        }
    }
}

@Composable
fun RandomNumbersCards(numbers: List<Int>, modifier: Modifier = Modifier){
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
            RandomNumberCard(numbers[index], order = index)
        }
    }
}



@Composable
fun ExceptionMessage(message: String, modifier: Modifier = Modifier){
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.ic_error),
            contentDescription = null,
            modifier = Modifier.size(100.dp)
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = message,
            textAlign = TextAlign.Center,
            fontSize = 20.sp
        )
    }

}

@Composable
fun ResponsiveText(text: String, modifier: Modifier = Modifier) {
    val textMeasurer = rememberTextMeasurer() // Для вимірювання ширини тексту
    var textSize by remember { mutableStateOf(12.sp) }
    var containerWidth by remember { mutableStateOf(0) }

    Box(
        modifier = modifier
            .onSizeChanged { size ->
                containerWidth = size.width
            }
    ) {
        LaunchedEffect(containerWidth, text) {
            if (containerWidth > 0) {
                // Підбираємо розмір шрифту, щоб текст уміщувався
                var size = 45f // Початковий розмір шрифту
                while (size > 8f) {
                    val measuredWidth = textMeasurer.measure(
                        text = AnnotatedString(text),
                        style = TextStyle(fontSize = size.sp)
                    ).size.width
                    if (measuredWidth <= containerWidth) {
                        break
                    }
                    size -= 1f // Зменшуємо розмір шрифту поступово
                }
                textSize = size.sp
            }
        }

        Text(
            text = text,
            fontSize = textSize,
            maxLines = 1,
            overflow = TextOverflow.Clip,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

suspend fun generateRandomNumbersWithProgress(
    minNumber: String,
    maxNumber: String,
    numbersToGenerate: String,
    avoidDuplicates: Boolean
): List<Int> {
    return generateRandomNumbers(minNumber, maxNumber, numbersToGenerate, avoidDuplicates)
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun NumberGeneratorScreenPreview(){
    NumberGeneratorScreen()
}

@Preview(showBackground = true)
@Composable
fun RandomNumberCardPreview(){
    RandomNumberCard(999
    )
}