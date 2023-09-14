package com.my.zitos.binke.colorpicker.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration


data class WindowSize(

    val width : WindowType,
    val height : WindowType

)



enum class WindowType{Compate,Medium,Expanded}


@Composable

fun rememberWindowSize() : WindowSize{

    val configuration = LocalConfiguration.current

    val screenWidth by remember(key1 = configuration) {
        mutableStateOf(configuration.screenWidthDp)
    }

    val screenHeight by remember(key1 = configuration) {
        mutableStateOf(configuration.screenHeightDp)
    }

    return WindowSize(
        width = getScreenWidth(screenWidth),
        height = getScreenHeight(screenHeight)
    )


}

fun getScreenWidth(widthss:Int) : WindowType = when {

    widthss < 600 -> WindowType.Compate

    widthss < 840 -> WindowType.Medium

    else -> WindowType.Expanded


}


fun getScreenHeight(heigthss:Int) : WindowType = when{

    heigthss < 480 -> WindowType.Compate

    heigthss < 900 -> WindowType.Medium

    else -> WindowType.Expanded



}