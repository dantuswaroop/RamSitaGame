package com.dantu.findingsita.ui.screens

import android.content.Context
import androidx.lifecycle.ViewModel
import com.dantu.findingsita.data.DataBaseHelper
import com.dantu.findingsita.data.GameDataBase
import com.dantu.findingsita.data.entities.Game
import com.dantu.findingsita.data.entities.GameStatus
import com.dantu.findingsita.data.entities.Player
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import java.util.Date
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class SelectPlayersViewModel @Inject constructor(private val gameDataBase: GameDataBase) : ViewModel() {
    private val _allPlayers = MutableStateFlow(mutableListOf<Player>())
    val allPlayers: StateFlow<MutableList<Player>> = _allPlayers

    suspend fun getPlayers() {
        _allPlayers.value =
            gameDataBase.playerDao().getAllPlayers().first()
                .toMutableList()
    }

    suspend fun toggleSelection(index: Int) {
        val player = allPlayers.value[index].copy()
        player.selected = !player.selected
        val newList = mutableListOf<Player>()
        newList.addAll(_allPlayers.value)
        newList[index] = player
        _allPlayers.emit(newList)
    }

    fun createGameWithPlayers(): String {
        val game = Game(UUID.randomUUID().toString(), null, Date().toString())
        gameDataBase.gameDao().createGame(game)
        with(gameDataBase.gameStatusDao()) {
            _allPlayers.value.filter { it.selected }.forEach { player ->
                insertPlayerRecord(GameStatus(gameId = game.id, player = player, score = 0))
            }
        }
        return game.id
    }
}