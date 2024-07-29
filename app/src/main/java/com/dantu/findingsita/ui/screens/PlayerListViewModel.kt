package com.dantu.findingsita.ui.screens

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dantu.findingsita.data.entities.Player
import com.dantu.findingsita.domain.GetAllPlayersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerListViewModel @Inject constructor(
    private val getAllPlayersUseCase: GetAllPlayersUseCase
) : ViewModel() {

    private val _playersData = MutableStateFlow(listOf<Player>())
    val playersData : StateFlow<List<Player>> = _playersData

    fun getAllPlayers() {
        viewModelScope.launch {
            _playersData.value = getAllPlayersUseCase()
        }
    }
}