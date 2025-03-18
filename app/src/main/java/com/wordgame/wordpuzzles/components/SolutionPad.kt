package com.wordgame.wordpuzzles.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.wordgame.wordpuzzles.models.GameLetter
import com.wordgame.wordpuzzles.models.GameSolution


val letter1 = GameLetter(label = "A", isvisible = true)
val letter2 = GameLetter(label = "B", isvisible = false)
val letter3 = GameLetter(label = "C", isvisible = true)
val letter4 = GameLetter(label = "D", isvisible = true)
val letter5 = GameLetter(label = "E", isvisible = false)

val lettersList = SnapshotStateList<GameLetter>().apply {
    add(letter1)
    add(letter2)
    add(letter3)
    add(letter4)
    add(letter5)
}

@Composable
@Preview
fun ViewSolutionPad() {
    // Sample data for preview
    val sampleSolutions = listOf(
        GameSolution("1", lettersList, iscompleted = true, animate = false),
        GameSolution("2", lettersList, iscompleted = true, animate = false)
        // Add more sample solutions as needed
    )
    val solutionsState = remember { mutableStateListOf(*sampleSolutions.toTypedArray()) }

    SolutionPad(solutions = solutionsState) { solutionIndex, letterIndex ->
        // Update the visibility of the clicked letter within the specified solution
        if (solutionIndex >= 0 && solutionIndex < solutionsState.size) {
            val solution = solutionsState[solutionIndex]
            if (letterIndex == 1) { // Assuming letter2 is at index 1
                solution.letters[letterIndex].isvisible = true
            }
        }
    }
}

@Composable
fun SolutionPad(
    solutions: List<GameSolution>,
    onTileClicked: (solutionIndex: Int, letterIndex: Int) -> Unit
) {
    val height = LocalConfiguration.current.screenHeightDp
    val small: Dp
    val medium: Dp
    val large: Dp
    if (height <= 600) {
        small = 28.dp
        medium = 32.dp
        large = 48.dp
    } else {
        small = 32.dp
        medium = 40.dp
        large = 60.dp
    }
    var itemSize: Dp
    var total = 0
    //Grouping Solutions
    val SECTION_1 = "section1"
    val SECTION_2 = "section2"
    val SECTION_3 = "section3"
    val groupedSolutions = solutions.groupBy { solution ->
        when (solution.letters.size) {
            in Int.MIN_VALUE..3 -> SECTION_1
            in 4..4 -> SECTION_2
            else -> SECTION_3
        }
    }
    var section1 = groupedSolutions.getOrElse(SECTION_1) { null }
    var section2 = groupedSolutions.getOrElse(SECTION_2) { null }
    val section3 = groupedSolutions.getOrElse(SECTION_3) { null }
    if ((section2 != null) || (section3 != null)) {
        itemSize = small
    }
    if (section1 != null && section2 != null && (section1.size + section2.size) <= 4) {
        section1 = section1 + section2
        section2 = null
    }
    section1?.let {
        total += it.size
    }
    section2?.let {
        total += it.size
    }
    section3?.let {
        total += it.size
    }

    itemSize = if (total < 4 && section3 == null) {
        large
    } else if (total < 5 || section3 != null) {
        medium
    } else {
        small
    }

    Box(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp),
    ) {

        Column {
            Row {
                val colModifier = Modifier.padding(start = 4.dp, end = 4.dp)
                section1?.let { sec ->
                    LazyColumn(
                        modifier = colModifier,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        items(sec) { sol ->
                            GameSolutionItem(
                                solution = sol,
                                itemsize = itemSize,
                                solutions = solutions,
                                onTileClicked = onTileClicked
                                /*{ solutionIndex, tileIndex ->
                                    Log.d(TAG, "SolutionPad: $solutionIndex : $tileIndex")
                                }*/
                            )
                        }
                    }
                }
                section2?.let { sec ->
                    LazyColumn(
                        modifier = colModifier,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        items(sec) { sol ->
                            GameSolutionItem(
                                solution = sol,
                                itemsize = itemSize,
                                solutions = solutions,
                                onTileClicked = onTileClicked
                            )
                        }
                    }
                }

            }

            section3?.let { sec ->
                LazyColumn {
                    items(sec) { sol ->
                        GameSolutionItem(
                            solution = sol,
                            itemsize = itemSize,
                            solutions = solutions,
                            onTileClicked = onTileClicked
                        )
                    }
                }
            }
        }
    }
}