package com.dantu.findingsita.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController


    @Composable
    fun WelcomeScreenComposable( onStartClicked : () -> Unit) {
        Column(
            Modifier
                .fillMaxSize()
                .background(Color.White)) {
            Column(modifier = Modifier
                .weight(.3f)
                .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Finding Sita",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp)
            }
            Column(modifier = Modifier
                .weight(.7f)
                .fillMaxSize(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally) {
                ElevatedButton(onClick =   onStartClicked, modifier = Modifier.fillMaxWidth(.9f)) {
                    Text(text = "Players", fontSize = 20.sp)
                }
                Spacer(modifier = Modifier.height(40.dp))
                ElevatedButton(onClick = {   }, modifier = Modifier.fillMaxWidth(.9f)) {
                    Text(text = "Start Game", fontSize = 20.sp)
                }
                Spacer(modifier = Modifier.height(40.dp))
                ElevatedButton(onClick = { },modifier = Modifier.fillMaxWidth(.9f)) {
                    Text(text = "Load Game", fontSize = 20.sp)
                }

                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }

    @Preview
    @Composable
    fun previewWelcomeScreen() {
//        WelcomeScreenComposable()
    }
