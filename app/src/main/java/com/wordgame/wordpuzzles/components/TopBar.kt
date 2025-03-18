package com.wordgame.wordpuzzles.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.wordgame.wordpuzzles.R
import com.wordgame.wordpuzzles.data.WordDetails
import com.wordgame.wordpuzzles.navigation.DestinationSettings
import com.wordgame.wordpuzzles.ui.theme.alfa_slab
import com.wordgame.wordpuzzles.ui.theme.krona

@Preview
@Composable
fun GameScreenPreview() {
    MaterialTheme {
        GameScreenUI()
    }
}

@Composable
fun GameScreenUI() {
    val list: List<WordDetails> = emptyList()
    Column {
        TopBar(level = "Level 1", gems = 5, navController = null, onSpinClick = {},
            onPlusClick = {}, onCompletedWordsClick = {}, list
        )
    }
}

@Composable
fun TopBar(
    level: String?, gems: Int, navController: NavController?,
    onSpinClick: () -> Unit,
    onPlusClick: () -> Unit,
    onCompletedWordsClick: () -> Unit,
    discoveredWords: List<WordDetails>?
) {
    var showDialog by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween,
    ) {

        //left
        if (level != null) {
            ButtonLevel(level)
        } else {
            ButtonSettings(navController = navController!!, onSpinClick, onCompletedWordsClick)
        }

        if (!discoveredWords.isNullOrEmpty()) {
            DiscoveredWordsButton(onClick = { showDialog = true })
        }
        //Right
        ButtonGem(gems, onPlusClick)
    }
    // Dialog to display discovered words
    if (showDialog) {
        AlertDialogWithDetails(
            showDialog = showDialog, onDismissRequest = { showDialog = false },
            discoveredWords = discoveredWords
        )
    }
}

@Composable
fun AlertDialogWithDetails(
    showDialog: Boolean,
    onDismissRequest: () -> Unit,
    discoveredWords: List<WordDetails>?
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = {
                Text(
                    text = "Discovered Words",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontFamily = alfa_slab, // Your custom font
                )
            },
            text = {
                Column {
                    // Populate the dialog with discovered words
                    discoveredWords?.forEach { wordDetails ->
                        WordEntry(wordDetails)
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = onDismissRequest,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clip(RoundedCornerShape(16.dp)), // Slightly less rounded corners
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1C88E5), // Beautiful color
                    ),
                ) {
                    Text(
                        text = "Close",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontFamily = krona, // Your custom font
                        fontSize = 18.sp
                    )
                }
            },
            containerColor = Color.Black
        )
    }
}

@Composable
fun WordEntry(wordDetails: WordDetails) {
    val word = wordDetails.word
    val details = wordDetails.details

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .border(1.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
            .background(Color.Black)
            .then(
                Modifier.shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(8.dp)
                )
            )
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            item {
                Text(
                    text = word,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.White,
                    fontFamily = FontFamily.Default,
                    textAlign = TextAlign.Start
                )
                details?.let {
                    Text(
                        text = it,
                        fontSize = 15.sp,
                        color = Color.Gray,
                        fontFamily = FontFamily.Default,
                        textAlign = TextAlign.Start
                    )
                }
            }
        }
    }
}

@Composable
fun DiscoveredWordsButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .clickable { onClick() }
            .border(1.dp, Color.Gray, shape = RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
    ) {
        Row(
            modifier = Modifier
                .width(42.dp)
                .height(32.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_format_list_bulleted_24), // Change this icon
                contentDescription = "Discovered Words",
                tint = Color.White,
                modifier = Modifier
                    .height(24.dp)
                    .width(28.dp)
                    .padding(start = 8.dp)
            )
        }
    }
}

@Composable
fun ButtonGem(gems: Int, onPlusClick: () -> Unit) {

    Surface(
        modifier = Modifier
            .padding(top = 4.dp, start = 8.dp, end = 8.dp)
            .width(110.dp)
            .height(32.dp)
            .border(1.dp, Color.Gray, shape = RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        color = Color.Transparent, // Set your desired background color here
        //      elevation = 4.dp // Set your desired elevation here
    ) {
        Box(
            modifier = Modifier
                .width(110.dp)
                .height(32.dp)
                .clip(RoundedCornerShape(16.dp))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 4.dp, end = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.gem),
                    contentDescription = "Gems",
                    tint = Color.White,
                    modifier = Modifier
                        .height(18.dp)
                        .width(24.dp)
                )
                Text(
                    text = "$gems",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp) // Adjust padding as needed
                )
                Image(
                    modifier = Modifier
                        .height(24.dp)
                        .width(28.dp)
                        .clickable { onPlusClick() },
                    painter = painterResource(id = R.drawable.plus),
                    contentDescription = "Plus Button",
                )
            }
        }
    }
}

@Composable
fun ButtonLevel(level: String) {
    Surface(
        modifier = Modifier
            .padding(top = 4.dp, start = 8.dp, end = 8.dp)
            .width(100.dp)
            .height(32.dp)
            .border(1.dp, Color.Gray, shape = RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        color = Color.Transparent, // Set your desired background color here
    ) {
        Box(
            modifier = Modifier
                .padding(top = 4.dp, start = 8.dp, end = 8.dp)
                .width(100.dp)
                .height(32.dp)
                .clip(RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(id = R.string.level, level),
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Composable
fun ButtonSettings(
    navController: NavController, onSpinClick: () -> Unit?,
    onCompletedWordsClick: () -> Unit?
) {
    var isPressed by remember { mutableStateOf(false) }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(top = 8.dp, start = 8.dp, end = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .padding(top = 8.dp, start = 8.dp, end = 8.dp)
                .size(42.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            val modifier = Modifier
                .fillMaxSize()
            ButtonRound(
                modifier = modifier,
                default = R.drawable.icon_settings,
                variant = R.drawable.icon_settings
            ) {
                navController.navigate(DestinationSettings.route)
            }
        }
//        Spacer(modifier = Modifier.height(8.dp))
//        Box(
//            modifier = Modifier
//                .size(56.dp)
//            //     .clip(CircleShape)
//            // ,
//            // contentAlignment = Alignment.Center
//        ) {
//            // Display the background image
//            Image(
//                painter = painterResource(id = R.drawable.lucky_wheel_bg),
//                contentDescription = null, // Provide a description if needed
//                modifier = Modifier.fillMaxSize(),
//                contentScale = ContentScale.FillBounds // You can try other ContentScale options
//            )
//
//            // Display the ButtonRound in the center
//            ButtonRound(
//                modifier = Modifier
//                    .align(Alignment.TopCenter)
//                    .size(42.dp)
//                    .padding(top = 8.dp),
//                default = R.drawable.luck_wheel,
//                variant = R.drawable.luck_wheel
//            ) {
//                onSpinClick()
//                isPressed = !isPressed
//            }
//        }
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .padding(16.dp)
                .size(32.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            val modifier = Modifier
                .fillMaxSize()

            /*         ButtonRound(
                         modifier = modifier,
                         default = R.drawable.baseline_format_list_bulleted_24,
                         variant = R.drawable.baseline_format_list_bulleted_24
                     ) {
                         onCompletedWordsClick()
                         isPressed = !isPressed
                     }*/
        }
    }
}