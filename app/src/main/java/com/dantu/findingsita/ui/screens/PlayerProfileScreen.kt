package com.dantu.findingsita.ui.screens

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import coil.compose.rememberAsyncImagePainter
import com.dantu.findingsita.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import java.io.File

@Serializable
data class AddOrEditPlayer(var playerId: Int)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerProfileScreen(
    playerId: Int,
    onSaveClicked: () -> Unit,
    onCancel: () -> Unit,
    onDelete: () -> Unit
) {
    val context = LocalContext.current
    val viewModel: PlayerProfileViewModel = hiltViewModel()
    val scope = rememberCoroutineScope()
    var playerName by rememberSaveable {
        mutableStateOf("")
    }
    var playerPassword by rememberSaveable {
        mutableStateOf("")
    }
    var profilePictureUri: String? by rememberSaveable {
        mutableStateOf(null)
    }

    var showBottomSheet by remember {
        mutableStateOf(false)
    }
    val authority = stringResource(id = R.string.fileprovider)

    val tempUri = remember { mutableStateOf<Uri?>(null) }

    var sheetState = rememberModalBottomSheetState()

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission is granted, proceed to step 2

        } else {
            // Permission is denied, handle it accordingly
        }
    }

    // for takePhotoLauncher used
    fun getTempUri(): Uri? {
        context.cacheDir?.let {
            it.mkdirs()
            val file = File.createTempFile(
                "image_" + System.currentTimeMillis().toString(),
                ".jpg",
                it
            )

            return FileProvider.getUriForFile(
                context,
                authority,
                file
            )
        }
        return null
    }

    val takePhotoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = {isSaved ->
            tempUri.value?.let {
//                onSetUri.invoke(it)
                profilePictureUri = it.toString()
                scope.launch {
                    showBottomSheet = false
                    sheetState.hide()
                }
            }
        }
    )

    val getContent = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            //Permissions to read the uri
            context.contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            profilePictureUri = it.toString()
            scope.launch {
                showBottomSheet = false
                sheetState.hide()
            }
            Log.d("ProfileIamge", "Profile Image " + profilePictureUri)
        }
    }

    LaunchedEffect(scope) {
        withContext(Dispatchers.IO) {
            if (playerId > 0) {
                viewModel.getPlayer(playerId)
                viewModel.playerData.collect {
                    it?.let {
                        playerName = it.name
                        playerPassword = it.pin.toString()
                        profilePictureUri = it.profilePic
                    }
                }
            }
        }
    }
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        val focusManager = LocalFocusManager.current
        val keyboardController = LocalSoftwareKeyboardController.current
        val (nameTitle, nameField, passwordTitle, passwordField) = createRefs()
        val saveButton = createRef()
        val cancelButton = createRef()
        val deletePlayerButton = createRef()
        val profilePicture = createRef()
        val painter = if (profilePictureUri == null) {
            painterResource(id = R.drawable.profile_place_holder)
        } else {
            rememberAsyncImagePainter(model = profilePictureUri)
        }
        Image(painter = painter,
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .clickable {
//                    getContent.launch("image/*")
                    showBottomSheet = true
                }
                .constrainAs(profilePicture) {
                    top.linkTo(parent.top)
                    absoluteLeft.linkTo(parent.absoluteLeft)
                    absoluteRight.linkTo(parent.absoluteRight)
                })
        Text(text = "Name", modifier = Modifier.constrainAs(nameTitle) {
            absoluteLeft.linkTo(parent.absoluteLeft, margin = 20.dp)
            top.linkTo(profilePicture.bottom, margin = 20.dp)
        })
        OutlinedTextField(value = playerName,
            onValueChange = {
                if (it.length < 8) {
                    playerName = it
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = {
                focusManager.moveFocus(FocusDirection.Down)
            }),
            isError = playerName.isBlank() || playerName.trim().length < 4,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .constrainAs(nameField) {
                    top.linkTo(nameTitle.bottom, margin = 10.dp)
                    absoluteLeft.linkTo(nameTitle.absoluteLeft)
                })
        Text(text = "Password", modifier = Modifier
            .constrainAs(passwordTitle) {
                absoluteLeft.linkTo(nameField.absoluteLeft)
                top.linkTo(nameField.bottom, margin = 20.dp)
            })
        OutlinedTextField(value = playerPassword,
            onValueChange = {
                if (it.length < 5) {
                    playerPassword = it
                }

            },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword,
                imeAction = ImeAction.Done
            ),
            isError = playerName.isBlank() || playerPassword.trim().length < 4,
            keyboardActions = KeyboardActions(onDone = {
                if (playerPassword.length == 4 && playerName.isNotBlank()) {
                    keyboardController?.hide()
                    viewModel.viewModelScope.launch {
                        viewModel.createOrUpdatePlayer(
                            playerId,
                            playerName,
                            playerPassword.toInt(),
                            profilePictureUri
                        )
                    }
                    onSaveClicked()
                }
            }),
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .constrainAs(passwordField) {
                    top.linkTo(passwordTitle.bottom, margin = 10.dp)
                    absoluteLeft.linkTo(passwordTitle.absoluteLeft)
                })
        Button(onClick = {
            if (playerPassword.length == 4 && playerName.isNotBlank()) {
                keyboardController?.hide()
                viewModel.viewModelScope.launch {
                    viewModel.createOrUpdatePlayer(
                        playerId,
                        playerName,
                        playerPassword.toInt(),
                        profilePictureUri
                    )
                }
                onSaveClicked()
            }
        },
            modifier = Modifier.constrainAs(saveButton) {
                absoluteLeft.linkTo(parent.absoluteLeft)
                absoluteRight.linkTo(parent.absoluteRight)
                top.linkTo(passwordField.bottom, margin = 20.dp)
            }) {
            Text(text = if (playerId > 0) "Save" else "Add")
        }
        Button(onClick = {
            keyboardController?.hide()
            onCancel()
        },
            modifier = Modifier.constrainAs(cancelButton) {
                absoluteLeft.linkTo(saveButton.absoluteRight, margin = 5.dp)
                absoluteRight.linkTo(parent.absoluteRight)
                top.linkTo(passwordField.bottom, margin = 20.dp)
            }) {
            Text(text = "Cancel")
        }
        if (playerId > 0) {
            Button(onClick = {
                viewModel.deletePlayer(playerId)
                onDelete()
            },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .constrainAs(deletePlayerButton) {
                        top.linkTo(saveButton.bottom, margin = 10.dp)
                        absoluteLeft.linkTo(parent.absoluteLeft)
                        absoluteRight.linkTo(parent.absoluteRight)
                    }) {
                Text(text = "Delete")
            }
        }
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState
            ) {
                Column {
                    Text(text = "Pick Image or Take Photo")
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = {
                                val permission = Manifest.permission.CAMERA
                                if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
                                ) {
                                    // Permission is already granted, proceed to step 2
                                    getTempUri()?.let {
                                        tempUri.value = it
                                        takePhotoLauncher.launch(tempUri.value!!)
                                    }
                                } else {
                                    // Permission is not granted, request it
                                    cameraPermissionLauncher.launch(permission)
                                }
                            },
                            contentPadding = ButtonDefaults.ButtonWithIconContentPadding
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.capture_photo),
                                contentDescription = "",
                                modifier = Modifier.size(ButtonDefaults.IconSize)
                            )
                            Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                            Text(text = "Capture")
                        }
                        Button(
                            onClick = {  getContent.launch("image/*") },
                            contentPadding = ButtonDefaults.ButtonWithIconContentPadding
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.photo_picker),
                                contentDescription = "",
                                modifier = Modifier.size(ButtonDefaults.IconSize)
                            )
                            Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                            Text(text = "Pick From Gallery")
                        }
                    }
                }
            }
        }
    }
}