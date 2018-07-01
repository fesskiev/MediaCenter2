package com.fesskiev.mediacenter

import com.fesskiev.engine.FFmpegEngine
import org.junit.After
import org.junit.Test

import org.junit.Before


class FFmpegRetrieverTest {

    var ffmpegEngine: FFmpegEngine? = null

    @Before
    fun setup() {
        ffmpegEngine = FFmpegEngine()
    }

    @After
    fun release() {

    }

    @Test
    fun fetchMetadata() {
        ffmpegEngine?.extractFileMetadata("/storage/emulated/0/Video/test.mp4")
    }
}
