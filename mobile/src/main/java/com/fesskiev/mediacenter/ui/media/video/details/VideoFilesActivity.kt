package com.fesskiev.mediacenter.ui.media.video.details

import android.os.Bundle
import com.fesskiev.mediacenter.domain.entity.media.VideoFolder
import com.fesskiev.mediacenter.utils.Constants.Companion.EXTRA_VIDEO_FOLDER
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject


class VideoFilesActivity : DaggerAppCompatActivity(), VideoFilesContract.View {

    @Inject
    @JvmField
    var presenter: VideoFilesPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val videoFolder : VideoFolder = intent.extras.getParcelable(EXTRA_VIDEO_FOLDER)
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