package com.dantu.findingsita

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.dantu.findingsita.data.DataBaseHelper
import com.dantu.findingsita.data.entities.Player
import com.dantu.findingsita.ui.screens.AddOrEditPlayer
import com.dantu.findingsita.ui.screens.EnterPinDialog
import com.dantu.findingsita.ui.screens.PlayerList
import com.dantu.findingsita.ui.screens.PlayerListScreen
import com.dantu.findingsita.ui.screens.PlayerProfileScreen
import com.dantu.findingsita.ui.screens.ValidatePin
import com.dantu.findingsita.ui.screens.WelcomeScreenComposable
import com.dantu.findingsita.ui.theme.FindingSitaTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable

@Serializable
object HomeScreen


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
            PlayerListScreen(modifier, onAddNewPlayer = { playerId ->
                playerId?.let {
                    navController.navigate(EnterPinDialog(playerId = playerId))
                } ?: run {
                    navController.navigate(AddOrEditPlayer(playerId))
                }

            })
        }

        composable<AddOrEditPlayer> {
            val playerId = it.toRoute<AddOrEditPlayer>().playerId
            val context = LocalContext.current
            PlayerProfileScreen(playerId, onSaveClicked = { playerName, playerPassword ->
                scope.launch {
                    withContext(Dispatchers.IO) {
                        DataBaseHelper.getInstance(context).playerDao()
                            .addPlayer(Player(name = playerName, pin = playerPassword))
                    }
                    navController.popBackStack()
                    snackbarHostState.showSnackbar("Saved $playerName with password $playerPassword")
                }
            }, onCancel = {
                navController.popBackStack()
            })
        }

        dialog<EnterPinDialog> {
            val enterPinDialog: EnterPinDialog = it.toRoute<EnterPinDialog>()
            ValidatePin(playerId = enterPinDialog.playerId) { valid ->
                navController.popBackStack()

                if (valid) {
                    navController.navigate(
                        AddOrEditPlayer(playerId = enterPinDialog.playerId)
                    )
                } else {
                    scope.launch {
                        snackbarHostState.showSnackbar("Entered pin is $valid")
                    }
                }
            }
        }

    }
}




