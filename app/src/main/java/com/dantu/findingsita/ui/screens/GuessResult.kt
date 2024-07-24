package com.dantu.findingsita.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.dantu.findingsita.R
import kotlinx.serialization.Serializable

@Serializable
data class GuessResult(val success: Boolean)

@Composable
fun GuessResultDialog(modifier: Modifier, success: Boolean) {
    Column(
        modifier
            .fillMaxWidth(0.7f)
            .height(400.dp)
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = if (success) R.drawable.baseline_check_24 else R.drawable.wrong_guess),
            contentDescription = "",
            modifier = Modifier.size(100.dp, 100.dp)
        )
        Text(text = if (success) "Correct" else "Wrong")
    }
}