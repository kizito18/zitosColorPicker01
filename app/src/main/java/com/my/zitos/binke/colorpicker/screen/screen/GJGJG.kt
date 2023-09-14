package com.my.zitos.binke.colorpicker.screen

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.my.zitos.binke.colorpicker.viewModel.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


data class TouchEvent(var x: Float, var y: Float, var radius: Float)
//data class TouchEvent(var x: Float, var y: Float)



@Composable
fun HomeScreen(windowSize: WindowSize, navigationController : NavController
               , homeViewModel: HomeViewModel
) {

    val context = LocalContext.current
    var dominantColor by remember { mutableStateOf(Color.White) }
    var dominantColorCode by remember { mutableStateOf("#FFFFFF") }

    val coroutineScope = rememberCoroutineScope()
    var imageOffset by remember { mutableStateOf(Offset.Zero) }



    var isImageShown  by remember { mutableStateOf(true) }


    // var selectedSingleImageUri by remember { mutableStateOf(homeViewModel.selectedSingleImageUri) }

    var selectedSingleImageUri by remember { mutableStateOf<Uri?>(null) }


    var bitmap by remember { mutableStateOf<Bitmap?>(null) }


    var scale by remember { mutableFloatStateOf(1f) }

    val imageSize = remember { mutableStateOf(Size.Zero) }

    var imageOffsetLimitX  by remember { mutableFloatStateOf(0f) }
    var imageOffsetLimitY by remember { mutableFloatStateOf(0f) }

    var radiusInPixels by remember { mutableFloatStateOf(50.0f)}  // Your radius value in pixels

    val radiusInDp = convertPixelsToDp(radiusInPixels)
    //val radiusInDp by remember { mutableFloatStateOf(convertPixelsToDp(radiusInPixels)) }









    // New Android 13 Photo picker


    val singleImagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {

                selectedSingleImageUri = uri


                isImageShown = false



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















    Surface (modifier = Modifier
        .fillMaxSize()
        .fillMaxHeight()
        .fillMaxWidth()){

        Box(modifier = Modifier
            .fillMaxSize()
            .fillMaxWidth()
            .fillMaxHeight(), contentAlignment = Alignment.Center) {


            Column(
                modifier = Modifier

                    .fillMaxSize()
                    .align(Alignment.Center)
                    .drawBehind {
                        val tileSizeHeight = size.height / 18
                        val tileSizeWidth = size.width / 12
                        val rowCount = size.height / tileSizeHeight
                        val colCount = size.width / tileSizeWidth

                        repeat(rowCount.toInt()) { row ->
                            repeat(colCount.toInt()) { col ->
                                val color =
                                    if ((row + col) % 2 == 0) Color(0xFF212121) else Color(
                                        0xFF424242
                                    )
                                drawRect(
                                    color,
                                    Offset(col * tileSizeWidth, row * tileSizeHeight),
                                    Size(tileSizeWidth, tileSizeHeight)
                                )
                            }
                        }
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            )
            {


                // Action Bar layout
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .zIndex(1f)
                ) {


                    IconButton(onClick = {


                        pickImage()


                    }) {

                        Icon(
                            painter = painterResource(id = R.drawable.icon_photo),
                            contentDescription = "pick image icon ",
                            tint = Color.LightGray,
                            modifier = Modifier.size(35.dp)
                        )


                    }






                    var openColorExpandLayout  by remember { mutableStateOf(false) }


                    IconButton(onClick = {

                        if (!isImageShown) {

                            openColorExpandLayout = true

                        }else{

                            Toast.makeText(context, "choose an image first", Toast.LENGTH_SHORT).show()

                        }


                    }) {


                        Icon(
                            painter = painterResource(id = R.drawable.baseline_colorize_24),
                            contentDescription = "show openColorExpandLayout ",
                            tint = Color.LightGray,
                            modifier = Modifier.size(35.dp)
                        )


                    }







                    //openColarExpandLayout
                    when (openColorExpandLayout){

                        true -> {


                            IconButton(onClick = {

                                radiusInPixels += 4.0f


                            }) {


                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_plus_one_24),
                                    contentDescription = "increase color picker size",
                                    tint = Color.LightGray,
                                    modifier = Modifier.size(35.dp)
                                )


                            }





                            IconButton(onClick = {


                                radiusInPixels -= 4.0f


                            }) {


                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_horizontal_rule_24),
                                    contentDescription = "reduce   color picker size",
                                    tint = Color.LightGray,
                                    modifier = Modifier.size(35.dp)
                                )


                            }

                        }


                        else -> {}
                    }










                }


                //Image layout
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .weight(1f)
                        , contentAlignment = Alignment.Center
                )
                {

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.Center)
                        , contentAlignment = Alignment.Center
                    )
                    {


                        LaunchedEffect(selectedSingleImageUri) {
                            coroutineScope.launch {
                                bitmap =
                                    loadImageBitmapResource(context, selectedSingleImageUri)
                            }
                        }



                        /*

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


                         */








                        val onTouchEvent: (TouchEvent) -> Unit = { touchEvent ->
                            if (bitmap != null) {
                                val x = touchEvent.x - imageOffset.x
                                val y = touchEvent.y - imageOffset.y

                                val bitmapWidth = bitmap!!.width.toFloat()
                                val bitmapHeight = bitmap!!.height.toFloat()

                                val scaledX = (x / imageSize.value.width) * bitmapWidth
                                val scaledY = (y / imageSize.value.height) * bitmapHeight

                                val radius = touchEvent.radius

                                if (scaledX >= 0 && scaledX < bitmapWidth && scaledY >= 0 && scaledY < bitmapHeight) {
                                    // val radius = touchEvent.radius

                                    // Create a list to store colors within the specified radius
                                    val colorsWithinRadius = mutableListOf<Int>()

                                    // Loop through the pixels within the radius and add them to the list
                                    for (i in (scaledX - radius).toInt()..(scaledX + radius).toInt()) {


                                        for (j in (scaledY - radius).toInt()..(scaledY + radius).toInt()) {


                                            if (i >= 0 && i < bitmapWidth && j >= 0 && j < bitmapHeight) {
                                                val color = bitmap!!.getPixel(i, j)
                                                colorsWithinRadius.add(color)
                                            }
                                        }


                                    }

                                    // Calculate the average color within the radius
                                    if (colorsWithinRadius.isNotEmpty()) {
                                        val mixedColor = mixColors(colorsWithinRadius)
                                        dominantColor = Color(mixedColor)
                                        dominantColorCode = "#" + Integer.toHexString(mixedColor)
                                    }
                                }
                            }
                        }




                        /*

                            val onTouchEvent: (TouchEvent) -> Unit = { touchEvent ->
                                if (bitmap != null) {


                                    val centerX =imageSize.value.width/2// (imageWidth * scale / 2) // Center X-coordinate
                                    val centerY = imageSize.value.height/2//(imageHeight * scale / 2) // Center Y-coordinate

                                    val x = touchEvent.x - imageOffset.x
                                    val y = touchEvent.y - imageOffset.y

                                    val bitmapWidth = bitmap!!.width.toFloat()
                                    val bitmapHeight = bitmap!!.height.toFloat()

                                    val scaledX = (x / imageSize.value.width) * bitmapWidth
                                    val scaledY = (y / imageSize.value.height) * bitmapHeight

                                    val distance = kotlin.math.sqrt((scaledX - centerX).pow(2) + (scaledY - centerY).pow(2))

                                    if (distance <= touchEvent.radius) {
                                        val color = bitmap!!.getPixel(scaledX.toInt(), scaledY.toInt())
                                        val sRGBColor = convertToSRGB(color)
                                        dominantColor = Color(sRGBColor)
                                        dominantColorCode = "#" + Integer.toHexString(sRGBColor)
                                    }
                                }
                            }

                            */







                        if (!isImageShown) {



                            Image(
                                painter = rememberAsyncImagePainter(selectedSingleImageUri),
                                contentDescription = null,
                                modifier = Modifier
                                    .wrapContentSize()
                                    /*
                                        always add the graphicsLayer before the pointerInput
                                        else the image or object will offset but will stll be
                                        at it position
                                         */
                                    .graphicsLayer(

                                        scaleX = scale,
                                        scaleY = scale,
                                        translationX = imageOffset.x,
                                        translationY = imageOffset.y,
                                    )
                                    .align(Alignment.Center)


                                    .pointerInput(Unit) {

                                        detectTransformGestures { _, pan, zoom, _ ->


                                            /* i created a new variable called newScale and i
                                                 multiplied the scale and zoom to get the new scale
                                                 of the image it the only way
                                                 */
                                            val newScale = scale * zoom

                                            //Now i set the scale to the new scale value
                                            //  scale = newScale

                                            if (newScale in 1f..4f) {

                                                scale = newScale

                                            }


                                            // here i got the height and width of the image
                                            val imageWidth = size.width
                                            val imageHeight = size.height


                                            /*

                                                after i have gotten the new scale value

                                                 */


                                            if (imageOffset.x + pan.x * scale >= -imageWidth * scale / 2
                                                && imageOffset.x + pan.x * scale <= imageWidth * scale / 2
                                            ) {

                                                imageOffsetLimitX =
                                                    imageOffset.x + pan.x * scale


                                                /*
                                                    imageOffsetLimitX = imageOffset.x.coerceIn(

                                                        -imageWidth * scale / 2,
                                                        imageWidth * scale / 2

                                                    ) + pan.x * scale

                                                     */

                                            }


                                            if (imageOffset.y + pan.y * scale >= -imageHeight * scale / 2
                                                && imageOffset.y + pan.y * scale <= imageHeight * scale / 2
                                            ) {


                                                imageOffsetLimitY =
                                                    imageOffset.y + pan.y * scale

                                                /*
                                                    imageOffsetLimitY = imageOffset.y.coerceIn(

                                                        -imageHeight * scale / 2,
                                                        imageHeight * scale / 2

                                                    ) + pan.y * scale

                                                     */
                                            }


                                            imageOffset =
                                                Offset(imageOffsetLimitX, imageOffsetLimitY)

                                            imageSize.value =
                                                Size(imageWidth.toFloat(), imageHeight.toFloat())


                                            /*

                                            onTouchEvent(
                                                TouchEvent
                                                    (
                                                     x = (),
                                                    y =() ,
                                                    //3f // Radius of 3 units as a float
                                                )

                                            )


                                             */









                                            onTouchEvent(
                                                TouchEvent
                                                    (
                                                    (imageWidth * scale) / 2,//this will center it
                                                    (imageHeight * scale) / 2,//this will center it
                                                    radiusInPixels // radius
                                                )

                                            )


                                        }


                                    }, contentScale = ContentScale.Crop

                            )


                        }


                    }




                    // Pointer Listener BOX
                    Box(
                        modifier = Modifier

                            // Pointer Listener BOX

                            //  .height(imageSize.value.height.dp / 10.0.toFloat())
                            // .width(imageSize.value.width.dp / 10.0.toFloat())
                            .size(radiusInDp.dp)
                            .clip(CircleShape)
                            .border(BorderStroke(2.dp, Color.Cyan), shape = CircleShape)
                            .align(Alignment.Center)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Gray
                                    ), //startY = 0.5f
                                )
                            ),



                        )



                    /*

                    Box(
                        modifier = Modifier


                            // THIS WILL MAKE THE BOX SAME WITH THE IMAGE
                            .height(imageSize.value.height.dp / 2)
                            .width(imageSize.value.width.dp / 2)
                            //.size(radiusInDp.dp)
                            .clip(CircleShape)
                            .border(BorderStroke(2.dp, Color.Cyan), shape = CircleShape)
                            .align(Alignment.Center)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Gray
                                    ), //startY = 0.5f
                                )
                            ),
                        contentAlignment = Alignment.Center


                    )
                    {



                        Box(
                            modifier = Modifier

                                // Pointer Listener BOX

                              //  .height(imageSize.value.height.dp / 10.0.toFloat())
                               // .width(imageSize.value.width.dp / 10.0.toFloat())
                                .size(radiusInDp.dp)
                                .clip(CircleShape)
                                .border(BorderStroke(2.dp, Color.Cyan), shape = CircleShape)
                                .align(Alignment.Center)
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            Color.Gray
                                        ), //startY = 0.5f
                                    )
                                ),



                        )



                    }



                    */





                }





                // Ads layout
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                )
                {


                }


                //Display color Layout


                Column(
                    modifier = Modifier
                        .fillMaxHeight(0.35f)
                        .fillMaxWidth()
                        .background(dominantColor)
                )
                {

                    Text(text = "Dominant Color: $dominantColorCode")
                    Text(text = "Dominant imageWidth: ${imageSize.value.width}")
                    Text(text = "Dominant imageHeight: ${imageSize.value.height}")
                    Text(text = "Dominant Color: $scale")
                    Text(text = "touch xx${imageSize.value.width * scale / 2}")
                    Text(text = "touch yy${imageSize.value.height * scale / 2}")
                    Text(text = "imageSize ${imageSize.value}")
                    Text(text = "scale yy${scale}")
                    Text(text = "imageOffsetLimitX ${imageOffset.x}")
                    Text(text = "imageOffsetLimitY ${imageOffset.y}")
                    Text(text = "radiusInDp $radiusInDp")


                }


            }


        }




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


fun convertPixelsToDp(pixels: Float): Float {
    val densityDpi = Resources.getSystem().displayMetrics.densityDpi.toFloat()
    return pixels / (densityDpi / 160f) // 160 is the baseline density (mdpi)
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




// Function to mix a list of colors and return the average color
private fun mixColors(colors: List<Int>): Int {
    if (colors.isEmpty()) {
        return 0
    }

    var alphaSum = 0
    var redSum = 0
    var greenSum = 0
    var blueSum = 0







    for (color in colors) {
        alphaSum += android.graphics.Color.alpha(color)
        redSum += android.graphics.Color.red(color)
        greenSum += android.graphics.Color.green(color)
        blueSum += android.graphics.Color.blue(color)
    }

    val size = colors.size
    val mixedAlpha = alphaSum / size
    val mixedRed = redSum / size
    val mixedGreen = greenSum / size
    val mixedBlue = blueSum / size

    return android.graphics.Color.argb(mixedAlpha, mixedRed, mixedGreen, mixedBlue)
}