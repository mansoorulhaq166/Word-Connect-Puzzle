package com.wordgame.wordpuzzles.components

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.wordgame.wordpuzzles.models.GameLetter
import com.wordgame.wordpuzzles.models.GameSolution
import com.wordgame.wordpuzzles.ui.theme.WordLinkTheme


@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
fun GameSolutionItemPreview() {
    WordLinkTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            // Dummy data for preview
            val dummyLetters = mutableStateListOf<GameLetter>()
            dummyLetters.addAll(List(5) {
                GameLetter(label = "A", offset = Offset.Zero)
            })
            val dummySolution = GameSolution(letters = dummyLetters, animate = true)
            val dummySolutions = List(3) { dummySolution }
            // Preview the GameSolutionItem
            GameSolutionItem(
                solution = dummySolution,
                itemsize = 50.dp,
                solutions = dummySolutions,
                onTileClicked = { solutionIndex, letterIndex ->
                }
            )
        }
    }
}

@Composable
fun GameSolutionItem(
    solution: GameSolution,
    itemsize: Dp,
    solutions: List<GameSolution>,
    onTileClicked: (solutionIndex: Int, letterIndex: Int) -> Unit
) {
    var clickedIndex by remember { mutableIntStateOf(-1) }

    val rotate by animateFloatAsState(
        targetValue = if (solution.iscompleted && solution.animate) 360f else 0f,
        animationSpec = tween(300), label = ""
    )
    Box(
        modifier = Modifier
            .padding(top = 4.dp, bottom = 4.dp)
            .graphicsLayer {
                rotationX = rotate
            }
//            .shadow(
//                elevation = 4.dp,
//                shape = RoundedCornerShape(8.dp)
//            )
        ,
        contentAlignment = Alignment.Center
    ) {
        val modifier = Modifier
            .padding(2.dp)
            .size(itemsize)
        LazyRow {
            itemsIndexed(solution.letters) { index, letter ->
                GameLetterItem(modifier = modifier, letter = letter,
                    onClick = {
                        clickedIndex = index
                        onTileClicked(solutions.indexOf(solution), index)
                    },
                    onOffsetObtained = { offset ->
                        val solutionindex = solutions.indexOf(solution)
                        if (solutionindex != -1) {
                            solutions[solutionindex].letters[index] = letter.copy(offset = offset)
                        }
                    }
                )
            }
        }
    }
}