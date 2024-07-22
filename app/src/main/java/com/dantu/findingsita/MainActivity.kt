package com.dantu.findingsita

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.text.isDigitsOnly
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import com.dantu.findingsita.ui.screens.WelcomeScreenComposable
import com.dantu.findingsita.ui.theme.FindingSitaTheme
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
object HomeScreen

@Serializable
object PlayerList

@Serializable
object AddNewPlayer

@Serializable
object EnterPinDialog

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FindingSitaTheme {
                val snackbarHostState = remember { SnackbarHostState() }
                Scaffold(snackbarHost = {
                    SnackbarHost(hostState = snackbarHostState)
                }, modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding),
                        snackbarHostState
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier, snackbarHostState: SnackbarHostState) {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = HomeScreen
    ) {

        composable<HomeScreen> {
            WelcomeScreenComposable {
                navController.navigate(PlayerList)
            }
        }

        composable<PlayerList> {
            PlayerListScreen(onAddNewPlayer = {
                navController.navigate(AddNewPlayer)
            })
        }

        composable<AddNewPlayer> {
            AddNewPlayerScreen(onSaveClicked = { playerName, playerPassword ->
                scope.launch {
                    snackbarHostState.showSnackbar("Saving $playerName with password $playerPassword")
                }
            }, onCancel = {
                navController.popBackStack()
            })
        }

        dialog<EnterPinDialog> {
//            EnterPinDialogScreen()
        }

    }
}

@Composable
fun AddNewPlayerScreen(
    onSaveClicked: (String, String) -> Unit,
    onCancel: () -> Unit
) {
    var playerName by rememberSaveable {
        mutableStateOf("")
    }
    var playerPassword by rememberSaveable {
        mutableStateOf("")
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
                keyboardController?.hide()
                onSaveClicked(
                    playerName,
                    playerPassword
                )
            }),
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .constrainAs(passwordField) {
                    top.linkTo(passwordTitle.bottom, margin = 10.dp)
                    absoluteLeft.linkTo(passwordTitle.absoluteLeft)
                })
        Button(onClick = {
            keyboardController?.hide()
            onSaveClicked(
                playerName,
                playerPassword
            )
        },
            modifier = Modifier.constrainAs(saveButton) {
                absoluteLeft.linkTo(parent.absoluteLeft)
                absoluteRight.linkTo(parent.absoluteRight)
                top.linkTo(passwordField.bottom, margin = 20.dp)
            }) {
            Text(text = "Add")
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
    }
}

@Composable
fun PlayerListScreen(onAddNewPlayer: () -> Unit) {
    ConstraintLayout(
        modifier = Modifier
            .background(Color.Gray)
            .fillMaxSize()
    ) {
        val addNewPlayerButton = createRef()
        val buttonBackground = createRef()
        Box(
            modifier = Modifier
                .background(Color.White)
                .fillMaxWidth()
                .height(100.dp)
                .constrainAs(buttonBackground) {
                    bottom.linkTo(parent.bottom)
                    top.linkTo(addNewPlayerButton.top)
                })
        Button(onClick = onAddNewPlayer,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .constrainAs(addNewPlayerButton) {
                    absoluteLeft.linkTo(parent.absoluteLeft)
                    absoluteRight.linkTo(parent.absoluteRight)
                    bottom.linkTo(parent.bottom, margin = 20.dp)
                }) {
            Text(text = "Add New Player")
        }

    }

}

