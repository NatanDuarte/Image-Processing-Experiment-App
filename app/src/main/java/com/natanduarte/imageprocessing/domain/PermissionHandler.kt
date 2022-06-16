package com.natanduarte.imageprocessing.domain

import android.Manifest
import android.app.Activity
import androidx.core.app.ActivityCompat

class PermissionHandler(private var activity: Activity) {

    private var requestCode: Int = 100

    private val permissions: Array<String> = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    fun writePermissions() {
        for (permission in permissions) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(permission),
                requestCode
            )
        }
    }
}