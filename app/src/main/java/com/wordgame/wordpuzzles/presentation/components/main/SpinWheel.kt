package com.wordgame.wordpuzzles.presentation.components.main

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.commandiron.spin_wheel_compose.DefaultSpinWheel
import com.commandiron.spin_wheel_compose.SpinWheelDefaults
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.wordgame.wordpuzzles.R
import kotlinx.coroutines.delay

@Composable
@Preview
fun SpinWheelView() {
    // SpinWheel(onSpinFinished = {})
}

@Composable
fun SpinWheel(onSpinFinished: (Int, String, Int) -> Unit) {
    val iconList = rememberIconList(
        listOf(
            R.drawable.gem_default,
            R.drawable.hint_default,
            R.drawable.boost_default,
            R.drawable.gem_default,
            R.drawable.hint_default,
        )
    )
    val textList = listOf("+3", "+1", "+1", "+1", "+2")

    var isSpinning by remember { mutableStateOf(false) }
    var selectedPieIndex by remember { mutableIntStateOf(0) }

    val modifiedIconList = iconList.toMutableList()
    val modifiedTextList = textList.toMutableList()
    val temp = modifiedIconList[0]
    val tempText = textList[0]

    modifiedIconList[0] = iconList[selectedPieIndex]
    modifiedIconList[selectedPieIndex] = temp

    modifiedTextList[0] = textList[selectedPieIndex]
    modifiedTextList[selectedPieIndex] = tempText

    // Timer to update selectedPieIndex while spinning
    LaunchedEffect(isSpinning) {
        val interval = 300 // Adjust the interval as needed
        while (isSpinning) {
            selectedPieIndex = (0..4).random()
        //    val tempText = textList[0]
        //    textList[0] = textList[selectedPieIndex]
        //    textList[selectedPieIndex] = tempText
            delay(interval.toLong())
        }
    }
    repeat(1) {
        Box(
            modifier = Modifier.rotate(45f)
        ) {
            Column {
                DefaultSpinWheel(
                    //   modifier = Modifier.rotate(15f),
                    dimensions = SpinWheelDefaults.spinWheelDimensions(
                        spinWheelSize = 270.dp,
                        frameWidth = 20.dp,
                        selectorWidth = 0.dp
                    ),
                    colors = SpinWheelDefaults.spinWheelColors(
                        frameColor = Color(0xFF2B2D42), // Dark gray
                        dividerColor = Color(0xFFEDF2F4), // Light gray
                        pieColors = listOf(
                            Color(0xFFEF233C), // Teal
                            Color(0xFF955290), // Gold
                            Color(0xFF955251), // Maroon
                            Color(0xFF5E548E), // Purple
                            Color(0xFF5E5443)
                        )
                    ),
                    animationAttr = SpinWheelDefaults.spinWheelAnimationAttr(
                        pieCount = 5,
                        durationMillis = 4000,
                        delayMillis = 200,
                        rotationPerSecond = 2f,
                        easing = FastOutSlowInEasing,
                        startDegree = 90f
                    ),
                    isSpinning = isSpinning,
                    onClick = { isSpinning = !isSpinning },
                    onFinish = {
                       // selectedPieIndex = (0..4).random()
                        val selectedIconName = formatIconName(selectedPieIndex)
                        val parsedText = parseGiftText(textList[selectedPieIndex])
                        onSpinFinished(selectedPieIndex, selectedIconName, parsedText)
                        isSpinning = false
                    }
                ) { pieIndex ->
                    Row(
                        //           horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxSize()
                    ) {

                        Image(
                            painter = modifiedIconList[pieIndex],
                            contentDescription = null,
                            modifier = Modifier
                                .size(32.dp)
                                .rotate(-45f)
                        )
                        val text = modifiedTextList[pieIndex]
                        Text(
                            text = text,
                            color = Color.White,
                            fontSize = 12.sp,
                            modifier = Modifier
                                .rotate(-45f)
                        )
                    }
                }
            }
            Image(
                painter = painterResource(id = R.drawable.arrow_circle),
                contentDescription = "Arrow",
                modifier = Modifier
                    .align(Alignment.Center)
                    .rotate(135f)
                    .size(32.dp)
            )
        }
    }
}

@Composable
fun rememberIconList(resourceIds: List<Int>): List<Painter> {
    return resourceIds.map { painterResource(id = it) }
}

private fun formatIconName(selectedIndex: Int): String {
    val iconNames =
        listOf("gem_default", "hint_default", "boost_default", "gem_default", "hint_default")
    return iconNames[selectedIndex].removeSuffix("_default")
}

private fun parseGiftText(text: String): Int {
    return text.removePrefix("+").toInt()
}