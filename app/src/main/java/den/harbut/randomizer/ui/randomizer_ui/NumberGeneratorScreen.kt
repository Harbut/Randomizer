package den.harbut.randomizer.ui.randomizer_ui

import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import den.harbut.randomizer.R
import den.harbut.randomizer.utils.realRange
import den.harbut.randomizer.utils.generateRandomNumbers

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun NumberGeneratorScreen(modifier: Modifier = Modifier){
    var randomNumbers by remember { mutableStateOf<List<Long>>(listOf(0)) }
    var showDialog by rememberSaveable { mutableStateOf(false) }
    var minNumber by rememberSaveable { mutableStateOf("0") }
    var maxNumber by rememberSaveable { mutableStateOf("10") }
    var numbersToGenerate by rememberSaveable { mutableStateOf("1") }
    var animationDuration by rememberSaveable { mutableStateOf("1000") }
    var avoidDuplicates by rememberSaveable { mutableStateOf(false) }
    var showSum by rememberSaveable { mutableStateOf(false) }
    var errorText by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        randomNumbers = generateRandomNumbers(minNumber, maxNumber, numbersToGenerate, avoidDuplicates)
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(stringResource(R.string.generate_number))
                }
                Spacer(modifier = Modifier.width(16.dp))
                IconButton(
                    onClick = { showDialog = true }
                ) {
                    Icon(painterResource(R.drawable.ic_tune), contentDescription = null /* TODO */)
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (randomNumbers.size < 2) {
                if(randomNumbers.isNotEmpty()){
                    RandomNumberCard(randomNumbers[0])
                }
            } else {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    randomNumbers.forEach { number ->
                        Text(
                            text = number.toString(),
                            style = MaterialTheme.typography.displayLarge.copy(fontSize = 60.sp),
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(4.dp)
                        )
                    }
                    if (randomNumbers.isEmpty()){
                        Text(stringResource(R.string.no_number_generated),
                            style = MaterialTheme.typography.displayLarge.copy(fontSize = 60.sp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
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
    var isMinNumberFocused by remember { mutableStateOf(false) }
    var isMaxNumberFocused by remember { mutableStateOf(false) }

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
                        if (newValue.length <= 8 && newValue.matches(Regex("-?\\d*"))) { // Обмеження до 8 символів
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
                        if (newValue.length <= 8 && newValue.matches(Regex("-?\\d*"))) { // Обмеження до 8 символів
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


                val numbersToGenerateMax = rememberSaveable(minNumber, maxNumber) {
                    val min = minNumber.toLongOrNull() ?: 0
                    val max = maxNumber.toLongOrNull() ?: 0
                    realRange(min, max).count().toString()
                }

                var numbersToGenerateState by remember(numbersToGenerate, numbersToGenerateMax, avoidDuplicates) {
                    mutableStateOf(numbersToGenerate)
                }
                var isFocused by rememberSaveable{ mutableStateOf(false) }

                LaunchedEffect(avoidDuplicates, numbersToGenerateMax, minNumberState, maxNumberState) {
                    if (avoidDuplicates && numbersToGenerateState.isNotEmpty() && numbersToGenerateState.toInt() > numbersToGenerateMax.toInt()) {
                        numbersToGenerateState = numbersToGenerateMax
                        onNumbersToGenerateChange(numbersToGenerateMax)
                    }
                }

                OutlinedTextField(
                    value = numbersToGenerateState,
                    onValueChange = { newValue ->
                        if (newValue.length <= 2 && newValue.all { it.isDigit() } || newValue.isEmpty()) {
                            var parsedValue = newValue

                            numbersToGenerateState =  parsedValue.toString()

                            if (avoidDuplicates && (parsedValue.toIntOrNull()
                                    ?: 0) > numbersToGenerateMax.toInt()
                            ) {
                                numbersToGenerateState = numbersToGenerateMax
                                onNumbersToGenerateChange(numbersToGenerateMax)
                            } else {
                                onNumbersToGenerateChange(numbersToGenerateState)
                            }
                        }
                    },
                    label = { Text(stringResource(R.string.numbers_to_generate)) },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    enabled = !avoidDuplicates || numbersToGenerateMax != "0",
                    modifier = Modifier.onFocusChanged { focusState -> // Обробка зміни фокусу
                        isFocused = focusState.isFocused
                        if (!focusState.isFocused && numbersToGenerateState.isEmpty()) { // Перевірка при втраті фокусу
                            numbersToGenerateState = "1"
                            onNumbersToGenerateChange("1")
                        }
                    }
                )

                var animationDurationState by remember { mutableStateOf(animationDuration) }
                var isAnimationDurationFocused by remember { mutableStateOf(false) }

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
                    Checkbox(checked = avoidDuplicates, onCheckedChange = { newValue ->
                        onAvoidDuplicatesChange(newValue)
                        if (newValue && numbersToGenerateState.isNotEmpty() && numbersToGenerateState.toInt() > numbersToGenerateMax.toInt()) {
                            numbersToGenerateState = numbersToGenerateMax
                            onNumbersToGenerateChange(numbersToGenerateMax)
                        }
                    })
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
fun RandomNumberCard(number: Long, modifier: Modifier = Modifier){
    val text = number.toString()
    val fontSize = when(text.length){
        in 0..3 -> 65.sp
        4 -> 55.sp
        5 -> 45.sp
        6 -> 40.sp
        in 7..8 -> 30.sp
        else -> 25.sp
    }
    Card(modifier =  Modifier
        .padding(4.dp)
        .size(150.dp)) {
        Box(contentAlignment = Alignment.Center,
            modifier = Modifier
            .padding(8.dp)
            .fillMaxSize()){
            Text(
                text = number.toString(),
                fontSize = fontSize,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
fun RandomNumbersCards(numbers: List<Long>, modifier: Modifier = Modifier){

}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun NumberGeneratorScreenPreview(){
    NumberGeneratorScreen()
}

@Preview(showBackground = true)
@Composable
fun RandomNumberCardPreview(){
    RandomNumberCard(999L
    )
}