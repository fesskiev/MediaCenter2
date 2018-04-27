package com.fesskiev.mediacenter.ui.settings

import android.os.Bundle
import com.fesskiev.mediacenter.R
import dagger.android.support.DaggerAppCompatActivity

class SettingsActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
    }
}