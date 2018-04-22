package com.fesskiev.mediacenter.utils

import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.view.Gravity
import android.widget.TextView
import com.fesskiev.mediacenter.R
import java.io.File
import java.io.FilenameFilter


fun ViewGroup.inflate(layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.GONE
}


fun AppCompatActivity.setupToolbar(toolbar: Toolbar) {
    setSupportActionBar(toolbar)
    supportActionBar?.apply {
        setDisplayHomeAsUpEnabled(true)
        setDisplayShowTitleEnabled(false)
    }
}

fun AppCompatActivity.showToast(resId: Int) {
    val toast = Toast.makeText(this, resId, Toast.LENGTH_LONG)
    val toastView = toast.view
    val toastMessage = toastView.findViewById(android.R.id.message) as TextView
    toastMessage.textSize = 14f
    toastMessage.setTextColor(resources.getColor(R.color.white))
    toastMessage.gravity = Gravity.CENTER
    toastView.setBackgroundResource(R.drawable.bg_rounded)
    toast.show()
}


fun Fragment.showToast(resId: Int) {
    val toast = Toast.makeText(context, resId, Toast.LENGTH_LONG)
    val toastView = toast.view
    val toastMessage = toastView.findViewById(android.R.id.message) as TextView
    toastMessage.textSize = 14f
    toastMessage.setTextColor(resources.getColor(R.color.white))
    toastMessage.gravity = Gravity.CENTER
    toastView.setBackgroundResource(R.drawable.bg_rounded)
    toast.show()
}

fun AppCompatActivity.openActivity(clazz: Class<*>) {
    startActivity(Intent(this, clazz))
}

fun isPointInsideView(x: Float, y: Float, view: View): Boolean {
    val location = IntArray(2)
    view.getLocationOnScreen(location)
    val viewX = location[0]
    val viewY = location[1]
    return x > viewX && x < viewX + view.width && y > viewY && y < viewY + view.height
}

fun filterAudioFilesInFolder(dir: File): Array<File> {
    return dir.listFiles(audioFilter())
}

fun filterVideoFilesInFolder(dir: File): Array<File> {
    return dir.listFiles(videoFilter())
}

fun filterImagesInFolder(dir: File): Array<File> {
    return dir.listFiles(folderImageFilter())
}

private fun folderImageFilter(): FilenameFilter {
    return FilenameFilter { dir, name ->
        val lowercaseName = name.toLowerCase()
        lowercaseName.endsWith(".png") || lowercaseName.endsWith(".jpg")
    }
}

private fun audioFilter(): FilenameFilter {
    return FilenameFilter { dir, name ->
        val lowercaseName = name.toLowerCase()
        (lowercaseName.endsWith(".mp3")
                || lowercaseName.endsWith(".flac")
                || lowercaseName.endsWith(".wav")
                || lowercaseName.endsWith(".m4a")
                || lowercaseName.endsWith(".aac")
                || lowercaseName.endsWith(".aiff"))
    }
}

private fun videoFilter(): FilenameFilter {
    return FilenameFilter { dir, name ->
        val lowercaseName = name.toLowerCase()
        lowercaseName.endsWith(".mp4")
                || lowercaseName.endsWith(".ts")
                || lowercaseName.endsWith(".mkv")
    }
}