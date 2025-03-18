package com.wordgame.wordpuzzles.ui.screens.gameview

import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wordgame.wordpuzzles.data.chapter.Chapter
import com.wordgame.wordpuzzles.data.chapter.ChapterRepo
import com.wordgame.wordpuzzles.utils.GEMS_TO_CONSUME
import com.wordgame.wordpuzzles.utils.GemShopManager
import com.wordgame.wordpuzzles.data.level.LevelRepo
import com.wordgame.wordpuzzles.data.level.LevelWithSolutions
import com.wordgame.wordpuzzles.data.solution.SolutionRepo
import com.wordgame.wordpuzzles.models.GameLetter
import com.wordgame.wordpuzzles.models.GameSolution
import com.wordgame.wordpuzzles.models.GameSolutionWithLetter
import com.wordgame.wordpuzzles.models.KeyPadButton
import com.wordgame.wordpuzzles.utils.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameScreenViewModel @Inject constructor(
    private val levelRepo: LevelRepo,
    private val chapterRepo: ChapterRepo,
    private val solutionRepo: SolutionRepo
) : ViewModel() {
    var mChapter: Chapter? = null
    var mListletters = mutableStateListOf<KeyPadButton>()
    var mListSolutions = mutableStateListOf<GameSolution>()
    private val mListSolutionsSelected = mutableStateListOf<GameSolution>()
    private var mLevelWithSolutions: LevelWithSolutions? = null
    var mLevel = mutableStateOf("")
    var cLevel = mutableIntStateOf(0)
    private var mCompletedLevels = mutableIntStateOf(0)
    var dataPrepared = mutableStateOf(false)
    private var mGameCompleted = mutableStateOf(false)

    //wordtopreview
    val wordtopreview = mutableStateOf("")

    fun updateWordToPreview(word: String) {
        wordtopreview.value = word
    }

    fun getCurrentLevel() = viewModelScope.launch {
        cLevel.value = levelRepo.getCompletedLevelCount()
    }

    fun prepareLevel() = viewModelScope.launch(Dispatchers.IO) {
        dataPrepared.value = false
        mChapter = null
        mListletters.clear()
        mListSolutions.clear()
        mListSolutionsSelected.clear()
        //Get Completed levels
        mCompletedLevels.value = levelRepo.getCompletedLevelCount() + 1
        //getting data from database
        // val chapters = chapterRepo.getAllChapters()
        //  Log.d("GameScreenViewModel", "All Chapters: $chapters")

        val job = viewModelScope.launch {
            //startagain
            mChapter = chapterRepo.getChapter(false)
            if (mChapter == null) {
                mGameCompleted.value = true
                return@launch
            }
            mChapter?.let { chapter ->
                val levelWithSolutions = levelRepo.getLevelWithSolutions(chapter.id, false)
                //     Log.e(TAG, "Level With Solutions: ${levelWithSolutions}")
                if (levelWithSolutions == null) {
                    chapterRepo.update(mChapter!!.copy(chapterCompleted = true))
                    return@launch
                } else {
                    mLevelWithSolutions = levelWithSolutions
                }
            }

        }
        job.join()
        mLevelWithSolutions?.let { levelWithSolutions ->
            val dblevel = levelWithSolutions.level
            mLevel.value = mCompletedLevels.value.toString()
            val answers = levelWithSolutions.solutions
            val solutions = mutableListOf<String>()
            answers.forEach { item ->
                solutions.add(item.solutionWord)
            }
            val letters = dblevel.levelLetters

            //Preparing Keypad Selection
            val listletters = mutableListOf<KeyPadButton>()
            for (l in letters) {
                listletters.add(
                    KeyPadButton(label = l.toString())
                )
            }
            mListletters = listletters.toMutableStateList()

            //Preparing Solution View
            val list = mutableListOf<GameSolution>()
            for (word in solutions) {
                val gameletters = mutableListOf<GameLetter>()
                for (letter in word) {
                    gameletters.add(
                        GameLetter(label = letter.toString())
                    )
                }
                val labelsAsString = gameletters.toMutableStateList().joinToString(separator = "") { letter ->
                    letter.label
                }
                list.add(
                    GameSolution(letters = gameletters.toMutableStateList()
                        ,details = solutionRepo.details(labelsAsString))
                )
            }

            val sorted = list.sortedBy { it.letters.size }
            mListSolutions = sorted.toMutableStateList()
            delay(1000)
            dataPrepared.value = true
        }
//        val solutionList = solutionRepo.list(mLevel.value)
//        Log.d(TAG, "prepareLevel: ${solutionList.toList()}")
    }

    //This will set the letter to visible
    //And return the letter's solution
    fun showLetter(): GameSolutionWithLetter? {
        mListSolutions.forEachIndexed { sindex, sitem ->
            sitem.letters.forEachIndexed { lindex, litem ->
                if (!litem.isvisible) {
                    mListSolutions[sindex].letters[lindex] = litem.copy(isvisible = true)
                    return GameSolutionWithLetter(sitem, litem)
                }
            }
        }
        return null
    }

    fun showIndexLetter(solutionIndex: Int, letterIndex: Int): GameSolutionWithLetter? {
        Log.d(TAG, "show: Solution $solutionIndex, Letter $letterIndex")
        val solution = mListSolutions.getOrNull(solutionIndex)
//        printListContents(mListSolutions)

        solution?.letters?.getOrNull(letterIndex)?.let { litem ->
            if (!litem.isvisible) {
                mListSolutions[solutionIndex].letters[letterIndex] = litem.copy(isvisible = true)
                return GameSolutionWithLetter(solution, litem)
            }
        }
        return null
    }

//    fun printListContents(list: List<GameSolution>) {
//        for ((index, solution) in list.withIndex()) {
//            Log.d(TAG, "Item at index $index: $solution")
//        }
//    }

    fun showWord(): GameSolutionWithLetter? {
        mListSolutions.forEachIndexed { sindex, sitem ->
            var allLettersRevealed = true
            for (index in 0 until sitem.letters.size) {
                val litem = sitem.letters[index]
                if (!litem.isvisible) {
                    mListSolutions[sindex].letters[index] = litem.copy(isvisible = true)
                    allLettersRevealed = false
                }
            }
            if (!allLettersRevealed) {
                return GameSolutionWithLetter(sitem, sitem.letters.last())
            }
        }
        return null
    }


    fun isAllLettersShowed(solution: GameSolution): Boolean {
        solution.letters.forEach { item ->
            if (!item.isvisible) {
                return false
            }
        }
        return true
    }

    //This will set the solution complete and return if
    //All solutions are completed
    fun setSolutionComplete(solution: GameSolution): Boolean {
        val index = mListSolutions.indexOf(solution)
        val letters = solution.letters.toMutableList() // Create a copy of the letters list
        letters.forEachIndexed { i, l ->
            letters[i] = l.copy(isvisible = true)
        }
        mListSolutions[index] =
            solution.copy(letters = letters.toMutableStateList(), iscompleted = true)
        mListSolutionsSelected.add(mListSolutions[index])
        return isLevelCompleted()
    }

    fun isSolutionExists(list: List<KeyPadButton>): GameSolution? {
        var word = ""
        list.forEach { kpb ->
            word += kpb.label.uppercase()
        }
        mListSolutions.forEach { solution ->
            if (solution.isEqual(word)) {
                return solution
            }
        }
        return null
    }

    fun isSolved(list: List<KeyPadButton>): GameSolution? {
        var word = ""
        list.forEach { kpb ->
            word += kpb.label.uppercase()
        }
        mListSolutionsSelected.forEach { solution ->
            if (solution.isEqual(word)) {

                return solution
            }
        }
        return null
    }

    private fun isLevelCompleted(): Boolean {
        mListSolutions.forEach { solution ->
            if (!solution.iscompleted) {
                return false
            }
        }
        mLevelWithSolutions?.let { level ->
            viewModelScope.launch(Dispatchers.IO) {
                levelRepo.update(level.level.copy(levelCompleted = true))
                //checking if chapter is completed
                mChapter?.let { chapter ->
                    val levelWithSolutions =
                        levelRepo.getLevelWithSolutions(chapter.id, false)
                    if (levelWithSolutions == null) {
                        chapterRepo.update(chapter.copy(chapterCompleted = true))
                    }
                }
            }
        }
        return true
    }

    fun consumeBoosts(boosts: Int): Boolean {
        val boostsTotal = GemShopManager.getTotalBoosts()
        if (boostsTotal >= GEMS_TO_CONSUME) {
            GemShopManager.consumeBoosts(boosts)
            return true
        }
        return false
    }

    fun consumeShowClicks(showClicks: Int): Boolean {
        val clicksTotal = GemShopManager.getTotalShowClicks()
        if (clicksTotal >= GEMS_TO_CONSUME) {
            GemShopManager.consumeShowClicks(showClicks)
            return true
        }
        return false
    }

    fun consumeHints(hints: Int): Boolean {
        val hintsTotal = GemShopManager.getTotalHints()
        if (hintsTotal >= GEMS_TO_CONSUME) {
            GemShopManager.consumeHints(hints)
            return true
        }
        return false
    }

    //Gem
    fun consumeGems(gems: Int): Boolean {
        val gemsTotal = GemShopManager.getGemsTotal()
        if (gemsTotal >= gems) {
            GemShopManager.consumeGems(gems)
            return true
        }
        return false
    }

    fun incrementGems(gems: Int): Boolean {
        GemShopManager.addGems(gems)
        return true
    }

    fun incrementHints(hints: Int): Boolean {
        GemShopManager.addHints(hints)
        return true
    }

    fun incrementBoosts(boosts: Int): Boolean {
        GemShopManager.addBoosts(boosts)
        return true
    }
}