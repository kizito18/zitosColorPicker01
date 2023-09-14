package com.my.zitos.binke.colorpicker.screen


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.my.zitos.binke.colorpicker.viewModel.HomeViewModel
import com.my.zitos.binke.colorpicker.viewModel.PickedImageDetailsViewModel


@Composable
fun PickImageScreen(windowSize: WindowSize, navigationController : NavController
                    , pickedImageScreenViewModel: PickedImageDetailsViewModel = viewModel()
                    , homeViewModel: HomeViewModel
){



    val context = LocalContext.current
    var dominantColor by remember { mutableStateOf(Color.White) }
    var dominantColorCode by remember { mutableStateOf("#FFFFFF") }

    val coroutineScope = rememberCoroutineScope()

    var imageWidth by remember { mutableStateOf(0.dp) }
    var imageHeight by remember { mutableStateOf(0.dp) }

    var isStarting = true


    var selectedSingleImageUri by remember { mutableStateOf(homeViewModel.selectedSingleImageUri) }








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
                    val tileSizeHeight = size.height / 20
                    val tileSizeWidth = size.width / 20
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



            Row(modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .background(Color.Red)) {

                IconButton(onClick = {

                     navigationController.navigate(route = Screen.Home.route)

                }) {

                    Icon(
                        painter = painterResource(id = R.drawable.icon_photo),
                        contentDescription = "hh",
                        tint = Color.LightGray,
                        modifier = Modifier.size(35.dp)
                    )


                }

            }


            Box(modifier = Modifier.fillMaxSize())
            {


                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center)
                ) {


                    Box(modifier = Modifier.fillMaxSize()) {


                        Image(
                            painter = rememberAsyncImagePainter(selectedSingleImageUri),
                            contentDescription = null,
                            modifier = Modifier
                                .wrapContentSize()
                                .align(Alignment.Center)


                                .layout { measurable, constraints ->


                                    val placeable = measurable.measure(constraints)

                                    // Update the width and height variables with the intrinsic size of the image

                                    imageWidth = placeable.width.toDp()
                                    imageHeight = placeable.height.toDp()


                                    homeViewModel.addWidthFunction(newWidth = imageWidth)
                                    homeViewModel.addHeightFunction(newHeight = imageHeight)









                                    layout(placeable.width, placeable.height) {
                                        // Place the image at the calculated offsets to center it

                                        placeable.place(

                                            0,0

                                        )

                                      //  navigationController.navigate(route = Screen.Home.route)


                                    }


                                }
                                .onSizeChanged {

                                  //  homeViewModel.addSizeFunction(newSize = it.toSize())


                                }


                        )
                    }










                    // Pointer Listener
                    Box(
                        modifier = Modifier


                            .size(3.dp)
                            .clip(CircleShape)
                            .border(BorderStroke(1.dp, Color.Cyan), shape = CircleShape)
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
            Text(text = "Dominant Color: $imageWidth")
            Text(text = "Dominant Color: $imageHeight")

            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(dominantColor)
            )
        }









    }





}






