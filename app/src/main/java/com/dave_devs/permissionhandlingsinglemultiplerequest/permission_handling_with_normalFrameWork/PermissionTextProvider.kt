package com.dave_devs.permissionhandlingsinglemultiplerequest.permission_handling_with_normalFrameWork


interface PermissionTextProvider {
    fun getDescription(isPermanentlyDeclined: Boolean): String
}

class CameraPermissionTextProvider: PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if(isPermanentlyDeclined) {
            "It seems you permanently declined camera permission." + 
                    "You can change this in app settings"
        } else {
            "This app needs access to your camera to function properly."
        }
    }
}

class PhoneCallPermissionTextProvider: PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if(isPermanentlyDeclined) {
            "It seems you permanently declined phone calling permission." +
                    "You can change this in app settings"
        } else {
            "This app needs access to your phone calling to function properly."
        }
    }
}

class RecordAudioPermissionTextProvider: PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if(isPermanentlyDeclined) {
            "It seems you permanently declined microphone permission." +
                    "You can change this in app settings"
        } else {
            "This app needs access to your microphone to function properly."
        }
    }
}