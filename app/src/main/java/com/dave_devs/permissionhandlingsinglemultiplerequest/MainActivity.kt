package com.dave_devs.permissionhandlingsinglemultiplerequest

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dave_devs.permissionhandlingsinglemultiplerequest.permission_handling_with_normalFrameWork.*
import com.dave_devs.permissionhandlingsinglemultiplerequest.ui.theme.PermissionHandlingSingleMultipleRequestTheme

class MainActivity : ComponentActivity() {
    private val permissionsToRequest = arrayOf(
        Manifest.permission.CALL_PHONE,
        Manifest.permission.RECORD_AUDIO
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PermissionHandlingSingleMultipleRequestTheme {
                //These following commented calls are for permission request with Google Accompanist
                //SinglePermissionRequest()
                //MultiplePermissionRequest()

                val viewModel = viewModel<MainViewModel>()
                val dialogQueue = viewModel.visiblePermissionDialogQueue

                //Single Launcher
                val cameraPermissionResultLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission(),
                    onResult = { isGranted ->
                        viewModel.onPermissionResult(
                            permission = Manifest.permission.CAMERA,
                            isGranted = isGranted
                        )
                    }
                )
                //Multiple Launcher
                val multiplePermissionResultLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestMultiplePermissions(),
                    onResult = { perms->
                        permissionsToRequest.forEach { permission ->
                            viewModel.onPermissionResult(
                                permission = permission,
                                isGranted = perms[permission] == true
                            )
                        }
                    }
                )

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(onClick = {
                        cameraPermissionResultLauncher.launch(
                            Manifest.permission.CAMERA
                        )
                    }) { Text(text = "Grant single permission") }
                    Spacer(Modifier.height(16.dp))
                    Button(onClick = {
                        multiplePermissionResultLauncher.launch(
                            permissionsToRequest
                        )
                    }) { Text(text = "Grant multiple permission") }
                }

                dialogQueue.reversed().forEach{ permission ->
                    PermissionDialog(
                        PermissionTextProvider = when (permission) {
                             Manifest.permission.CAMERA -> {
                                 CameraPermissionTextProvider()
                             }
                             Manifest.permission.CALL_PHONE ->  {
                                 PhoneCallPermissionTextProvider()
                             }
                             Manifest.permission.RECORD_AUDIO -> {
                                 RecordAudioPermissionTextProvider()
                             }
                            else -> return@forEach
                        },
                        isPermanentlyDeclined = !shouldShowRequestPermissionRationale(
                            permission
                        ),
                        onDismiss = viewModel::dismissDialog,
                        onOkayClick = {
                            viewModel.dismissDialog()
                            multiplePermissionResultLauncher.launch(
                                arrayOf(permission)
                            )
                        },
                        onGoToAppSettingsClick = ::openAppSettings
                    )
                }
            }
        }
    }
}

fun Activity.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}