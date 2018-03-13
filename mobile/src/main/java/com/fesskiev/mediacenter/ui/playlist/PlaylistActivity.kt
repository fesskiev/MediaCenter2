package com.fesskiev.mediacenter.ui.playlist

import android.os.Bundle
import com.fesskiev.mediacenter.R
import com.fesskiev.mediacenter.domain.entity.media.MediaFile
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject


class PlaylistActivity : DaggerAppCompatActivity(), PlaylistContract.View {

    @Inject
    @JvmField
    var presenter: PlaylistPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist)
    }

    override fun onResume() {
        super.onResume()
        presenter?.getPlaylist()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.detach()
    }

    override fun showPlaylist(mediaFiles: List<MediaFile>) {

    }

    override fun showEmptyPlaylist() {

    }

    override fun showProgressBar() {

    }

    override fun hideProgressBar() {

    }
}