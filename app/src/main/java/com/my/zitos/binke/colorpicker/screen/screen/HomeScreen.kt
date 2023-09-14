package com.my.zitos.binke.colorpicker.screen


/*
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color.alpha
import android.graphics.Color.blue
import android.graphics.Color.green
import android.graphics.Color.red
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
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.my.zitos.binke.colorpicker.R
import com.my.zitos.binke.colorpicker.WindowSize
import com.my.zitos.binke.colorpicker.WindowType
import com.my.zitos.binke.colorpicker.viewModel.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


data class TouchEvent(var x: Float, var y: Float)



@Composable
fun HomeScreenss(windowSize: WindowSize, navigationController : NavController
               , homeViewModel: HomeViewModel
) {

    val context = LocalContext.current
    var dominantColor by remember { mutableStateOf(Color.White) }
    var dominantColorCode by remember { mutableStateOf("#FFFFFF") }

    val coroutineScope = rememberCoroutineScope()
    var imageOffset by remember { mutableStateOf(Offset.Zero) }



    var isStarting  by remember { mutableStateOf(true) }


   // var selectedSingleImageUri by remember { mutableStateOf(homeViewModel.selectedSingleImageUri) }

    var selectedSingleImageUri by remember { mutableStateOf<Uri?>(null) }


    var scale by remember { mutableFloatStateOf(1f) }

    val imageSize = remember { mutableStateOf(Size.Zero) }

    var imageOffsetLimitX  = 0f
    var imageOffsetLimitY = 0f


    // New Android 13 Photo picker


    val singleImagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {

                selectedSingleImageUri = uri


                isStarting = false



            }


        }
    )







    // Old photo picker


    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val uri = data?.data
            if (uri != null) {

                homeViewModel.addUriFunction(newUri = uri)


            }
        }
    }


    fun pickImage() {

        when (windowSize.width) {

            WindowType.Medium -> {


                // Function to launch the file picker with the hidden folder option

                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = "image/*"
                    // Enable showing hidden folders by setting an extra in the intent
                    putExtra("android.provider.extra.SHOW_ADVANCED", true)
                }

                // Start the file picker activity
                filePickerLauncher.launch(intent)


            }

            WindowType.Expanded -> {


                // Function to launch the file picker with the hidden folder option

                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = "image/*"
                    // Enable showing hidden folders by setting an extra in the intent
                    putExtra("android.provider.extra.SHOW_ADVANCED", true)
                }

                // Start the file picker activity
                filePickerLauncher.launch(intent)


            }

            else -> {


                singleImagePickerLauncher.launch(

                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)

                )


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
                .drawBehind {
                    val tileSizeHeight = size.height / 15
                    val tileSizeWidth = size.width / 10
                    val rowCount = size.height / tileSizeHeight
                    val colCount = size.width / tileSizeWidth

                    repeat(rowCount.toInt()) { row ->
                        repeat(colCount.toInt()) { col ->
                            val color =
                                if ((row + col) % 2 == 0) Color.Gray else Color.DarkGray
                            drawRect(
                                color,
                                Offset(col * tileSizeWidth, row * tileSizeHeight),
                                Size(tileSizeWidth, tileSizeHeight)
                            )
                        }
                    }
                }
        ) {




            Box(modifier = Modifier.fillMaxSize())
            {


                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center),

                ) {









                    Box(modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center), Alignment.Center) {






                            var bitmap by remember { mutableStateOf<Bitmap?>(null) }


                            LaunchedEffect(selectedSingleImageUri) {
                                coroutineScope.launch {
                                    bitmap = loadImageBitmapResource(context, selectedSingleImageUri)
                                }
                            }



                        //bitmap = loadImageBitmapResource( context,selectedSingleImageUri)






                        val onTouchEvent: (TouchEvent) -> Unit = { touchEvent ->
                                if (bitmap != null) {
                                    val x = touchEvent.x - imageOffset.x
                                    val y = touchEvent.y - imageOffset.y

                                    val bitmapWidth = bitmap!!.width.toFloat()
                                    val bitmapHeight = bitmap!!.height.toFloat()

                                    val scaledX = (x / imageSize.value.width) * bitmapWidth
                                    val scaledY = (y / imageSize.value.height) * bitmapHeight



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




                        if (!isStarting) {


                            Image(
                                painter = rememberAsyncImagePainter(selectedSingleImageUri),
                                contentDescription = null,
                                modifier = Modifier
                                    .wrapContentSize()
                                    .graphicsLayer(

                                        scaleX = scale,
                                        scaleY = scale,
                                        translationX = imageOffset.x,
                                        translationY = imageOffset.y,
                                    )
                                    .align(Alignment.Center)

                                    /*
                                    .zoomable(rememberZoomState())

                                         */

                                    .pointerInput(Unit) {

                                        detectTransformGestures { _, pan, zoom, _ ->

                                          //  detectTransformGestures { centroid, pan, zoom, rotationChange ->


                                            val newScale = scale.coerceIn(1f,4f) * zoom

                                            scale = newScale


                                            val imageWidth= size.width
                                            val imageHeight = size.height






                                            if (imageOffset.x + pan.x * scale >= -imageWidth * scale/2 && imageOffset.x + pan.x * scale <= imageWidth * scale/2 ){



                                                 imageOffsetLimitX = imageOffset.x.coerceIn(

                                                    -imageWidth * scale/2 , imageWidth * scale/2

                                                ) + pan.x * scale



                                            }


                                            if (imageOffset.y + pan.y * scale >= -imageHeight * scale/2 && imageOffset.y + pan.y * scale <= imageHeight * scale/2 ){



                                                 imageOffsetLimitY = imageOffset.y.coerceIn(

                                                    -imageHeight * scale/2 , imageHeight * scale/2

                                                ) + pan.y * scale



                                            }





                                            imageOffset = Offset(imageOffsetLimitX,imageOffsetLimitY)




                                            imageSize.value = Size(imageWidth * scale, imageHeight * scale)

                                            onTouchEvent(
                                                TouchEvent
                                                    (
                                                    imageWidth * scale  / 2,
                                                    imageHeight * scale / 2
                                                )

                                            )





                                        }


                                    }








                                , contentScale = ContentScale.Crop
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
        )
        {
            Text(text = "Dominant Color: $dominantColorCode")
            Text(text = "Dominant Color: $imageOffset")






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


                            pickImage()




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
            alpha = alpha(color) / 255f,
            red = red(color) / 255f,
            green = green(color) / 255f,
            blue = blue(color) / 255f,
            colorSpace = ColorSpaces.Srgb
        ).toArgb()
    } else {
        color
    }
}



private fun loadImageBitmapResource(context: Context, imageUri: Uri?): Bitmap? {
    return runBlocking(Dispatchers.IO) {
        try {
            if (imageUri != null) {
                val inputStream = context.contentResolver.openInputStream(imageUri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream?.close()
                bitmap
            } else {
                // Handle the case where imageUri is null, e.g., by returning a default image or null
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}



 */
 */
 */
