package com.fesskiev.mediacenter.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat

open class PermissionsUtils {

    companion object {
        const val PERMISSION_RECORD = 1
        const val PERMISSION_STORAGE = 2
    }

    fun checkPermissionsStorage(activity: Activity): Boolean {
        return if(PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            true
        } else {
            requestPermissionsStorage(activity)
            false
        }
    }

    fun checkPermissionsRecords(activity: Activity): Boolean {
        return if(PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(activity, Manifest.permission.MODIFY_AUDIO_SETTINGS)
                && PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO)){
            true
        } else {
            requestPermissionsRecords(activity)
            false
        }
    }

    fun requestPermissionsRecords(activity: Activity) {
        ActivityCompat.requestPermissions(activity,
                arrayOf(Manifest.permission.MODIFY_AUDIO_SETTINGS, Manifest.permission.RECORD_AUDIO),
                PERMISSION_RECORD)
    }

    fun requestPermissionsStorage(activity: Activity) {
        ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                PERMISSION_STORAGE)
    }

    fun checkPermissionsResultGranted(grantResults: IntArray): Boolean {
        grantResults.forEach { result ->
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    fun shouldShowRequestStoragePermissionRationale(activity: Activity): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }
}