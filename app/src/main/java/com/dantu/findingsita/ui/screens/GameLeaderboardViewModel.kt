package com.dantu.findingsita.ui.screens

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.dantu.findingsita.data.DataBaseHelper
import com.dantu.findingsita.data.entities.GameStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class GameLeaderboardViewModel @Inject constructor() : ViewModel() {
    private val _gameStatus  = MutableStateFlow(listOf<GameStatus>())
    val gameStatus : StateFlow<List<GameStatus>> = _gameStatus

    suspend fun getLeaderBoard(context: Context, gameId : String) {
        _gameStatus.value = DataBaseHelper.getInstance(context = context).gameStatusDao().getGameStatus(gameId).sortedByDescending { it.score }
    }
}