package com.wordgame.wordpuzzles.presentation.screens.gameview

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wordgame.wordpuzzles.core.utils.GEMS_TO_CONSUME
import com.wordgame.wordpuzzles.core.utils.GemShopManager
import com.wordgame.wordpuzzles.core.utils.SettingOptionsManager
import com.wordgame.wordpuzzles.data.local.entities.Chapter
import com.wordgame.wordpuzzles.data.repository.ChapterRepo
import com.wordgame.wordpuzzles.data.repository.LevelRepo
import com.wordgame.wordpuzzles.data.repository.SolutionRepo
import com.wordgame.wordpuzzles.domain.models.GameLetter
import com.wordgame.wordpuzzles.domain.models.GameSolution
import com.wordgame.wordpuzzles.domain.models.GameSolutionWithLetter
import com.wordgame.wordpuzzles.domain.models.KeyPadButton
import com.wordgame.wordpuzzles.domain.models.LevelWithSolutions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameScreenViewModel @Inject constructor(
    private val levelRepo: LevelRepo,
    private val chapterRepo: ChapterRepo,
    private val solutionRepo: SolutionRepo,
    settingOptionsManager: SettingOptionsManager
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

    private val _isSoundEnabled = mutableStateOf(settingOptionsManager.isSoundEnabled())
    val isSoundEnabled: State<Boolean> = _isSoundEnabled

    //wordtopreview
    val wordtopreview = mutableStateOf("")

    fun updateWordToPreview(word: String) {
        wordtopreview.value = word
    }

    fun getCurrentLevel() = viewModelScope.launch {
        cLevel.intValue = levelRepo.getCompletedLevelCount()
    }

    fun prepareLevel() = viewModelScope.launch(Dispatchers.IO) {
        dataPrepared.value = false
        mChapter = null
        mListletters.clear()
        mListSolutions.clear()
        mListSolutionsSelected.clear()
        mCompletedLevels.intValue = levelRepo.getCompletedLevelCount() + 1

        val job = viewModelScope.launch {
            mChapter = chapterRepo.getChapter(false)
            if (mChapter == null) {
                mGameCompleted.value = true
                return@launch
            }
            mChapter?.let { chapter ->
                val levelWithSolutions = levelRepo.getLevelWithSolutions(chapter.id, false)
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
            mLevel.value = mCompletedLevels.intValue.toString()
            val answers = levelWithSolutions.solutions
            val solutions = mutableListOf<String>()
            answers.forEach { item ->
                solutions.add(item.solutionWord)
            }
            val letters = dblevel.levelLetters

            val listLetters = mutableListOf<KeyPadButton>()
            for (l in letters) {
                listLetters.add(
                    KeyPadButton(label = l.toString())
                )
            }
            mListletters = listLetters.toMutableStateList()

            //Preparing Solution View
            val list = mutableListOf<GameSolution>()
            for (word in solutions) {
                val gameletters = mutableListOf<GameLetter>()
                for (letter in word) {
                    gameletters.add(
                        GameLetter(label = letter.toString())
                    )
                }
                val labelsAsString =
                    gameletters.toMutableStateList().joinToString(separator = "") { letter ->
                        letter.label
                    }
                list.add(
                    GameSolution(
                        letters = gameletters.toMutableStateList(),
                        details = solutionRepo.details(labelsAsString)
                    )
                )
            }

            val sorted = list.sortedBy { it.letters.size }
            mListSolutions = sorted.toMutableStateList()
            delay(1000)
            dataPrepared.value = true
        }
    }

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
        val solution = mListSolutions.getOrNull(solutionIndex)

        solution?.letters?.getOrNull(letterIndex)?.let { litem ->
            if (!litem.isvisible) {
                mListSolutions[solutionIndex].letters[letterIndex] = litem.copy(isvisible = true)
                return GameSolutionWithLetter(solution, litem)
            }
        }
        return null
    }

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

    fun setSolutionComplete(solution: GameSolution): Boolean {
        val index = mListSolutions.indexOf(solution)
        val letters = solution.letters.toMutableList()
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