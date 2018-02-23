package com.fesskiev.engine

class FFmpegEngine {

    companion object {
        init {
            System.loadLibrary("FFmpegEngine")
        }
    }

    @Throws(IllegalArgumentException::class)
    external fun extractFileMetadata(path: String)

    @Throws(IllegalArgumentException::class)
    external fun executeCommand(command: String)
}