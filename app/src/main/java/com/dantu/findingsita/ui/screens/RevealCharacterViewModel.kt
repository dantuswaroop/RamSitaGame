package com.dantu.findingsita.ui.screens

import android.content.Context
import androidx.compose.material3.surfaceColorAtElevation
import androidx.lifecycle.ViewModel
import com.dantu.findingsita.data.DataBaseHelper
import com.dantu.findingsita.data.entities.CharacterType
import com.dantu.findingsita.data.entities.GameStatus
import com.dantu.findingsita.data.entities.Player
import com.dantu.findingsita.data.entities.characters
import java.util.Random

class RevealCharacterViewModel : ViewModel() {

    private var players: List<Player>? = null
    private lateinit var gameStatuses: List<GameStatus>

    suspend fun getPlayersWithCharacters(context: Context, gameId: String): List<Player> {
        if (players == null) {
            gameStatuses =
                DataBaseHelper.getInstance(context = context).gameStatusDao().getGameStatus(gameId)
            players = gameStatuses.map { it.player }
            val subCharacters = characters.subList(0, players!!.size)
            subCharacters.shuffled(Random()).forEachIndexed { index, character ->
                players!![index].character = character
            }
        }
        return players!!
    }

    suspend fun validateResult(context: Context, hiderId: Int): Boolean {
        val success =
            players!!.filter { it.playerId == hiderId }[0].character?.type == CharacterType.HIDE
        with(DataBaseHelper.getInstance(context).gameStatusDao()) {
            if (success) {
                val winner = players!!.filter { it.character?.type == CharacterType.FIND }[0]
                val winnerPlayerRow =
                    gameStatuses.filter { it.player.playerId == winner.playerId }[0].copy()
                winnerPlayerRow.score += (winner.character?.points ?: 0)
                updatePlayerScore(winnerPlayerRow)
            } else {
                val winner = players!!.filter { it.character?.type == CharacterType.HIDE }[0]
                val winnerPlayerRow =
                    gameStatuses.filter { it.player.playerId == winner.playerId }[0].copy()
                winnerPlayerRow.score += (winner.character?.points ?: 0)
                updatePlayerScore(winnerPlayerRow)
            }
            players!!.filter { it.character?.type == CharacterType.NONE }.forEach {normalPlayer ->
                val playerRow =
                    gameStatuses.filter { it.player.playerId == normalPlayer.playerId }[0].copy()
                playerRow.score += normalPlayer.character?.points?:0
                updatePlayerScore(playerRow)
            }
        }
        return success
    }

}