package com.dantu.findingsita.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dantu.findingsita.domain.usecase.CreateOrUpdatePlayerUseCase
import com.dantu.findingsita.domain.usecase.DeletePlayerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerProfileViewModel @Inject constructor(private val createOrUpdatePlayerUseCase : CreateOrUpdatePlayerUseCase,
                                                 private val deletePlayerUseCase : DeletePlayerUseCase
) : ViewModel() {

    fun createOrUpdatePlayer(id : Int?, name : String, pin : Int) {
        viewModelScope.launch(Dispatchers.IO) {
            createOrUpdatePlayerUseCase(id, name, pin)
        }
    }

    fun deletePlayer(playerId : Int) {
        viewModelScope.launch(Dispatchers.IO) {
            deletePlayerUseCase(playerId)
        }
    }
}