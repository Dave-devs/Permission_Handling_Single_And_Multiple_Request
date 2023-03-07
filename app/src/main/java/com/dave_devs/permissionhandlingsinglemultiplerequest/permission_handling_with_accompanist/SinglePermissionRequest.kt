package com.dave_devs.permissionhandlingsinglemultiplerequest.permission_handling_with_accompanist

import android.Manifest
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SinglePermissionRequest() {
    val permissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)
    //The CompositionLocal containing the current LifecycleOwner (A class that has an Android lifecycle).
    val lifecycleOwner = LocalLifecycleOwner.current

    /*A side effect of composition that must run
    for any new unique value of key1 and must be
    reversed or cleaned up if key1 changes or if
    the DisposableEffect leaves the composition.*/
    DisposableEffect(key1 = lifecycleOwner, effect = {
        //LifecycleEventObserver is a class that can receive any lifecycle change and dispatch it to the receiver.
        val observer = LifecycleEventObserver { _, event ->
            when(event) {
                Lifecycle.Event.ON_START -> {
                    permissionState.launchPermissionRequest()
                }
                else -> return@LifecycleEventObserver
            }
        }
        //Call to add an observer(effect) to the DisposableEffect block after the key change.
        lifecycleOwner.lifecycle.addObserver(observer)
        //Call to remove a key from DisposableEffect block.
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    })

    if( permissionState.status.isGranted) {
        Text("Camera permission granted")
    } else {
        Column {
            val textToShow = if(permissionState.status.shouldShowRationale) {
                "This app needs access to your camera to function properly."
            } else {
                "It seems you permanently declined camera permission." +
                        "You can change this in app settings"
            }
            Text(text = textToShow)
        }
    }
}