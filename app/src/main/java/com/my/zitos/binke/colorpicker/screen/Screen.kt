package com.my.zitos.binke.colorpicker.screen

sealed class Screen(val route: String) {
    object Home : Screen("home_screen")
    object PickedImageScn : Screen ("picked_image_screen")
    object DisplayClickedItemScreen : Screen("display_clicked_item_screen")
}