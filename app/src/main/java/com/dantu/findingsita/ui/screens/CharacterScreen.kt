package com.dantu.findingsita.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dantu.findingsita.data.entities.characters
import kotlinx.serialization.Serializable

@Serializable
data class CharacterScreen(val characterId: Int)

@Composable
fun ShowCharacterToPlayer(modifier: Modifier, characterId: Int, goBack: () -> Unit) {
    val character = characters.filter { it.characterId == characterId }[0]
    Column(
        Modifier
            .fillMaxSize()
            .clickable {
                goBack()
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(text = "${character.name}", fontSize = 48.sp, fontWeight = FontWeight.ExtraBold)
        Image(painter = painterResource(id = character.image), contentDescription = "")
        Spacer(
            modifier = Modifier
                .height(10.dp)
                .fillMaxWidth()
        )
        Text(text = "${character.points}", fontSize = 24.sp)
    }

}