package com.dantu.findingsita.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import com.dantu.findingsita.data.DataBaseHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable

@Serializable
data class AddOrEditPlayer(var playerId : Int)

@Composable
fun PlayerProfileScreen(
    playerId : Int,
    onSaveClicked: () -> Unit,
    onCancel: () -> Unit,
    onDelete : () -> Unit
) {
    val viewModel : PlayerProfileViewModel = hiltViewModel()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var playerName by rememberSaveable {
        mutableStateOf("")
    }
    var playerPassword by rememberSaveable {
        mutableStateOf("")
    }
    LaunchedEffect(scope) {
        playerId?.let {
            withContext(Dispatchers.IO) {
                DataBaseHelper.getInstance(context).playerDao().getPlayer(playerId.toInt())?.let {
                    playerName = it.name
                    playerPassword = it.pin.toString()
                }
            }
        }
    }
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        val focusManager = LocalFocusManager.current
        val keyboardController = LocalSoftwareKeyboardController.current
        val (nameTitle, nameField, passwordTitle, passwordField) = createRefs()
        val saveButton = createRef()
        val cancelButton = createRef()
        val deletePlayerButton = createRef()
        Text(text = "Name", modifier = Modifier.constrainAs(nameTitle) {
            absoluteLeft.linkTo(parent.absoluteLeft, margin = 20.dp)
            top.linkTo(parent.top, margin = 20.dp)
        })
        OutlinedTextField(value = playerName,
            onValueChange = {
                if (it.length < 8) {
                    playerName = it
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = {
                focusManager.moveFocus(FocusDirection.Down)
            }),
            isError = playerName.isBlank() || playerName.trim().length < 4,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .constrainAs(nameField) {
                    top.linkTo(nameTitle.bottom, margin = 10.dp)
                    absoluteLeft.linkTo(nameTitle.absoluteLeft)
                })
        Text(text = "Password", modifier = Modifier
            .constrainAs(passwordTitle) {
                absoluteLeft.linkTo(nameField.absoluteLeft)
                top.linkTo(nameField.bottom, margin = 20.dp)
            })
        OutlinedTextField(value = playerPassword,
            onValueChange = {
                if (it.length < 5) {
                    playerPassword = it
                }

            },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword,
                imeAction = ImeAction.Done
            ),
            isError = playerName.isBlank() || playerPassword.trim().length < 4,
            keyboardActions = KeyboardActions(onDone = {
                if (playerPassword.length == 4 && playerName.isNotBlank()) {
                    keyboardController?.hide()
                    viewModel.viewModelScope.launch {
                        viewModel.createOrUpdatePlayer(playerId, playerName, playerPassword.toInt())
                    }
                    onSaveClicked()
                }
            }),
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .constrainAs(passwordField) {
                    top.linkTo(passwordTitle.bottom, margin = 10.dp)
                    absoluteLeft.linkTo(passwordTitle.absoluteLeft)
                })
        Button(onClick = {
            if (playerPassword.length == 4 && playerName.isNotBlank()) {
                keyboardController?.hide()
                viewModel.viewModelScope.launch {
                    viewModel.createOrUpdatePlayer(playerId, playerName, playerPassword.toInt())
                }
                onSaveClicked()
            }
        },
            modifier = Modifier.constrainAs(saveButton) {
                absoluteLeft.linkTo(parent.absoluteLeft)
                absoluteRight.linkTo(parent.absoluteRight)
                top.linkTo(passwordField.bottom, margin = 20.dp)
            }) {
            Text(text = if(playerId > 0) "Save" else "Add" )
        }
        Button(onClick = {
            keyboardController?.hide()
            onCancel()
        },
            modifier = Modifier.constrainAs(cancelButton) {
                absoluteLeft.linkTo(saveButton.absoluteRight, margin = 5.dp)
                absoluteRight.linkTo(parent.absoluteRight)
                top.linkTo(passwordField.bottom, margin = 20.dp)
            }) {
            Text(text = "Cancel")
        }
        if(playerId > 0) {
            Button(onClick = {
                viewModel.deletePlayer(playerId)
                onDelete()
                             },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .constrainAs(deletePlayerButton) {
                        top.linkTo(saveButton.bottom, margin = 10.dp)
                        absoluteLeft.linkTo(parent.absoluteLeft)
                        absoluteRight.linkTo(parent.absoluteRight)
                    }) {
                Text(text = "Delete")
            }
        }
    }
}