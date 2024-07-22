package com.dantu.findingsita.ui.screens

import android.R
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.dantu.findingsita.data.DataBaseHelper
import com.dantu.findingsita.data.entities.Player
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
object PlayerList

@Composable
fun PlayerListScreen(modifier: Modifier, onAddNewPlayer: (String?) -> Unit) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 20.dp)
    ) {
        val scope = rememberCoroutineScope()
        val context = LocalContext.current
        var allPlayers by remember {
            mutableStateOf<List<Player>>(listOf())
        }

        val addNewPlayerButton = createRef()
        val buttonBackground = createRef()
        val playerListLazyColumn = createRef()

        LaunchedEffect(key1 = scope) {
            launch(Dispatchers.IO) {
                allPlayers =
                    DataBaseHelper.getInstance(context).playerDao().getAllPlayers().first()
                Log.i("", allPlayers!!.size.toString())
            }
        }
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = modifier
                .fillMaxSize()
                .constrainAs(playerListLazyColumn) {
                    top.linkTo(parent.top, margin = 80.dp)
                    bottom.linkTo(buttonBackground.top)
                    absoluteLeft.linkTo(parent.absoluteLeft)
                    absoluteRight.linkTo(parent.absoluteRight)
                }) {
            items(allPlayers) {
                Row(verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .clickable {
                            onAddNewPlayer(it.name)
                        }
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text(
                        text = it.name, fontSize = 24.sp
                    )
                    Image(painter = painterResource(id = R.drawable.ic_menu_edit), contentDescription = "")
                }
            }
        }

        Box(
            modifier = Modifier
                .background(Color.White)
                .fillMaxWidth()
                .height(100.dp)
                .constrainAs(buttonBackground) {
                    bottom.linkTo(parent.bottom)
                    top.linkTo(addNewPlayerButton.top)
                })
        Button(onClick = { onAddNewPlayer(null) },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .constrainAs(addNewPlayerButton) {
                    absoluteLeft.linkTo(parent.absoluteLeft)
                    absoluteRight.linkTo(parent.absoluteRight)
                    bottom.linkTo(parent.bottom, margin = 20.dp)
                }) {
            Text(text = "Add New Player")
        }

    }

}