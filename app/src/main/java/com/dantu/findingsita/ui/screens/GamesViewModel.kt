package com.dantu.findingsita.ui.screens

import android.content.Context
import androidx.lifecycle.ViewModel
import com.dantu.findingsita.data.DataBaseHelper
import com.dantu.findingsita.data.entities.Game
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class GamesViewModel @Inject constructor() : ViewModel() {

    private val _games = MutableStateFlow(listOf<Game>())
    val games : StateFlow<List<Game>> = _games

    suspend fun loadGames(context: Context) {
        _games.value = DataBaseHelper.getInstance(context).gameDao().getAllGames()
    }
}