package com.dantu.findingsita.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.dantu.findingsita.data.DataBaseHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable

@Serializable
data class EnterPinDialog(val playerId : String)

@Composable
fun ValidatePin(playerId: String, onValidation: (Boolean) -> Unit) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    var playerPin by remember {
        mutableStateOf(0)
    }
    var enteredPin by remember {
        mutableStateOf("")
    }
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            DataBaseHelper.getInstance(context).playerDao().getPlayer(playerId)?.let {
                playerPin = it.pin
            }

        }
    }

    Column(Modifier.background(Color.White).padding(40.dp)) {
        Text(text = "Enter your password")
        OutlinedTextField(
            value = enteredPin, onValueChange = {
                enteredPin = it
            }, keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, keyboardType = KeyboardType.NumberPassword),
            keyboardActions = KeyboardActions(onDone = {
                keyboardController?.hide()
                onValidation(playerPin.toString() == enteredPin)
            })
        )
    }
}