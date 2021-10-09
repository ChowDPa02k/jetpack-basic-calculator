package com.ast3r.cnmfjp.calculator.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
        primary = Color(0xff2d6dd1),
        primaryVariant = Color(0xff2660bc),
        secondary = Teal200,
        background = Color.Black,
        surface = Color.White
)

private val LightColorPalette = lightColors(
        primary = Color(0xff2962ff),
        primaryVariant = Color(0xff3155cc),
        secondary = Teal200,
        background = Color(0xfff0f0f0),
        surface = Color.Black

        /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun CalculatorTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
            colors = colors,
            typography = Typography,
            shapes = Shapes,
            content = content
    )
}