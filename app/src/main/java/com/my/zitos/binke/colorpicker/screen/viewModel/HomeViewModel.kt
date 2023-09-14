package com.my.zitos.binke.colorpicker.viewModel

import android.net.Uri
//import android.util.Size

import androidx.compose.ui.geometry.Size


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.ViewModel


class HomeViewModel : ViewModel() {

    // State to hold the selected image URI
    var selectedSingleImageUri by mutableStateOf<Uri?>(null)

    fun addUriFunction(newUri: Uri){

        selectedSingleImageUri = newUri

    }

    // State to hold the selected image URI
    var imageWidth by mutableStateOf<Dp?>(null)

    fun addWidthFunction(newWidth: Dp){

        imageWidth = newWidth

    }



    var imageHeight by mutableStateOf<Dp?>(null)

    fun addHeightFunction(newHeight: Dp){

        imageHeight = newHeight

    }


    var imageSize by mutableStateOf(Size.Zero)

    fun addSizeFunction(newSize: Size){

        imageSize = newSize

    }



}