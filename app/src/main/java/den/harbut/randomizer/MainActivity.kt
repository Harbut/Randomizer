package den.harbut.randomizer

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random
import den.harbut.randomizer.ui.randomizer_ui.MainScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}

@Composable
fun RandomNumberGeneratorApp() {
    var minNumber by rememberSaveable { mutableStateOf("1") }
    var maxNumber by rememberSaveable { mutableStateOf("10") }
    var showDialog by rememberSaveable { mutableStateOf(false) }
    var randomNumber by rememberSaveable { mutableStateOf(0L) }
    var errorText by rememberSaveable { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val toastText = stringResource(R.string.copied_to_clipboard)

    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = R.mipmap.ic_launcher_foreground),
                contentDescription = "Random Number Generator Icon",
                modifier = Modifier
                    .size(150.dp),
                contentScale = ContentScale.Fit
            )

            Text(
                text = stringResource(R.string.app_name),
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = minNumber,
                onValueChange = { newValue ->
                    minNumber = newValue.filter { it.isDigit() || it == '-' }
                },
                label = { Text(stringResource(id = R.string.min_number)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                visualTransformation = NumberTransformation()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = maxNumber,
                onValueChange = { newValue ->
                    maxNumber = newValue.filter { it.isDigit() || it == '-' }
                },
                label = { Text(stringResource(id = R.string.max_number)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                visualTransformation = NumberTransformation()
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                try {
                    val min = minNumber.toLong()
                    val max = maxNumber.toLong()


                    val realMin = min(min, max)
                    val realMax = max(min, max)

                    randomNumber = Random.nextLong(realMin, realMax + 1)
                    errorText = ""
                    showDialog = true

                    
                    focusManager.clearFocus()
                    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow((context as? ComponentActivity)?.currentFocus?.windowToken, 0)
                } catch (e: NumberFormatException) {
                    errorText = "Введіть коректні числа."
                } catch(e: IllegalArgumentException){
                    errorText = "Діапазон має містити хоча б одне число"
                }
            }) {
                Text(stringResource(id = R.string.generate_number))
            }
            Spacer(modifier = Modifier.height(8.dp))
            if (errorText.isNotEmpty()) {
                Text(text = errorText, color = MaterialTheme.colorScheme.error)
            }
            RandomNumberDialog(
                showDialog = showDialog,
                randomNumber = randomNumber,
                onDismissRequest = { showDialog = false },
                onCopyClick = {
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("Random Number", randomNumber.toString())
                    clipboard.setPrimaryClip(clip)
                    Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}


@Composable
fun RandomNumberDialog(
    showDialog: Boolean,
    randomNumber: Long,
    onDismissRequest: () -> Unit,
    onCopyClick: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = { Text(stringResource(R.string.random_number)) },
            text = {
                Text(
                    text = randomNumber.toString(),
                    fontSize = 48.sp,
                    modifier = Modifier.clickable { onCopyClick() }
                )
            },
            confirmButton = {
                Button(onClick = onDismissRequest) {
                    Text("OK")
                }
            }
        )
    }
}

class NumberTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return TransformedText(
            AnnotatedString(text.text.filter { it.isDigit() || it == '-' }),
            OffsetMapping.Identity
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RandomNumberGeneratorApp()
}