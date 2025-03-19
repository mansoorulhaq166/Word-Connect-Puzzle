package com.wordgame.wordpuzzles.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.wordgame.wordpuzzles.R
import com.wordgame.wordpuzzles.ads.LoadBannerAds
import com.wordgame.wordpuzzles.components.AnimatedWordConnectBackground
import com.wordgame.wordpuzzles.components.ButtonPlayMain
import com.wordgame.wordpuzzles.components.BuyGemsDialog
import com.wordgame.wordpuzzles.components.CongratulationDialog
import com.wordgame.wordpuzzles.components.TopBar
import com.wordgame.wordpuzzles.navigation.DestinationGame
import com.wordgame.wordpuzzles.ui.screens.gameview.GameScreenViewModel
import com.wordgame.wordpuzzles.utils.GemShopManager
import com.wordgame.wordpuzzles.utils.SoundPlayer
import com.wordgame.wordpuzzles.utils.UserDataManager
import java.util.Timer
import kotlin.concurrent.scheduleAtFixedRate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController) {
    val mContext = LocalContext.current
    var showBuyGemsDialog by remember { mutableStateOf(false) }
//    var showSpinWheel by remember { mutableStateOf(false) }
    var showCompletedWords by remember { mutableStateOf(false) }
    var useGemsToSpin by remember { mutableStateOf(false) }
    var showCongratulationDialog by remember {
        mutableStateOf(false)
    }
    val selectedGiftName by remember { mutableStateOf("") }
    val selectedGiftText by remember { mutableStateOf("") }
    val selectedGiftIcon by remember { mutableIntStateOf(R.drawable.gem_default) }
    var remainingTime by remember {
        mutableLongStateOf(GemShopManager.getRemainingTime())
    }
    var mGems by remember {
        mutableIntStateOf(0)
    }
    mGems = GemShopManager.getGemsTotal()
    val viewModel: GameScreenViewModel = hiltViewModel()
    LaunchedEffect(Unit) {
        viewModel.getCurrentLevel()
    }
    val mLevel = viewModel.cLevel.intValue
    LaunchedEffect(Unit) {
        val updateInterval = 1000L // 1 second
        val timer = Timer()
        timer.scheduleAtFixedRate(0, updateInterval) {
            remainingTime = GemShopManager.getRemainingTime()
        }
    }

    // Use a CoroutineScope to control animations
    val completedWords = UserDataManager.getCompletedWords()
    val cLevel = stringResource(
        id = R.string.level, mLevel + 1
    )

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        AnimatedWordConnectBackground()

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Bar
            TopBar(null, mGems, navController, onSpinClick = {
//                showSpinWheel = !showSpinWheel
            }, {
                showBuyGemsDialog = true
            }, onCompletedWordsClick = {
                showCompletedWords = !showCompletedWords
            }, null)


            // Play Button
            Box(
                modifier = Modifier.fillMaxWidth()
                    .weight(1f).padding(bottom = 96.dp),
                contentAlignment = Alignment.Center,
            ) {
                ButtonPlayMain(
                    text = "Play $cLevel"
                ) {
                    navController.navigate(DestinationGame.route)
                }
            }

            LoadBannerAds()
        }
    }
    if (showCompletedWords) {
        BasicAlertDialog(
            onDismissRequest = { showCompletedWords = false }, properties = DialogProperties(
                dismissOnClickOutside = true, dismissOnBackPress = true
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .border(1.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
                    .background(Color.Black)
                    .then(
                        Modifier.shadow(
                            elevation = 4.dp,
                            shape = RoundedCornerShape(8.dp)
                        )
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ) {
                    // Content above the milestone section
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Completed Words: ${completedWords.size}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color.White,
                            fontFamily = FontFamily.Default,
                            textAlign = TextAlign.Start,
                            modifier = Modifier
                                .padding(8.dp)
                                .background(
                                    color = Color.Gray,
                                    shape = RoundedCornerShape(8.dp)
                                )
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.Start
                        ) {
                            val wordsPerRow = 3 // Adjust the number of words per row as needed
                            val numberOfRows = (completedWords.size + wordsPerRow - 1) / wordsPerRow

                            items(numberOfRows) { rowIndex ->
                                val startIndex = rowIndex * wordsPerRow
                                val endIndex = minOf(startIndex + wordsPerRow, completedWords.size)
                                val rowWords = completedWords.subList(startIndex, endIndex)

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    rowWords.forEach { word ->
                                        Card(
                                            modifier = Modifier
                                                .padding(8.dp)
                                                .border(
                                                    1.dp,
                                                    Color.Gray,
                                                    shape = RoundedCornerShape(16.dp)
                                                )
                                        ) {
                                            Text(
                                                text = word,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 18.sp,
                                                color = Color.White,
                                                fontFamily = FontFamily.Default,
                                                textAlign = TextAlign.Start,
                                                modifier = Modifier.padding(8.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Milestone section with reward button
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Next Reward on 120 Words",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color.White,
                            fontFamily = FontFamily.Default,
                            textAlign = TextAlign.Start,
                            modifier = Modifier
                                //      .clickable { if (milestoneReached) onClaimRewardClick() }
                                .padding(8.dp)
                                .background(
                                    color = Color.Gray,
                                    shape = RoundedCornerShape(8.dp)
                                )
                        )
                    }
                }
            }
        }
    }
/*    if (showSpinWheel) {
        AlertDialog(
            onDismissRequest = { showSpinWheel = false }, properties = DialogProperties(
                dismissOnClickOutside = true, dismissOnBackPress = true
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Displaying the remaining time if user cannot spin
                    if (!GemShopManager.canSpinWheel() && !useGemsToSpin) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .background(
                                    color = Color(0xFF2E3B4E),
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            IconButton(
                                onClick = { showSpinWheel = false },
                                modifier = Modifier
                                    .align(Alignment.End)
                                    .padding(start = 8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close"
                                )
                            }
                            Text(
                                text = "Next spin available in:",
                                style = TextStyle(
                                    textAlign = TextAlign.Center,
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontFamily = mattone,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                text = formatTime(remainingTime),
                                style = TextStyle(
                                    textAlign = TextAlign.Center,
                                    color = Color.White,
                                    fontSize = 24.sp,
                                    fontFamily = mattone,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                text = "You need to wait for your next spin.",
                                style = TextStyle(
                                    textAlign = TextAlign.Center,
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontFamily = mattone,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            TextButton(
                                onClick = {
                                    if (GemShopManager.useGemsForSpin()) {
                                        Log.d(TAG, "Using gems for spin.")
                                        useGemsToSpin = true
                                    } else {
                                        Toast.makeText(
                                            mContext,
                                            "Not enough gems.",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    }
                                },
                                modifier = Modifier
                                    .padding(8.dp)
                                    .fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(
                                    colorResource(id = R.color.primary_variant),
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(32.dp)
                            ) {
                                Text(
                                    text = "Use 5 Gems to Spin Now",
                                    style = TextStyle(
                                        textAlign = TextAlign.Center,
                                        fontSize = 14.sp,
                                        fontFamily = mattone,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                    } else if (GemShopManager.canSpinWheel() || useGemsToSpin) {
                        SpinWheel { selectedIndex, selectedIconName, selectedGiftAmount ->
                            Log.d(
                                TAG,
                                "$selectedIndex, $selectedIconName, $selectedGiftAmount"
                            )
                            selectedGiftName = selectedIconName
                            selectedGiftText = selectedGiftAmount.toString()
                            selectedGiftIcon = getIconResource(selectedIconName)
                            showCongratulationDialog = true
                            if (GemShopManager.canSpinWheel()) {
                                GemShopManager.spinWheel()
                            }
                        }
                    }
                }
            }
        }
    }*/
    if (showCongratulationDialog) {
        SoundPlayer.playSound(mContext, R.raw.congrats)
        CongratulationDialog(
            onDismiss = {
                when (selectedGiftName) {
                    "gem" -> {
                        viewModel.incrementGems(Integer.parseInt(selectedGiftText))
                    }

                    "hint" -> {
                        viewModel.incrementHints(Integer.parseInt(selectedGiftText))
                    }

                    "boost" -> {
                        viewModel.incrementBoosts(Integer.parseInt(selectedGiftText))
                    }
                }

                showCongratulationDialog = false
//                showSpinWheel = false
                useGemsToSpin = false
            },
            painterResource(id = selectedGiftIcon),
            "Congratulations!",
            "+$selectedGiftText $selectedGiftName"
        )
    }

    if (showBuyGemsDialog) {
        BuyGemsDialog(onGemsEarned = {
            mGems = GemShopManager.getGemsTotal()
        })
        { showBuyGemsDialog = false }
    }
}