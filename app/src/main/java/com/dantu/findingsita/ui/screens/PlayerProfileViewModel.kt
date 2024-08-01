package com.dantu.findingsita.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dantu.findingsita.data.entities.Player
import com.dantu.findingsita.domain.usecase.CreateOrUpdatePlayerUseCase
import com.dantu.findingsita.domain.usecase.DeletePlayerUseCase
import com.dantu.findingsita.domain.usecase.GetPlayerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerProfileViewModel @Inject constructor(private val createOrUpdatePlayerUseCase : CreateOrUpdatePlayerUseCase,
                                                 private val deletePlayerUseCase : DeletePlayerUseCase,
                private val getPlayerUseCase : GetPlayerUseCase
) : ViewModel() {

    private val _playerData = MutableSharedFlow<Player?>()
    val playerData : SharedFlow<Player?> = _playerData


    fun createOrUpdatePlayer(id: Int, name: String, pin: Int, profilePictureUri: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            createOrUpdatePlayerUseCase.invoke(id, name, pin, profilePictureUri)
        }
    }

    fun deletePlayer(playerId : Int) {
        viewModelScope.launch(Dispatchers.IO) {
            deletePlayerUseCase(playerId)
        }
    }

    fun getPlayer(playerId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _playerData.emit(getPlayerUseCase(playerId))
        }
    }
}