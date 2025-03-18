package com.wordgame.wordpuzzles.ui.screens.gameview

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.gson.Gson
import com.wordgame.wordpuzzles.utils.GemShopManager
import com.wordgame.wordpuzzles.R
import com.wordgame.wordpuzzles.components.AnimatedOverlay
import com.wordgame.wordpuzzles.components.BottomBar
import com.wordgame.wordpuzzles.components.ButtonWatchAd
import com.wordgame.wordpuzzles.components.BuyGemsDialog
import com.wordgame.wordpuzzles.components.DialogLevelComplete
import com.wordgame.wordpuzzles.components.GoodJob
import com.wordgame.wordpuzzles.components.KeyPad
import com.wordgame.wordpuzzles.ads.LoadRewardedInterstitial
import com.wordgame.wordpuzzles.components.CongratulationDialog
import com.wordgame.wordpuzzles.components.MiddleBar
import com.wordgame.wordpuzzles.components.PreviewWord
import com.wordgame.wordpuzzles.components.QuitConfirmationDialog
import com.wordgame.wordpuzzles.components.ShowLetterPopup
import com.wordgame.wordpuzzles.components.ShowLetterSnackBar
import com.wordgame.wordpuzzles.components.SolutionPad
import com.wordgame.wordpuzzles.components.TopBar
import com.wordgame.wordpuzzles.data.WordDetails
import com.wordgame.wordpuzzles.navigation.DestinationMain
import com.wordgame.wordpuzzles.utils.SettingOptionsManager
import com.wordgame.wordpuzzles.utils.ShowLetterState
import com.wordgame.wordpuzzles.utils.SoundPlayer
import com.wordgame.wordpuzzles.utils.TAG
import com.wordgame.wordpuzzles.utils.UserDataManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun GameScreen(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val viewModel: GameScreenViewModel = hiltViewModel()
    val mListLetters = viewModel.mListletters
    var clickedTileIndex by remember { mutableIntStateOf(-1) }
    var clickedSolutionIndex by remember { mutableIntStateOf(-1) }
    var isSoundEnabled by remember {
        mutableStateOf(true)
    }
    var showCongratulationDialog by remember {
        mutableStateOf(false)
    }
    var loadRewardedInterstitial by remember {
        mutableStateOf(false)
    }
    //  var showLetterOnClick by remember { mutableStateOf(false) }
    var shuffle by remember {
        mutableStateOf(false)
    }
    val mLevel by viewModel.mLevel
    val dataprepared by viewModel.dataPrepared

    var levelCompleted by remember {
        mutableStateOf(false)
    }
    var solutionCompleted by remember {
        mutableStateOf(false)
    }
    var mGems by remember {
        mutableIntStateOf(0)
    }

    var mHints by remember {
        mutableIntStateOf(0)
    }
    var mBoosts by remember {
        mutableIntStateOf(0)
    }
    var mShowTile by remember {
        mutableIntStateOf(0)
    }
    var offsetShowLetterTarget by remember {
        mutableStateOf(Offset.Zero)
    }
    var offsetHint by remember {
        mutableStateOf(Offset.Zero)
    }
    var offsetBoost by remember {
        mutableStateOf(Offset.Zero)
    }
    var offsetShow by remember {
        mutableStateOf(Offset.Zero)
    }
    var showPopupLetter by remember {
        mutableStateOf(false)
    }
    var showGemsMessage by remember {
        mutableStateOf(false)
    }
    var showPopupWord by remember {
        mutableStateOf(false)
    }
    var showPopupIndex by remember {
        mutableStateOf(false)
    }
    var showHand by remember {
        mutableStateOf(false)
    }
    var showDialog by remember {
        mutableStateOf(false)
    }
    var exited by remember {
        mutableStateOf(false)
    }
    var showBuyGemsDialog by remember {
        mutableStateOf(false)
    }
    var previousChapterId by remember {
        mutableStateOf<Int?>(null)
    }
    LaunchedEffect(Unit) {
        mGems = GemShopManager.getGemsTotal()
        mHints = GemShopManager.getTotalHints()
        mBoosts = GemShopManager.getTotalBoosts()
        mShowTile = GemShopManager.getTotalShowClicks()
    }
    LaunchedEffect(Unit) {
        viewModel.prepareLevel()
    }
    isSoundEnabled = SettingOptionsManager.isSoundEnabled()

    val onTileClicked: (solutionIndex: Int, letterIndex: Int) -> Unit =
        { solutionIndex, letterIndex ->
            if (ShowLetterState.showLetterOnClick.value) {
                scope.launch(Dispatchers.IO) {
                    val showTileConsumed = viewModel.consumeShowClicks(1)
                    if (!showTileConsumed) {
                        val gemsConsumed = viewModel.consumeGems(2)
                        if (!gemsConsumed) {
                            showHand = true
                            if (isSoundEnabled) {
                                SoundPlayer.playSound(context, R.raw.not_allowed)
                            }
                            ShowLetterState.showLetterOnClick.value = false
                            return@launch
                        } else {
                            showGemsMessage = true
                            mGems = GemShopManager.getGemsTotal()
                        }
                    } else {
                        mShowTile = GemShopManager.getTotalShowClicks()
                    }
                    if (isSoundEnabled) {
                        SoundPlayer.playSound(context, R.raw.show_letter)
                    }

                    clickedTileIndex = letterIndex
                    clickedSolutionIndex = solutionIndex

                    val solutionWithLetter =
                        viewModel.showIndexLetter(clickedSolutionIndex, clickedTileIndex)
                    if (solutionWithLetter != null) {
                        val solution = solutionWithLetter.solution
                        val letterToShow = solutionWithLetter.letter

                        // Update the state to show the letter
                        offsetShowLetterTarget = letterToShow.offset
                        showPopupIndex = true

                        val allLettersShowed = viewModel.isAllLettersShowed(solution)
                        if (allLettersShowed) {
                            val allLevelsCompleted = viewModel.setSolutionComplete(solution)
                            if (allLevelsCompleted) { // level is completed
                                levelCompleted = true
                            } else { // only solution is completed
                                solutionCompleted = true
                            }
                        }
                    }
                    ShowLetterState.showLetterOnClick.value = false
                }
            }
        }

    if (showBuyGemsDialog) {
        BuyGemsDialog(onGemsEarned = {
            mGems = GemShopManager.getGemsTotal()
        })
        { showBuyGemsDialog = false }
    }
    val backDispatcherOwner = LocalOnBackPressedDispatcherOwner.current
    val backCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showDialog = true
            }
        }
    }
    DisposableEffect(Unit) {
        backDispatcherOwner?.onBackPressedDispatcher?.addCallback(backCallback)
        onDispose {
            backCallback.remove()
        }
    }
    BackHandler(enabled = showDialog) {
        showDialog = false
    }

    if (showDialog) {
        QuitConfirmationDialog(onExitGame = {
            exited = true
            showDialog = false
            navController.navigate(DestinationMain.route)
        }, onCancel = {
            showDialog = false
        })
    }
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.main_bg),
            contentDescription = "Background",
            contentScale = ContentScale.FillBounds
        )
        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            //Upper
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (dataprepared) {
                        val levelSolutionCompleted = createLevelSolutionCompleted(viewModel)
                        Box(
                            modifier = Modifier.weight(0.2f)
                        ) {
                            TopBar(mLevel, mGems, null, onSpinClick = {}, {
                                showBuyGemsDialog = true
                            }, onCompletedWordsClick = {}, levelSolutionCompleted)
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            modifier = Modifier.fillMaxSize(),
                            painter = painterResource(id = R.drawable.gameview_solutionpad),
                            contentDescription = "Solution Pad",
                            contentScale = ContentScale.FillBounds
                        )
                        //Solution pad goes here
                        if (dataprepared) {
                            val solutionsList = viewModel.mListSolutions
                            SolutionPad(
                                solutionsList,
                                onTileClicked = onTileClicked
                            )
                        }
                    }
                }
            }

            //Lower
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
            ) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(id = R.drawable.fabric),
                    contentDescription = "Fabric",
                    contentScale = ContentScale.FillBounds
                )

                Column(
                    modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(0.2f)
                    ) {
                        MiddleBar(
                            mBoosts, mShowTile, onBoostClicked = {
                                offsetBoost = it
                                scope.launch(Dispatchers.IO) {
                                    val boostsConsumed = viewModel.consumeBoosts(1)
                                    if (!boostsConsumed) {
                                        val gemsConsumed = viewModel.consumeGems(3)
                                        if (!gemsConsumed) {
                                            showHand = true
                                            if (isSoundEnabled) {
                                                SoundPlayer.playSound(context, R.raw.not_allowed)
                                            }
                                            return@launch
                                        } else {
                                            showGemsMessage = true
                                            mGems = GemShopManager.getGemsTotal()
                                        }
                                    } else {
                                        mBoosts = GemShopManager.getTotalBoosts()
                                    }
                                    if (isSoundEnabled) {
                                        SoundPlayer.playSound(context, R.raw.show_letter)
                                    }
                                    val deffered = scope.async {
                                        val solutionWithLetter = viewModel.showWord()
                                        solutionWithLetter
                                    }
                                    val dsolutionWithLetter = deffered.await()
                                    if (dsolutionWithLetter != null) {
                                        val solution = dsolutionWithLetter.solution
                                        val lastLetter = dsolutionWithLetter.letter

                                        showPopupWord = true
                                        offsetShowLetterTarget = lastLetter.offset

                                        val allLettersShowed =
                                            viewModel.isAllLettersShowed(solution)
                                        if (allLettersShowed) {
                                            val alllevelscompleted =
                                                viewModel.setSolutionComplete(solution)
                                            if (alllevelscompleted) { // level is completed
                                                levelCompleted = true
                                            } else { //only solution is completed
                                                solutionCompleted = true
                                            }
                                        }
                                    }
                                }
                            }, onShowClicked = {
                                offsetShow = it
                                ShowLetterState.showLetterOnClick.value =
                                    !ShowLetterState.showLetterOnClick.value
                            }
                        )
                    }
                    //Selection pad
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                    ) {
                        if (dataprepared) {
                            KeyPad(shuffle = shuffle, list = mListLetters, onSetShuffle = {
                                shuffle = false
                            }, onSelected = { list ->
                                val listLocal = list.toList()
                                var word = ""
                                listLocal.forEach { btn ->
                                    word += btn.label
                                }
                                viewModel.updateWordToPreview(word)
                            }, onCompleted = { list ->
                                val listLocal = list.toList()
                                scope.launch {

                                    //check if already solved
                                    val differedIsSolved = scope.async {
                                        val solution = viewModel.isSolved(listLocal)
                                        solution
                                    }
                                    val solved = differedIsSolved.await()
                                    if (solved != null) {
                                        val index = viewModel.mListSolutions.indexOfFirst {
                                            it.id == solved.id
                                        }
                                        if (index != -1) {
                                            val animate = viewModel.mListSolutions[index].animate
                                            viewModel.mListSolutions[index] =
                                                viewModel.mListSolutions[index].copy(animate = !animate)
                                            if (isSoundEnabled) {
                                                SoundPlayer.playSound(context, R.raw.not_allowed)
                                            }
                                            viewModel.wordtopreview.value = ""
                                        }
                                        return@launch
                                    }//

                                    //if not already solved then check if solution exists for the
                                    //selected word
                                    val differedIsExists = scope.async {
                                        val solution = viewModel.isSolutionExists(listLocal)
                                        solution
                                    }
                                    val solution = differedIsExists.await()
                                    if (solution == null) {
                                        if (isSoundEnabled) {
                                            SoundPlayer.playSound(context, R.raw.not_allowed)
                                        }
                                        viewModel.wordtopreview.value = ""
                                        return@launch
                                    }

                                    //If solution exists then set it as complete
                                    val alllevelscompleted = viewModel.setSolutionComplete(solution)
                                    if (alllevelscompleted) { // level is completed
                                        levelCompleted = true
                                    } else { //only solution is completed
                                        solutionCompleted = true
                                    }
                                    viewModel.wordtopreview.value = ""
                                }
                            })
                        }
                    }

                    //Bottom
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(0.2f)
                    ) {
                        BottomBar(mHints, onHintClicked = { offset ->
                            offsetHint = offset
                            scope.launch(Dispatchers.IO) {
                                val hintsConsumed = viewModel.consumeHints(1)
                                if (!hintsConsumed) {
                                    val gemsConsumed = viewModel.consumeGems(1)
                                    if (!gemsConsumed) {
                                        showHand = true
                                        if (isSoundEnabled) {
                                            SoundPlayer.playSound(context, R.raw.not_allowed)
                                        }
                                        return@launch
                                    } else {
                                        showGemsMessage = true
                                        mGems = GemShopManager.getGemsTotal()
                                    }
                                } else {
                                    mHints = GemShopManager.getTotalHints()
                                }
                                if (isSoundEnabled) {
                                    SoundPlayer.playSound(context, R.raw.show_letter)
                                }
                                val deffered = scope.async {
                                    val solution = viewModel.showLetter()
                                    solution
                                }

                                val dsolution = deffered.await()

                                if (dsolution != null) {
                                    val solution = dsolution.solution
                                    val letter = dsolution.letter
                                    //showing start popup over the letter
                                    showPopupLetter = true
                                    offsetShowLetterTarget = letter.offset
                                    val allLettersShowed = viewModel.isAllLettersShowed(solution)
                                    if (allLettersShowed) {
                                        val alllevelscompleted =
                                            viewModel.setSolutionComplete(solution)
                                        if (alllevelscompleted) { // level is completed
                                            levelCompleted = true
                                        } else { //only solution is completed
                                            solutionCompleted = true
                                        }
                                    }
                                }
                                mGems = GemShopManager.getGemsTotal()
                            }
                        }, onShuffleClicked = {
                            scope.launch {
                                if (isSoundEnabled) {
                                    SoundPlayer.playSound(context, R.raw.shuffle)
                                }
                            }
                            shuffle = true
                            mListLetters.shuffle()
                        })
                    }
                }
            }
        }

        //Screen Overlay
        if (!viewModel.dataPrepared.value) {
            LaunchedEffect(Unit) {
                scope.launch {
                    if (isSoundEnabled) {
                        SoundPlayer.playSound(context, R.raw.overlay_enter)
                    }
                }
            }
        } else {
            LaunchedEffect(Unit) {
                scope.launch {
                    if (isSoundEnabled) {
                        SoundPlayer.playSound(context, R.raw.overlay_exit)
                    }
                }
            }
        }
        //Preview
        PreviewWord(
            modifier = Modifier.align(Alignment.Center),
            viewmodel = viewModel,
        )
        if (mGems < 3) {
            //Ad
            val adbuttonmodifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(top = 16.dp)
            ButtonWatchAd(modifier = adbuttonmodifier, showHand = showHand, onGemsEarned = {
                mGems = GemShopManager.getGemsTotal()
            })
        }
        LaunchedEffect(showHand) {
            if (showHand) {
                delay(3000)
                showHand = false
            }
        }

        //letter start
        if (showPopupLetter) {
            LaunchedEffect(Unit) {
                scope.launch {
                    delay(1000)
                    showPopupLetter = false
                }
            }

            ShowLetterPopup(
                hintOffset = offsetHint, letterOffset = offsetShowLetterTarget
            )
        }
        if (showPopupWord) {
            LaunchedEffect(Unit) {
                scope.launch {
                    delay(1000)
                    showPopupWord = false
                }
            }

            ShowLetterPopup(
                hintOffset = offsetBoost, letterOffset = offsetShowLetterTarget
            )
        }
        if (showPopupIndex) {
            LaunchedEffect(Unit) {
                scope.launch {
                    delay(1000)
                    showPopupIndex = false
                }
            }

            ShowLetterPopup(
                hintOffset = offsetShow, letterOffset = offsetShowLetterTarget
            )
        }
        AnimatedOverlay(animate = !viewModel.dataPrepared.value)


        //level completed
        if (levelCompleted) {
            LaunchedEffect(Unit) {
                scope.launch {
                    if (isSoundEnabled) {
                        SoundPlayer.playSound(context, R.raw.level_completed)
                    }
                }
            }
            DialogLevelComplete(onOkClicked = {
                loadRewardedInterstitial = true
                levelCompleted = false
                viewModel.prepareLevel()
                viewModel.incrementGems(1)
                mGems = GemShopManager.getGemsTotal()
            }, gemsText = " + " + 1)
        }

        //Solution completed
        if (solutionCompleted) {
            val modifier = Modifier.align(Alignment.Center)
            GoodJob(modifier)
            LaunchedEffect(Unit) {
                scope.launch(Dispatchers.IO) {
                    if (isSoundEnabled) {
                        SoundPlayer.playSound(context, R.raw.good_job)
                    }
                    delay(1000)
                    solutionCompleted = false
                }
            }
        }
        if (showCongratulationDialog) {
            SoundPlayer.playSound(context, R.raw.congrats)
            CongratulationDialog(
                onDismiss = {
                    viewModel.incrementGems(1)
                    showCongratulationDialog = false
                },
                painterResource(id = R.drawable.gem_default),
                "Congratulations!",
                "+1 Gems"
            )
        }
        if (loadRewardedInterstitial) {
            val currentChapterId = viewModel.mChapter?.id
            if (currentChapterId != previousChapterId) {
                LoadRewardedInterstitial(onAdClosed = {},
                    onUserEarnedReward = {
                        showCongratulationDialog = true
                        mGems = GemShopManager.getGemsTotal()
                        loadRewardedInterstitial = false
                    })
                previousChapterId = currentChapterId
            }
        }
    }
    if (ShowLetterState.showLetterOnClick.value) {
        ShowLetterSnackBar(
            showLetterOnClick = ShowLetterState.showLetterOnClick.value,
            onDismiss = { },
            "Click on a tile to reveal a letter!"
        )
    }
    if (showGemsMessage) {
        ShowLetterSnackBar(
            showLetterOnClick = true,
            onDismiss = { },
            "Using Gems for the Powerup!"
        )
        Handler(Looper.getMainLooper()).postDelayed({
            showGemsMessage = false
        }, 3000)
    }
}

@Composable
fun createLevelSolutionCompleted(viewModel: GameScreenViewModel): List<WordDetails> {
    val completedWords = viewModel.mListSolutions.filter { solution -> solution.iscompleted }.map { solution ->
        val word = solution.letters.joinToString("") { letter ->
            letter.label
        }
        val details = solution.details
        Log.d(TAG, "GameScreen: ${WordDetails(word, details)}")
        WordDetails(word, details)
    }
    UserDataManager.setCompletedWords(completedWords.map { it.word })
    return completedWords
}