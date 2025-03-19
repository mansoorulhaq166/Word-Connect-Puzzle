package com.wordgame.wordpuzzles.components

import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.ads.rewarded.RewardedAd
import com.wordgame.wordpuzzles.R
import com.wordgame.wordpuzzles.ads.AdPrefs
import com.wordgame.wordpuzzles.ads.LoadRewardedAd
import com.wordgame.wordpuzzles.billing.Billing
import com.wordgame.wordpuzzles.billing.MEGA_PACK
import com.wordgame.wordpuzzles.billing.MINI_PACK
import com.wordgame.wordpuzzles.billing.NO_ADS
import com.wordgame.wordpuzzles.billing.SetupBilling
import com.wordgame.wordpuzzles.ui.screens.gameview.GameScreenViewModel
import com.wordgame.wordpuzzles.ui.theme.alfa_slab
import com.wordgame.wordpuzzles.ui.theme.darumadropone
import com.wordgame.wordpuzzles.ui.theme.krona
import com.wordgame.wordpuzzles.ui.theme.mattone
import kotlinx.coroutines.delay

@Composable
fun BuyGemsDialog(
    onGemsEarned: () -> Unit,
    onDismiss: () -> Unit
) {
    val isRowVisible by remember { mutableStateOf(false) }
    var loadAd by remember {
        mutableStateOf(false)
    }
    var loadBilling by remember {
        mutableStateOf(false)
    }
    var loadingProgress by remember {
        mutableFloatStateOf(0f)
    }
    var rewardedAd: RewardedAd? by remember {
        mutableStateOf(null)
    }
    var billingID by remember {
        mutableStateOf("")
    }
    val activity = LocalActivity.current
    val context = LocalContext.current
    val viewModel: GameScreenViewModel = hiltViewModel()

    SetupBilling()

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Surface(
            modifier = Modifier
                .size(300.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(id = R.drawable.dialog_background),
                contentDescription = "Background",
                contentScale = ContentScale.FillBounds
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxSize()
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Watch Ad Option
                    Box(
                        modifier = Modifier
                            .width(120.dp)
                            .height(120.dp)
                    ) {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable(onClick = {
                                    loadAd = true
                                })
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.icon_video_ad),
                                contentDescription = "Watch Ad",
                                modifier = Modifier
                                    .width(55.dp)
                                    .height(55.dp)
                            )
                            Text(
                                text = "Watch Ad",
                                style = TextStyle(
                                    color = Color.Green, fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    fontFamily = krona
                                )
                            )
                            Text(
                                text = "+ 3",
                                style = TextStyle(
                                    color = Color.Green, fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    fontFamily = krona
                                )
                            )
                        }
                    }
                    // Remove Ads
                    if (!AdPrefs.isPremium()) {
                        Box(
                            modifier = Modifier
                                .width(120.dp)
                                .height(120.dp)
                        ) {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clickable(onClick = {
                                        billingID = NO_ADS
                                        loadBilling = true
                                    })
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.icon_ad_remove),
                                    contentDescription = "Remove Ads",
                                    modifier = Modifier
                                        .width(55.dp)
                                        .height(55.dp)
                                )
                                Text(
                                    text = "Remove Ads Forever",
                                    style = TextStyle(
                                        color = Color.Green, fontSize = 14.sp,
                                        textAlign = TextAlign.Center,
                                        fontFamily = mattone
                                    )
                                )
                            }
                        }
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
                ) {
                    GemButton(
                        gemCount = "50",
                        price = "Buy 50 Gems",
                        onClick = {
                            //                   onBuy50GemsClicked()
                            billingID = MINI_PACK
                            loadBilling = true
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                    )

                    GemButton(
                        gemCount = "125",
                        price = "Buy 125 Gems",
                        onClick = {
                            //                 onBuy125GemsClicked()
                            billingID = MEGA_PACK
                            loadBilling = true
                        },
                        modifier = Modifier.weight(1f).padding(start = 8.dp)
                    )
                }
                if (isRowVisible) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        RoundedButton(
                            text = "Buy 200 Gems",
                            onClick = {}
                        )

                        RoundedButton(
                            text = "Buy 325 Gems",
                            onClick = {}
                        )
                    }
                }
            }
        }
        if (loadAd) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(15.dp)
                    .padding(8.dp),
                color = MaterialTheme.colorScheme.scrim,
                strokeWidth = 4.dp
            )
        }
    }
    LaunchedEffect(loadingProgress) {
        if (loadAd && loadingProgress < 1f) {
            delay(100) // Delay between progress updates
            loadingProgress += 0.05f // Gradually increase progress
        }
    }
    if (loadAd) {
        LoadRewardedAd(
            onAdLoaded = { ad ->
                activity?.let {
                    rewardedAd = ad
                    rewardedAd?.show(it) {
                        viewModel.incrementGems(3)
                        loadAd = false
                        onGemsEarned()
                    }
                }
            },
            onAdFailed = {
                Toast.makeText(context, "No Ads Available", Toast.LENGTH_SHORT).show()
                loadAd = false
            }
        ) { progress ->
            loadingProgress = progress as Float
        }
    }
    if (loadBilling) {
        Billing(billingID)
    }
}

@Composable
fun RoundedButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .width(140.dp)
            .height(50.dp),
        shape = RoundedCornerShape(25.dp),
        colors = ButtonDefaults.buttonColors(
            colorResource(id = R.color.primary_variant),
            contentColor = Color.Black
        )
    ) {
        Text(text, style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold))
    }
}

@Composable
fun GemButton(
    gemCount: String,
    price: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val backgroundColor = animateColorAsState(
        targetValue = if (isPressed)
            Color(0xFF7B40E5)
        else
            Color(0xFF8F4FFF),
        label = "backgroundColor"
    )

    Button(
        onClick = onClick,
        modifier = modifier
            .height(80.dp)
            .shadow(
                elevation = if (isPressed) 2.dp else 4.dp,
                shape = RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor.value,
            contentColor = Color.White
        ),
        interactionSource = interactionSource
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Gems",
                    modifier = Modifier.size(24.dp),
                    tint = Color(0xFFFFD700)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = gemCount,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(
                        textAlign = TextAlign.Center,
                        fontFamily = krona
                    ),
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = price,
                fontSize = 14.sp,
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    fontFamily = mattone
                ),
                color = Color(0xFFE0E0E0)
            )
        }
    }
}