package com.fesskiev.mediacenter.ui.media.audio.details

import android.os.Bundle
import com.fesskiev.mediacenter.domain.entity.media.AudioFolder
import com.fesskiev.mediacenter.utils.Constants.Companion.EXTRA_AUDIO_FOLDER
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject


class AudioFilesActivity : DaggerAppCompatActivity(), AudioFilesContract.View {

    @Inject
    @JvmField
    var presenter: AudioFilesPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val audioFolder : AudioFolder = intent.extras.getParcelable(EXTRA_AUDIO_FOLDER)
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