package com.fesskiev.engine

class SuperpoweredEngine(private val recordPath: String,
                         private val sampleRate: Int,
                         private val bufferSize: Int) {

    private var duration: Int = 0
    private var position: Int = 0
    private var positionPercent: Float = 0.toFloat()
    private var volume: Float = 0.toFloat()
    private var playing: Boolean = false
    private var looping: Boolean = false
    private var enableEQ: Boolean = false
    private var enableReverb: Boolean = false
    private var enableEcho: Boolean = false
    private var enableWhoosh: Boolean = false

    companion object {
        init {
            System.loadLibrary("SuperpoweredEngine")
        }
    }

    fun createAudioPlayer(){
        createAudioPlayer(sampleRate, bufferSize, recordPath)
        registerCallback()
    }

    external fun updatePlaybackState()

    external fun onDestroyAudioPlayer()

    external fun onBackground()

    external fun onForeground()

    external fun registerCallback()

    external fun unregisterCallback()

    external fun createAudioPlayer(sampleRate: Int, bufferSize: Int, recorderTempPath: String)

    external fun openAudioFile(path: String)

    external fun togglePlayback()

    external fun setVolume(value: Float)

    external fun setSeek(value: Int)

    external fun setPosition(value: Int)

    external fun setLooping(isLooping: Boolean)

    external fun setTempo(tempo: Double)

    external fun setPitchShift(pitchShift: Int)

    /***
     * EQ methods
     */
    external fun enableEQ(enable: Boolean)

    external fun setEQBands(band: Int, value: Int)

    /***
     * Reverb
     */
    external fun setReverbValue(mix: Int, width: Int, damp: Int, roomSize: Int)

    external fun enableReverb(enable: Boolean)

    /***
     * Echo
     */
    external fun setEchoValue(value: Int)

    external fun enableEcho(enable: Boolean)

    /***
     * Whoosh!
     */
    external fun setWhooshValue(wet: Int, frequency: Int)

    external fun enableWhoosh(enable: Boolean)

    /***
     * Recording
     */
    external fun startRecording(destinationPath: String)

    external fun stopRecording()


    /**
     * looping
     */
    external fun loopBetween(startMs: Double, endMs: Double)

    external fun loopExit()


    fun playStatusCallback(status: Int) {

    }
}