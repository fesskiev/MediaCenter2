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
import android.widget.FrameLayout
import android.widget.TextView
import com.fesskiev.mediacenter.R


fun ViewGroup.inflate(layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
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