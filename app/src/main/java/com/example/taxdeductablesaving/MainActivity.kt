package com.example.taxdeductablesaving

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taxdeductablesaving.ui.theme.TaxDeductableSavingTheme
import java.text.NumberFormat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TaxDeductableSavingTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.primary
                ) {
                    AppScreen()
                }
            }
        }
    }
}

@Composable
fun AppScreen() {
    var amountInput by remember { mutableStateOf("") }

    val amount = amountInput.toDoubleOrNull() ?: 0.0

    val radioOptions = listOf(
        R.string.bracket_18200, R.string.bracket_45000, R.string.bracket_120000,
        R.string.bracket_180000, R.string.bracket_highest
    )
    val (selectedOption, onOptionSelected) = remember {
        mutableStateOf(radioOptions[0])
    }
    val taxAmount = getTaxAmount(selectedOption)
    val adjustedCost = calculateAdjustedCost(amount, taxAmount)

    Column(
        modifier = Modifier.padding(32.dp)
    ) {
        Text(
            text = stringResource(id = R.string.calculate_cost),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.income_bracket),
            fontSize = 18.sp
        )
        SelectTaxBracket(radioOptions, selectedOption, onOptionSelected)
        Spacer(Modifier.height(16.dp))
        InputNumberField(value = amountInput, onValueChange = { amountInput = it })
        Spacer(Modifier.height(16.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = stringResource(R.string.reduced_cost, adjustedCost),
                modifier = Modifier.align(Alignment.CenterHorizontally)
                    .padding(8.dp),
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Composable
fun SelectTaxBracket(
    radioOptions: List<Int>,
    selectedOption: Int,
    onOptionSelected: (Int) -> Unit
) {
    Column {
        radioOptions.forEach {
            Row(
                Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (it == selectedOption),
                        onClick = { onOptionSelected(it) }
                    )
                    .padding(2.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.onPrimary)
            ) {
                RadioButton(
                    selected = (it == selectedOption),
                    onClick = {
                        onOptionSelected(it)
                    },
                )
                Text(
                    text = stringResource(id = it),
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .align(Alignment.CenterVertically),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputNumberField(
    value: String,
    onValueChange: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(stringResource(id = R.string.input_label)) },
        modifier = Modifier.fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.secondary),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
    )
}


private fun getTaxAmount(selectedOption: Int): Double {
    return when (selectedOption) {
        R.string.bracket_18200 -> 0.0
        R.string.bracket_45000 -> 0.19
        R.string.bracket_120000 -> 0.325
        R.string.bracket_180000 -> 0.37
        else -> 0.45
    }
}

private fun calculateAdjustedCost(cost: Double, taxAmount: Double): String {
    val adjustedCost = cost - cost * taxAmount
    return NumberFormat.getCurrencyInstance().format(adjustedCost)
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TaxDeductableSavingTheme {
        AppScreen()
    }
}