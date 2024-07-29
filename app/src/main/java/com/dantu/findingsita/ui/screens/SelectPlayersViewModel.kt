package com.dantu.findingsita.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dantu.findingsita.data.entities.Game
import com.dantu.findingsita.data.entities.GameStatus
import com.dantu.findingsita.data.entities.Player
import com.dantu.findingsita.domain.usecase.CreateGameUseCase
import com.dantu.findingsita.domain.usecase.GetAllPlayersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class SelectPlayersViewModel @Inject constructor(private val getAllPlayersUseCase: GetAllPlayersUseCase,
                                                 private val createGameUseCase : CreateGameUseCase
) : ViewModel() {
    private val _allPlayers = MutableStateFlow(mutableListOf<Player>())
    val allPlayers: StateFlow<MutableList<Player>> = _allPlayers

    private val _gameId = MutableSharedFlow<String>()
    val gameId : SharedFlow<String> = _gameId

    suspend fun getPlayers() {
        _allPlayers.value = getAllPlayersUseCase().toMutableList()
    }

    suspend fun toggleSelection(index: Int) {
        val player = allPlayers.value[index].copy()
        player.selected = !player.selected
        val newList = mutableListOf<Player>()
        newList.addAll(_allPlayers.value)
        newList[index] = player
        _allPlayers.emit(newList)
    }

    fun createGameWithPlayers() {
        viewModelScope.launch(Dispatchers.IO) {
            _gameId.emit(createGameUseCase.invoke(_allPlayers.value.filter { it.selected }))
        }
    }
}