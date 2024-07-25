package com.dantu.findingsita

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.dantu.findingsita.data.DataBaseHelper
import com.dantu.findingsita.data.dao.PlayerDao
import com.dantu.findingsita.data.entities.Player
import com.dantu.findingsita.ui.screens.AddOrEditPlayer
import com.dantu.findingsita.ui.screens.CharacterScreen
import com.dantu.findingsita.ui.screens.EnterPinDialog
import com.dantu.findingsita.ui.screens.EnterPinDialogForReveal
import com.dantu.findingsita.ui.screens.GameLeaderBoard
import com.dantu.findingsita.ui.screens.GameLeaderBoardScreen
import com.dantu.findingsita.ui.screens.LoadGames
import com.dantu.findingsita.ui.screens.LoadGamesScreen
import com.dantu.findingsita.ui.screens.PlayerList
import com.dantu.findingsita.ui.screens.PlayerListScreen
import com.dantu.findingsita.ui.screens.PlayerProfileScreen
import com.dantu.findingsita.ui.screens.GameScreen
import com.dantu.findingsita.ui.screens.GuessResult
import com.dantu.findingsita.ui.screens.GuessResultDialog
import com.dantu.findingsita.ui.screens.ShowGameScreen
import com.dantu.findingsita.ui.screens.SelectPlayers
import com.dantu.findingsita.ui.screens.ShowCharacterToPlayer
import com.dantu.findingsita.ui.screens.ValidatePin
import com.dantu.findingsita.ui.screens.WelcomeScreenComposable
import com.dantu.findingsita.ui.theme.FindingSitaTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable

@Serializable
object HomeScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FindingSitaTheme {
                val snackbarHostState = remember { SnackbarHostState() }
                var screenTitle by rememberSaveable {
                    mutableStateOf("Finding Sita")
                }
                val navController = rememberNavController()
                Scaffold(
                    topBar = {
                        TopAppBar(title = { Text(text = screenTitle) },
                            navigationIcon = {
                                IconButton(onClick = { navController.popBackStack() }) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = ""
                                    )
                                }
                            })
                    },
                    snackbarHost = {
                        SnackbarHost(hostState = snackbarHostState)
                    }, modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding),
                        snackbarHostState,
                        navController
                    ) { scrTitle ->
                        screenTitle = scrTitle
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(
    name: String,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    navController: NavHostController,
    title: (String) -> Unit
) {
    val scope = rememberCoroutineScope()
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = HomeScreen
    ) {

        composable<HomeScreen> {
            title("Finding Sita")
            WelcomeScreenComposable(onPlayers = {
                navController.navigate(PlayerList)
            }, onStartGame = {
                navController.navigate(SelectPlayers)
            }, loadGame = {
                navController.navigate(LoadGames)
            }
            )
        }

        composable<PlayerList> {
            title("Players")
            PlayerListScreen(modifier, onAddNewPlayer = { playerId ->
                if (playerId > 0) {
                    navController.navigate(EnterPinDialog(playerId = playerId))
                } else {
                    navController.navigate(AddOrEditPlayer(playerId))
                }

            })
        }

        composable<AddOrEditPlayer> {
            val playerId = it.toRoute<AddOrEditPlayer>().playerId
            val context = LocalContext.current
            if (playerId > 0) {
                title("Edit Player")
            } else {
                title("Add New Player")
            }
            PlayerProfileScreen(playerId, onSaveClicked = { playerName, playerPassword ->
                scope.launch {
                    withContext(Dispatchers.IO) {
                        playerId?.let {
                            DataBaseHelper.getInstance(context).playerDao()
                                .let { playerDao: PlayerDao ->
                                    playerDao.getPlayer(playerId)?.let { player ->
                                        player.name = playerName
                                        player.pin = playerPassword
                                        playerDao.updatePlayer(player)
                                    }
                                }

                        } ?: run {
                            DataBaseHelper.getInstance(context).playerDao()
                                .addPlayer(Player(name = playerName, pin = playerPassword))
                        }
                    }
                    navController.popBackStack()
                    snackbarHostState.showSnackbar("Saved $playerName with password $playerPassword")
                }
            }, onCancel = {
                navController.popBackStack()
            },
                onDelete = { playerId ->
                    scope.launch {
                        withContext(Dispatchers.IO) {
                            DataBaseHelper.getInstance(context).playerDao()
                                .deletePlayer(Player(playerId.toInt(), "", 0))
                        }
                    }
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
        dialog<EnterPinDialogForReveal> {
            val enterPinDialog: EnterPinDialogForReveal = it.toRoute<EnterPinDialogForReveal>()
            ValidatePin(playerId = enterPinDialog.playerId) { valid ->
                navController.popBackStack()
                if (valid) {
//                    navController.navigate(
//                        AddOrEditPlayer(playerId = enterPinDialog.playerId)
//                    )
                    navController.navigate(CharacterScreen(enterPinDialog.characterId))
                } else {
                    scope.launch {
                        snackbarHostState.showSnackbar("Entered pin is $valid")
                    }
                }
            }
        }

        composable<SelectPlayers> {
            title("Select Players")
            SelectPlayers(modifier, mutableListOf<Int>(), onPlayersSelected = {gameId ->
                navController.popBackStack()
                navController.navigate(GameLeaderBoard(gameId = gameId))
                //Navigate to Leaderboard screen with start Game Button
            }, onMessage =  {
                scope.launch {
                    snackbarHostState.showSnackbar(it)
                }
            })
        }

        composable<GameLeaderBoard> {
            title("Score Board")
            val gameLeaderBoard : GameLeaderBoard = it.toRoute()
            GameLeaderBoardScreen(modifier, gameId = gameLeaderBoard.gameId) {
                navController.popBackStack()
                navController.navigate(GameScreen(gameLeaderBoard.gameId))
            }
        }
        
        composable<LoadGames> {
            title("Saved Games")
            LoadGamesScreen(modifier = modifier) {gameId ->
                navController.navigate(GameLeaderBoard(gameId))
            }
        }

        composable<GameScreen> {
            title("Game")
            val reveal : GameScreen = it.toRoute()
            ShowGameScreen(modifier = modifier, gameId = reveal.gameId, onPlayerClicked =
            { playerId, characterId ->
              navController.navigate(EnterPinDialogForReveal(playerId, characterId))
            }, onRoundFinished = {success ->
                navController.popBackStack()
                navController.navigate(GameLeaderBoard(reveal.gameId))
                navController.navigate(GuessResult(success = success))
            })
        }

        composable<CharacterScreen> {
            title("Your Character")
            val character : CharacterScreen = it.toRoute()
            ShowCharacterToPlayer(modifier, character.characterId) {
                navController.popBackStack()
            }
        }

        dialog<GuessResult> {
            val guessResult : GuessResult = it.toRoute()
            GuessResultDialog(modifier = modifier, success = guessResult.success)
        }
    }
}




