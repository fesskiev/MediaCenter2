package com.fesskiev.mediacenter.ui.media.video.details

import android.os.Bundle
import com.fesskiev.mediacenter.R
import com.fesskiev.mediacenter.domain.entity.media.VideoFolder
import com.fesskiev.mediacenter.utils.Constants.Companion.EXTRA_VIDEO_FOLDER
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_video_files.*
import javax.inject.Inject

class VideoFilesActivity : DaggerAppCompatActivity(), VideoFilesContract.View {

    @Inject
    @JvmField
    var presenter: VideoFilesPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_files)
        setupToolbar()
        val videoFolder: VideoFolder = intent.extras.getParcelable(EXTRA_VIDEO_FOLDER)
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun showProgressBar() {

    }

    override fun hideProgressBar() {

    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.detach()
    }
}