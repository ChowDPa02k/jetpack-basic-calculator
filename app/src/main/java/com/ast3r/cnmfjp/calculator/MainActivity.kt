package com.ast3r.cnmfjp.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ast3r.cnmfjp.calculator.ui.theme.CalculatorTheme
import net.objecthunter.exp4j.ExpressionBuilder


class MainActivity : ComponentActivity() {
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculatorTheme {
                val systemUiController = rememberSystemUiController()
                val useDarkIcons = MaterialTheme.colors.isLight
                SideEffect {
                    // Update all of the system bar colors to be transparent, and use
                    // dark icons if we're in light theme
                    systemUiController.setSystemBarsColor(
                        color = Color.Transparent,
                        darkIcons = useDarkIcons
                    )
                    // setStatusBarsColor() and setNavigationBarsColor() also exist
                }
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    DisplayArea(Display("Hello", "Android"))
                }
            }
        }
    }
}

data class Display(var calInput: String, var calResult: String)

@ExperimentalAnimationApi
@Composable
fun DisplayArea(calculator: Display) {

    var input by remember { mutableStateOf(calculator.calInput) }
    var result by remember { mutableStateOf(calculator.calResult) }
    var dotLocked by remember { mutableStateOf(false) }
    var calFinished by remember { mutableStateOf(false) }
    var calError by remember { mutableStateOf(false) }
    val symbolRegex = "[+\\-×÷]".toRegex()

    val delKeyColor by animateColorAsState(if(!calError) MaterialTheme.colors.primary else Color(0xffaf2323))
    val scrollState = rememberScrollState()

    fun clear() {
        input = ""
        result = "0"
        dotLocked = false
        calFinished = false
        calError = false
    }

    fun typeDigit(source: String, inputKey: String): String {
        if (calFinished) {
            clear()
            return inputKey
        }

        return if (source == calculator.calInput) {
            // Greeting Detection
            inputKey
        } else {
            source.plus(inputKey)
        }
    }

    fun typeSymbol(source: String, inputKey: String): String {
        var src = source
        if (calFinished and !calError) {
            src = result
            result = ""
            calFinished = false
        }
        if (src.isNotEmpty()) {
            // Greeting Detection
            if ((src == calculator.calInput) or calError) {
                src = "0"
            }
            // Repeat Detection
            if (symbolRegex.matches(src.last().toString())) {
                return src.substring(0, src.length - 1).plus(inputKey)
            } else {
                // Release Dot Lock
                if (dotLocked) {
                    dotLocked = false
                }
                return src.plus(inputKey)
            }
        } else {
            return "0".plus(inputKey)
        }
    }


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(if (isSystemInDarkTheme()) MaterialTheme.colors.background else Color.White)
        ) {
            // Input and Result
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f)
                    .padding(all = 16.dp),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Bottom,
                ) {
                AnimatedContent(
                    targetState = input,
                    transitionSpec = {
                        fadeIn() with fadeOut()
                    }
                ) { targetInput ->
                    Text(
                        text = targetInput,
                        color = MaterialTheme.colors.primaryVariant,
                        fontSize = 24.sp,
                        textAlign = TextAlign.End
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                AnimatedContent(
                    targetState = result,
                    transitionSpec = {
                    slideInVertically({fullHeight -> fullHeight}) + fadeIn() with slideOutVertically({fullHeight -> -fullHeight}) + fadeOut()
                }) { targetResult ->
                    SelectionContainer {
                        Text(
                            text = targetResult,
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.End,
                            maxLines = 1,
                            modifier = Modifier.fillMaxWidth().horizontalScroll(scrollState)
                        )
                    }
                }
            }
            // Spacer(modifier = Modifier.height(16.dp))
            // Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(4f)
            ) {
                Column(
                    modifier = Modifier
                        .weight(3f)
                        .background(if (isSystemInDarkTheme()) Color(0xff101010) else MaterialTheme.colors.background)
                ) {
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        TextButton(
                            onClick = {
                                input = typeDigit(input, "7")
                            },
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        ) {
                            Text(
                                text = "7",
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Light,
                                color = MaterialTheme.colors.surface
                            )
                        }
                        TextButton(
                            onClick = {
                                input = typeDigit(input, "8")
                            },
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        ) {
                            Text(
                                text = "8",
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Light, color = MaterialTheme.colors.surface
                            )
                        }
                        TextButton(
                            onClick = {
                                input = typeDigit(input, "9")
                            },
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        ) {
                            Text(
                                text = "9",
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Light, color = MaterialTheme.colors.surface
                            )
                        }
                    }
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        TextButton(
                            onClick = {
                                input = typeDigit(input, "4")
                            },
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        ) {
                            Text(
                                text = "4",
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Light, color = MaterialTheme.colors.surface
                            )
                        }
                        TextButton(
                            onClick = {
                                input = typeDigit(input, "5")
                            },
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        ) {
                            Text(
                                text = "5",
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Light, color = MaterialTheme.colors.surface
                            )
                        }
                        TextButton(
                            onClick = {
                                input = typeDigit(input, "6")
                            },
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        ) {
                            Text(
                                text = "6",
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Light, color = MaterialTheme.colors.surface
                            )
                        }
                    }
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        TextButton(
                            onClick = {
                                input = typeDigit(input, "1")
                            },
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        ) {
                            Text(
                                text = "1",
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Light, color = MaterialTheme.colors.surface
                            )
                        }
                        TextButton(
                            onClick = {
                                input = typeDigit(input, "2")
                            },
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        ) {
                            Text(
                                text = "2",
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Light, color = MaterialTheme.colors.surface
                            )
                        }
                        TextButton(
                            onClick = {
                                input = typeDigit(input, "3")
                            },
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        ) {
                            Text(
                                text = "3",
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Light, color = MaterialTheme.colors.surface
                            )
                        }
                    }
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        TextButton(
                            onClick = {
                                if(!dotLocked) {
                                    val i = typeDigit(input, ".")
                                    input = if (i == ".") {
                                        "0."
                                    } else {
                                        i
                                    }
                                    dotLocked = true
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        ) {
                            Text(
                                text = ".",
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Light, color = MaterialTheme.colors.surface
                            )
                        }
                        TextButton(
                            onClick = {
                                if(input != "0") {
                                    input = typeDigit(input, "0")
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        ) {
                            Text(
                                text = "0",
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Light, color = MaterialTheme.colors.surface
                            )
                        }
                        TextButton(
                            onClick = {
                                if (!calError) {
                                    val expression = ExpressionBuilder(
                                        input.replace("×", "*").replace("÷", "/")
                                    ).build()
                                    try {
                                        val expResult = expression.evaluate()
                                        result = expResult.toString()
                                    } catch (ex: ArithmeticException) {
                                        result = "Error"
                                        input = ""
                                        calError = true
                                    }
                                    calFinished = true
                                } else {
                                    clear()
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        ) {
                            Text(
                                text = "=",
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Light, color = MaterialTheme.colors.surface
                            )
                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .background(delKeyColor)
                ) {
                    // Delete Button
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onTap = {
                                        if (!calError) {
                                            if (input.isNotEmpty()) {
                                                if (input.last() == '.') {
                                                    dotLocked = false
                                                }
                                                input = input.substring(0, input.length - 1)
                                            }
                                        } else {
                                            clear()
                                        }
                                    },
                                    onLongPress = {
                                        clear()
                                    }
                                )
                            }
                    ) {
                        Text(
                            text = "DEL",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Light,
                            color = if (isSystemInDarkTheme()) Color.White else MaterialTheme.colors.background,
                        )
                    }
                    // + Button
                    TextButton(
                        onClick = {
                            input = typeSymbol(input, "+")
                        },
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "+",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Light,
                            color = if (isSystemInDarkTheme()) Color.White else MaterialTheme.colors.background
                        )
                    }
                    // - Button
                    TextButton(
                        onClick = {
                            input = typeSymbol(input, "-")
                        },
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "-",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Light,
                            color = if (isSystemInDarkTheme()) Color.White else MaterialTheme.colors.background
                        )
                    }
                    TextButton(
                        onClick = {
                            input = typeSymbol(input, "×")
                        },
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "×",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Light,
                            color = if (isSystemInDarkTheme()) Color.White else MaterialTheme.colors.background
                        )
                    }
                    TextButton(
                        onClick = {
                            input = typeSymbol(input, "÷")
                        },
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "÷",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Light,
                            color = if (isSystemInDarkTheme()) Color.White else MaterialTheme.colors.background
                        )
                    }
                }
            }
        }
    }

}


@ExperimentalAnimationApi
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CalculatorTheme {
        DisplayArea(Display("Input", "Result"))
    }
}