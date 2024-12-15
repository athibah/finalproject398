package com.example.finalproj398

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finalproj398.ui.theme.Finalproj398Theme
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Finalproj398Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CoffeeMenuInputs(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun PreviewCoffeeMenu() {
    Finalproj398Theme {
        CoffeeMenuInputs()
    }
}
@Composable
fun CoffeeMenuInputs(modifier: Modifier = Modifier) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        WelcomeWithInputs(modifier = modifier)
    }
}

// I learned how to do snackbar in the 197 project
@Composable
fun WelcomeWithInputs(modifier: Modifier = Modifier) {
    val snackbarHost = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GreetingMessage()
        enterName()
        enterNumber()
        selectDrink()
        Spacer(modifier = Modifier.height(16.dp))
        SubmitOrderButton(snackbarHostState = snackbarHost, scope = scope)
        SnackbarHost(hostState = snackbarHost, modifier = Modifier.align(Alignment.CenterHorizontally))
    }
}

// I used a different color sample from online because I didnt like the default ones and
// wanted it to resemble coffee
@Composable
fun GreetingMessage() {
    Text(
        text = "Welcome to Browny Cafe <3",
        color = Color(0xFF6A4A3C),
        fontSize = 24.sp,
        modifier = Modifier.padding(bottom = 16.dp)
    )
}

// I ask the user for their name and did a brown box so it looked more aesthetic
@Composable
fun enterName() {
    var keepName by remember { mutableStateOf(TextFieldValue("")) }

    Text(
        text = "Please enter your name:",
        color = Color(0xFF6A4A3C),
        fontSize = 16.sp,
        modifier = Modifier.padding(bottom = 4.dp)
    )
    Surface(
        color = Color(0xFF6A4A3C),
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .padding(bottom = 8.dp)
    ) {
        BasicTextField(
            value = keepName,
            onValueChange = { keepName = it },
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .fillMaxSize(),
            textStyle = androidx.compose.ui.text.TextStyle(
                color = Color.White,
                fontSize = 16.sp
            )
        )
    }
}

// I ask user for their phone number
@Composable
fun enterNumber() {
    var phoneState by remember { mutableStateOf(TextFieldValue("")) }

    Text(
        text = "And your phone number:",
        color = Color(0xFF6A4A3C),
        fontSize = 16.sp,
        modifier = Modifier.padding(bottom = 4.dp)
    )
    Surface(
        color = Color(0xFF6A4A3C),
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .padding(bottom = 16.dp)
    ) {
        BasicTextField(
            value = phoneState,
            onValueChange = { phoneState = it },
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .fillMaxSize(),
            textStyle = androidx.compose.ui.text.TextStyle(
                color = Color.White,
                fontSize = 16.sp
            )
        )
    }
}

// A few drink options
@Composable
fun selectDrink() {
    val drinkOptions = listOf(
        "Coffee" to 3.0,
        "Macchiato" to 5.5,
        "Latte" to 5.0,
        "Cappuccino" to 4.5
    )
    var selectedDrink by remember { mutableStateOf<Pair<String, Double>?>(null) }
    var quantity by remember { mutableStateOf(1) }

    Text(
        text = "What drink would you like?",
        color = Color(0xFF6A4A3C),
        fontSize = 16.sp,
        modifier = Modifier.padding(bottom = 8.dp)
    )
    drinkOptions.forEach { (drink, price) ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            RadioButton(
                selected = (selectedDrink?.first == drink),
                onClick = { selectedDrink = drink to price },
                colors = RadioButtonDefaults.colors(
                    selectedColor = Color(0xFF6A4A3C),
                    unselectedColor = Color.Gray
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "$drink - ${price} USD", color = Color.Black, fontSize = 16.sp)
        }
    }
    if (selectedDrink != null) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Select quantity:",
                color = Color(0xFF6A4A3C),
                fontSize = 16.sp
            )
            Row {
                Button(onClick = { if (quantity > 1) quantity -= 1 }) {
                    Text(text = "-")
                }
                Text(
                    text = "$quantity",
                    color = Color.Black,
                    fontSize = 16.sp,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                Button(onClick = { quantity += 1 }) {
                    Text(text = "+")
                }
            }
        }
        Text(
            text = "Total Price: ${selectedDrink!!.second * quantity} USD",
            color = Color(0xFF6A4A3C),
            fontSize = 16.sp,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

// I added a colorfuls submit order button with a bright color
@Composable
fun SubmitOrderButton(snackbarHostState: SnackbarHostState, scope: CoroutineScope) {
    Button(
        onClick = {
            scope.launch { //added a snackbar button to show the user the order was placed successfully
                snackbarHostState.showSnackbar(
                    message = "Order Submitted Successfully!",
                    duration = SnackbarDuration.Short
                )
            }
        },
        colors = ButtonDefaults.buttonColors(containerColor = Color.Magenta),
        modifier = Modifier.padding(top = 16.dp)
    ) {
        Text(text = "Submit Order", color = Color.White)
    }
}
