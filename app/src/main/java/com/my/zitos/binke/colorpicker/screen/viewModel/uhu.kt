package com.my.zitos.binke.colorpicker.viewModel

/*
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.my.zitos.binke.colorpicker.screen.R

import com.my.zitos.binke.colorpicker.screen.TouchEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking



data class TouchEvent(var x: Float, var y: Float)


@Composable
fun mainb() {

    var dominantColor by remember { mutableStateOf(Color.White) }
    var dominantColorCode by remember { mutableStateOf("#FFFFFF") }
    var imageOffset by remember { mutableStateOf(Offset.Zero) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var isStarting  by remember { mutableStateOf(true) }


    var selectedSingleImageUri by remember { mutableStateOf<Uri?>(null) }




    var scale by remember { mutableStateOf(1f) }
    var sizess = remember { mutableStateOf(Size.Zero) }




    var rotation by remember { mutableStateOf(0f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val state = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
        scale *= zoomChange
        // rotation += rotationChange
        imageOffset += offsetChange



    }


    val singleImagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {


                selectedSingleImageUri = uri


                isStarting = false


            }


        }
    )



    val onTouchEvent: (TouchEvent) -> Unit = { touchEvent ->
        if (bitmap != null) {
            val x = touchEvent.x - imageOffset.x
            val y = touchEvent.y - imageOffset.y

            val bitmapWidth = bitmap!!.width.toFloat()
            val bitmapHeight = bitmap!!.height.toFloat()

            val scaledX = (x / sizess.value.width) * bitmapWidth
            val scaledY = (y / sizess.value.height) * bitmapHeight

            if (scaledX >= 0 && scaledX < bitmapWidth && scaledY >= 0 && scaledY < bitmapHeight) {
                val color = bitmap!!.getPixel(scaledX.toInt(), scaledY.toInt())

                val sRGBColor = convertToSRGB(
                    color
                )
                dominantColor = Color(sRGBColor)
                dominantColorCode = "#" + Integer.toHexString(sRGBColor)
            }



        }
    }






    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {


        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .fillMaxWidth()

        ) {


            Box(modifier = Modifier.fillMaxSize())
            {


                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center)
                ) {


                    Box(modifier = Modifier.fillMaxSize()) {


                        if (!isStarting) {


                            Image(
                                painter = rememberAsyncImagePainter(selectedSingleImageUri),
                                contentDescription = null,
                                modifier = Modifier
                                    .wrapContentSize()
                                    .align(Alignment.Center)


                                    .graphicsLayer(
                                        scaleX = scale,
                                        scaleY = scale,
                                        // rotationZ = rotation,
                                        translationX = imageOffset.x,
                                        translationY = imageOffset.y,

                                        )

                                    .pointerInput(Unit) {

                                        detectTransformGestures { centroid, pan, zoom, rotation ->


                                            scale *= zoom

                                            imageOffset += pan

                                            onTouchEvent(
                                                TouchEvent
                                                    (
                                                    scale / 2,
                                                    scale / 2
                                                )

                                            )


                                        }


                                    }, contentScale = ContentScale.Crop
                            )


                        }

                    }


                    // Pointer Listener
                    Box(
                        modifier = Modifier


                            .size(10.dp)
                            .clip(CircleShape)
                            .border(BorderStroke(2.dp, Color.Cyan), shape = CircleShape)
                            .align(Alignment.Center)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Gray
                                    ), startY = 0.5f
                                )
                            )


                    )
                }
            }


        }



        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(text = "Dominant Color: $dominantColorCode")

            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(dominantColor)
            )

            Box(modifier = Modifier.wrapContentSize()) {

                Row(
                    modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth()
                ) {

                    IconButton(onClick = {


                        singleImagePickerLauncher.launch(

                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)

                        )




                    }) {

                        Icon(
                            painter = painterResource(id = R.drawable.icon_photo),
                            contentDescription = "hh",
                            tint = Color.LightGray,
                            modifier = Modifier.size(35.dp)
                        )


                    }

                }
            }
        }



    }




}




private fun convertToSRGB(color: Int): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        Color(
            alpha = android.graphics.Color.alpha(color) / 255f,
            red = android.graphics.Color.red(color) / 255f,
            green = android.graphics.Color.green(color) / 255f,
            blue = android.graphics.Color.blue(color) / 255f,
            colorSpace = ColorSpaces.Srgb
        ).toArgb()
    } else {
        color
    }
}





private fun loadImageBitmapResource(context: Context, imageUri: Uri?): Bitmap? {
    return runBlocking(Dispatchers.IO) {
        try {
            val inputStream = context.contentResolver.openInputStream(imageUri!!)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()
            bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}




 */