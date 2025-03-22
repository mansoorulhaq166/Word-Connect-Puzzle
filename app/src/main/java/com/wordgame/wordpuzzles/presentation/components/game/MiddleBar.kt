package com.wordgame.wordpuzzles.presentation.components.game

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.wordgame.wordpuzzles.R
import com.wordgame.wordpuzzles.core.utils.ShowLetterState

@Composable
fun MiddleBar(
    availableBoosts: Int,
    availableShow: Int,
    onBoostClicked: (offset: Offset) -> Unit = {},
    onShowClicked: (offset: Offset) -> Unit = {}
) {
    var showLetterOnClick = ShowLetterState.showLetterOnClick.value
    val buttonScale by animateFloatAsState(if (showLetterOnClick) 1.2f else 1f, label = "")

/*    val boosts = GemShopManager.getTotalBoosts()
    val showClicks = GemShopManager.getTotalShowClicks()

    if (boosts < 1 || showClicks < 1) {
        ShowLetterSnackBar(
            showLetterOnClick = true,
            onDismiss = { },
            "Using Gems for the Powerup!"
        )
    }*/
    Row(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxSize(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val modifier = Modifier
            .padding(4.dp)
            .size(48.dp)
        Box(
            modifier = Modifier
                .padding(4.dp)
        ) {
            ButtonRound(
                modifier = modifier,
                default = R.drawable.boost_default,
                variant = R.drawable.boost_variant
            ) { offset ->
                onBoostClicked(offset)
            }
            Badge(
                modifier = Modifier.align(Alignment.TopEnd),
                badgeColor = Color(0xFF2196F3),  // Blue
                count = availableBoosts
            )
        }
        Box(
            modifier = Modifier
                .padding(8.dp)
        ) {
            ButtonRound(
                modifier = modifier.scale(buttonScale),
                default = R.drawable.point_default,
                variant = R.drawable.point_variant
            ) {
                showLetterOnClick = !showLetterOnClick
                onShowClicked(it)
            }
            Badge(
                modifier = Modifier.align(Alignment.TopEnd),
                badgeColor = Color(0xFF2196F3),
                count = availableShow
            )
        }
    }
}