package com.dave_devs.permissionhandlingsinglemultiplerequest.permission_handling_with_normalFrameWork

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    val visiblePermissionDialogQueue = mutableStateListOf<String>()

    //Function to dismiss a dialog
    fun dismissDialog() {
        visiblePermissionDialogQueue.removeFirst()
    }

    //Function for permission result (granted || !granted)
    fun onPermissionResult(
        permission: String,
        isGranted: Boolean
    ) {
        if(!isGranted && !visiblePermissionDialogQueue.contains(permission)) {
            visiblePermissionDialogQueue.add(permission)
        }
    }
}