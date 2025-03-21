package com.wordgame.wordpuzzles.presentation.components.game

import android.graphics.Paint
import android.graphics.Rect
import android.view.MotionEvent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wordgame.wordpuzzles.domain.models.KeyPadButton
import com.wordgame.wordpuzzles.presentation.theme.WordLink_YellowDark
import java.lang.Math.cos
import java.lang.Math.sin

const val STATE_IDLE = 0
const val STATE_DOWN = 1
const val STATE_MOVE = 2
const val STATE_UP = 3
const val BUTTON_DEFAULT = 0
const val BUTTON_SELECTED = 1

@Preview
@Composable
fun KeyPadPreview() {
    // Sample data for KeyPadButton
    val button1 = KeyPadButton(label = "A")
    val button2 = KeyPadButton(label = "B")
    val button3 = KeyPadButton(label = "C")
    val button4 = KeyPadButton(label = "D")
    val button5 = KeyPadButton(label = "E")

    // Create a list of KeyPadButton
    val sampleList = listOf(button1, button2, button3, button4, button5)

    // Call the KeyPad composable with the sample data
    KeyPad(
        shuffle = false, // Set to true if you want to see the shuffle animation
        list = sampleList,
        onSetShuffle = { /* Implement the shuffle logic if needed */ },
        onSelected = { list -> /* Implement the onSelected logic if needed */ },
        onCompleted = { list -> /* Implement the onCompleted logic if needed */ }
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun KeyPad(
    shuffle:Boolean,
    list:List<KeyPadButton>,
    onSetShuffle:()->Unit = {},
    onSelected:(List<KeyPadButton>)->Unit = {},
    onCompleted:(List<KeyPadButton>)->Unit = {}
) {
    val listSelected = mutableListOf<KeyPadButton>()
    val paddingCanvas = 40.dp
    var state by remember{
        mutableIntStateOf(STATE_IDLE)
    }
    var currentPos by remember{
        mutableStateOf(Offset(0f,0f))
    }
    //path and templine
    val path by remember {
       mutableStateOf( Path())
    }
    val pathTmp by remember {
        mutableStateOf(Path())
    }
    //

    var shouldSelect by remember{
        mutableStateOf(false)
    }
    var buttonRadius by remember{
        mutableFloatStateOf(0f)
    }
    val tweenspeed = 200
    //rotation
    val rotation by animateFloatAsState(
        targetValue = if(shuffle) 360f else 0f,
        animationSpec = tween(tweenspeed), label = ""
    )
    //
    var buttonType by remember{
        mutableIntStateOf(BUTTON_DEFAULT)
    }
    //animation
    var centerMain by remember{
        mutableStateOf(Offset(0f,0f))
    }
    var sizeMain by remember {
        mutableStateOf(Size(0f,0f))
    }
    val radiusMain by animateFloatAsState(
        targetValue = if(shuffle) 50f else Math.min(sizeMain.width,sizeMain.height)/2.5f,
        animationSpec = tween(200), label = ""
    ){
        onSetShuffle()
    }
    //
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(top = 50.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Canvas(modifier =
        Modifier
            .fillMaxSize()
            //.padding(paddingCanvas)
            .pointerInteropFilter {
                when (it.action) {
                    MotionEvent.ACTION_DOWN -> {
                        state = STATE_DOWN
                        currentPos = Offset(it.x, it.y)
                    }

                    MotionEvent.ACTION_MOVE -> {
                        state = STATE_MOVE
                        currentPos = Offset(it.x, it.y)
                    }

                    MotionEvent.ACTION_UP -> {
                        state = STATE_UP

                    }
                }
                true
            }
        ) {
            centerMain = center
            sizeMain = size
            buttonRadius = size.width * 0.08f
            val offsetAngle = 360f/list.size
            when(state){
                STATE_IDLE ->{

                }
                STATE_DOWN ->{
                    listSelected.clear()
                    list.forEach { item ->
                        val istouched = isCircleTouched(
                            currentPos.x,
                            currentPos.y,
                            item.center.x,
                            item.center.y,
                            buttonRadius
                        )
                        if (istouched) {
                            listSelected.add(item)
                            path.moveTo(item.center.x, item.center.y)
                            if(!shouldSelect){
                                onSelected(listSelected)
                            }
                            shouldSelect = true
                            return@forEach
                        }
                    }

                }
                STATE_MOVE ->{
                    if(shouldSelect){
                        if(listSelected.isNotEmpty()){
                            val last = listSelected.last()
                            pathTmp.reset()
                            pathTmp.moveTo(last.center.x,last.center.y)
                            pathTmp.lineTo(currentPos.x, currentPos.y)
                        }
                        list.forEach { item ->
                            val istouched = isCircleTouched(
                                currentPos.x,
                                currentPos.y,
                                item.center.x,
                                item.center.y,
                                buttonRadius
                            )
                            if (istouched) {
                                if(listSelected.size > 1 && isGoingBack(item,listSelected)){
                                    listSelected.removeLast()
                                    path.reset()
                                    path.moveTo(listSelected.first().center.x, listSelected.first().center.y)
                                    for (i in 1 until listSelected.size) {
                                        val selected = listSelected[i]
                                        path.lineTo(selected.center.x, selected.center.y)
                                    }
                                } else {
                                    if (!listSelected.contains(item)) {
                                        listSelected.add(item)
                                        path.lineTo(item.center.x, item.center.y)
                                    }
                                }
                                onSelected(listSelected)
                               return@forEach
                            }
                        }
                    }

                }
                STATE_UP ->{
                    if(listSelected.isNotEmpty()){
                        onCompleted(listSelected)
                        shouldSelect = false
                        listSelected.clear()
                        path.reset()
                        pathTmp.reset()
                        buttonType = BUTTON_DEFAULT
                    }
                }
            }
            // Draw buttons
            repeat(list.count()) { index->
                val angle = index * offsetAngle
                val offset = getButtonCenter(center,radiusMain,angle)
                //Preparing button to draw
                list[index].center = offset
                val button = list[index]
                //End Preparing

                rotate(rotation){
                    // Draw button circle
                    if(listSelected.contains(button)){
                        val listColorsSelected = listOf(Color(0xFF0175E7),Color(0xFF01A2FF))
                        val brushButton = Brush.linearGradient(
                            colors = listColorsSelected,
                            start = Offset(
                                button.center.x ,
                                button.center.y
                            ),
                            end = Offset(
                                button.center.x + (buttonRadius+buttonRadius),
                                button.center.y + (buttonRadius+buttonRadius)
                            )
                        )
                        //Button Circle
                        drawCircle(
                            brush =brushButton,
                            center = button.center,
                            radius = buttonRadius
                        )
                    } else {
                        val listColorsDefault = listOf(Color(0xFF01A2FF),Color(0xFF0175E7))
                        val brushButton = Brush.linearGradient(
                            colors = listColorsDefault,
                            start = Offset(
                                button.center.x + (buttonRadius),
                                button.center.y + (buttonRadius)
                            ),
                            end = Offset(
                                button.center.x - (buttonRadius),
                                button.center.y - (buttonRadius)
                            )
                        )
                        //Button Circle
                        drawCircle(
                            brush =brushButton,
                            center = button.center,
                            radius = buttonRadius
                        )
                    }

                    val brushBorderLinearDefault =  Brush.linearGradient(
                        colors = listOf(Color(0xFF0175E7),Color(0xFF0175E7)),
                        start = Offset(
                            button.center.x + (buttonRadius+buttonRadius),
                            button.center.y + (buttonRadius+buttonRadius)),
                        end = Offset(button.center.x - (buttonRadius),
                            button.center.y - (buttonRadius))
                    )
                    drawCircle(
                        brush = brushBorderLinearDefault,
                        center = button.center,
                        radius = buttonRadius+5,
                        style = Stroke(width = 10f)
                    )

                    // Draw text in the center of the button circle
                    drawIntoCanvas { canvas ->
                        val text = button.label
                        val textPaint = Paint().apply {
                            color = Color.White.toArgb()
                            textAlign = Paint.Align.CENTER
                            textSize = 24.dp.toPx()
                        }
                        val textBounds = Rect()
                        textPaint.getTextBounds(text, 0, text.length, textBounds)
                        val textX = button.center.x
                        val textY = button.center.y + (textBounds.height()/2f)
                        canvas.nativeCanvas.drawText(text, textX, textY, textPaint)
                    }
                }
                //End Draw Button
            }

            //Draw Path
            val stroke = Stroke(
                width = 20f,
                cap = StrokeCap.Round,
                join = StrokeJoin.Bevel,
            )
            drawPath(
                color = WordLink_YellowDark,
                path = path,
                style = stroke
            )
            drawPath(
                color = WordLink_YellowDark,
                path = pathTmp,
                style = stroke
            )

        }
    }
}

// Helper function to calculate button center based on angle
private fun getButtonCenter(center: Offset, radius: Float, angle: Float): Offset {
    val radians = Math.toRadians(angle.toDouble())
    val x = center.x + (radius * cos(radians)).toFloat()
    val y = center.y + (radius * sin(radians)).toFloat()
    return Offset(x, y)
}
private fun isCircleTouched(
    touchX: Float,
    touchY: Float,
    centerX: Float,
    centerY: Float,
    r: Float
): Boolean {
    val distanceX = touchX - centerX
    val distanceY = touchY - centerY
    return (distanceX * distanceX) + (distanceY * distanceY) <= r * r
}
private fun isGoingBack(button: KeyPadButton, listselected:List<KeyPadButton>): Boolean {
    var back = false
    if(listselected.isNotEmpty()){
        val last = listselected[listselected.size - 2]
        if (button.id == last.id) {
            back = true
        }
    }
    return back
}