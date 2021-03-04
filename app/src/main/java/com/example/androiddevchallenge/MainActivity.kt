/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import android.os.*
import android.util.Log
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.androiddevchallenge.ui.theme.MyTheme
import java.util.*


class MainActivity : AppCompatActivity() {
    var content by mutableStateOf(0f)
    var time by mutableStateOf(0)
    var currentTime = 0

    val radius = 120
    val imageWidth = 30
    val strokWidth = 10f

    //状态
    var isStarted by mutableStateOf(false)

    lateinit var timer: Timer

    @RequiresApi(Build.VERSION_CODES.R)
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val displayWidth = windowManager.defaultDisplay.width
        val des = resources.displayMetrics.density


        setContent {
            MyTheme {

                Column() {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                    ) {
                        Canvas(modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp), onDraw = {

                            drawArc(
                                color = Color.Black,
                                startAngle = 180f,
                                sweepAngle = 360f,
                                useCenter = false,
                                size = Size(
                                    (radius * 2).dp.value * des,
                                    (radius * 2).dp.value * des
                                ),
                                style = Stroke(width = strokWidth),
                                topLeft = Offset((displayWidth - radius * des * 2) / 2, 30f),

                                )
                        })

                        Canvas(modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp), onDraw = {

                            drawArc(
                                color = Color.Red,
                                startAngle = -90f,
                                sweepAngle = content,
                                useCenter = false,
                                size = Size(
                                    (radius * 2).dp.value * des,
                                    (radius * 2).dp.value * des
                                ),
                                style = Stroke(width = strokWidth),
                                topLeft = Offset((displayWidth - radius * des * 2) / 2, 30f),

                                )
                        })
                        Text(
                            text = time.toString(), color = Color.Black,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    vertical = radius.dp
                                ),
                            textAlign = TextAlign.Center,
                        )
                    }


                    ConstraintLayout(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)

                    ) {

                        val (image1, image2, image3) = createRefs()

                        Image(
                            painter = painterResource(id = R.mipmap.iv_reduce),
                            contentDescription = "reduce",
                            modifier = Modifier
                                .width(imageWidth.dp)
                                .height(imageWidth.dp)
                                .clickable {
                                    if (time > 0) {
                                        time--
                                    }
                                }
                                .constrainAs(image1) {
                                    start.linkTo(parent.start, margin = 30.dp)
                                    top.linkTo(parent.top)

                                })

                        Image(
                            painter = painterResource(id = R.mipmap.iv_start),
                            contentDescription = "start",
                            modifier = Modifier
                                .width(imageWidth.dp)
                                .height(imageWidth.dp)
                                .clickable {
                                    isStarted = !isStarted

                                    if (isStarted) {
                                        timer = Timer()
                                        currentTime = time
                                        timer.schedule(object : TimerTask() {
                                            override fun run() {
                                                if (time > 0) {
                                                    content += 360f / currentTime
                                                    time--
                                                }
                                                if (time == 0) {
                                                    timer.cancel()
                                                }

                                            }
                                        }, 1000, 1000)
                                    } else {
                                        timer.cancel()
                                    }
                                }
                                .constrainAs(image2) {
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                    top.linkTo(parent.top)
                                    centerHorizontallyTo(parent)

                                })

                        Image(
                            painter = painterResource(id = R.mipmap.iv_add),
                            contentDescription = "add",
                            modifier = Modifier
                                .width(imageWidth.dp)
                                .height(imageWidth.dp)
                                .clickable {
                                    if (time == 0) {
                                        content = 0f
                                    }
                                    if (time < 10) {
                                        time++
                                    }
                                }
                                .constrainAs(image3) {
                                    end.linkTo(parent.end, margin = 30.dp)
                                    top.linkTo(parent.top)


                                })


                    }


                }


            }
        }


    }
}

// Start building your app here!
@Composable
fun MyApp(content: Int) {
    Surface(color = MaterialTheme.colors.background) {
        Text(text = content.toString())
    }
}

@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        MyApp(10)
    }
}

@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        MyApp(10)
    }
}



