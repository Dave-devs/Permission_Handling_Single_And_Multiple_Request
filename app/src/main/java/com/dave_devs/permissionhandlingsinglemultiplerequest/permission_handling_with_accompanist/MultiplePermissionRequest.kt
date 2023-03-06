package com.dave_devs.permissionhandlingsinglemultiplerequest.permission_handling_with_accompanist

import android.Manifest
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MultiplePermissionRequest() {
    val permissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CALL_PHONE
        )
    )
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(key1 = lifecycleOwner, effect = {
        val observer = LifecycleEventObserver { _, event ->
            when(event) {
                Lifecycle.Event.ON_START -> {
                    permissionState.launchMultiplePermissionRequest()
                }
                else -> return@LifecycleEventObserver
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    })

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        permissionState.permissions.forEach{ perms ->
            when(perms.permission) {
                Manifest.permission.RECORD_AUDIO -> {
                    if( perms.status.isGranted) {
                        Text("Microphone permission granted")
                    } else {
                        Column {
                            val textToShow = if(perms.status.shouldShowRationale) {
                                "This app needs access to your microphone to function properly."
                            } else {
                                "It seems you permanently declined microphone permission." +
                                        "You can change this in app settings"
                            }
                            Text(text = textToShow)
                        }
                    }
                }
                Manifest.permission.CALL_PHONE -> {
                    if( perms.status.isGranted) {
                        Text("Calling permission granted")
                    } else {
                        Column {
                            val textToShow = if(perms.status.shouldShowRationale) {
                                "This app needs access to your phone calling to function properly."
                            } else {
                                "It seems you permanently declined phone calling permission." +
                                        "You can change this in app settings"
                            }
                            Text(text = textToShow)
                        }
                    }
                }
            }
        }
    }
}