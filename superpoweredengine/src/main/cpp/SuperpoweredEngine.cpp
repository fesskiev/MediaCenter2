#include "SuperpoweredEngine.h"
#include "SuperpoweredSimple.h"
#include <SuperpoweredCPU.h>
#include <stdlib.h>
#include <jni.h>
#include <android/log.h>
#include <SLES/OpenSLES.h>
#include <SLES/OpenSLES_AndroidConfiguration.h>

#define MINFREQ 20.0f
#define MAXFREQ 20000.0f

static SuperpoweredPlayer *player = NULL;
JavaVM *gJavaVM;
jobject callbackObject = NULL;

static inline float floatToFrequency(float value) {
    if (value > 0.97f) return MAXFREQ;
    if (value < 0.03f) return MINFREQ;
    value = powf(10.0f,
                 (value + ((0.4f - fabsf(value - 0.4f)) * 0.3f)) * log10f(MAXFREQ - MINFREQ)) +
            MINFREQ;
    return value < MAXFREQ ? value : MAXFREQ;
}

static void handlingCallback(int event) {
    JNIEnv *env;
    int isAttached = 0;

    if (!callbackObject) {
        return;
    }

    if ((gJavaVM->GetEnv((void **) &env, JNI_VERSION_1_6)) < 0) {
        if ((gJavaVM->AttachCurrentThread(&env, NULL)) < 0) {
            return;
        }
        isAttached = 1;
    }

    jclass cls = env->GetObjectClass(callbackObject);
    if (!cls) {
        if (isAttached) {
            gJavaVM->DetachCurrentThread();
        }
        return;
    }

    jmethodID method = env->GetMethodID(cls, "playStatusCallback", "(I)V");
    if (!method) {
        if (isAttached) {
            gJavaVM->DetachCurrentThread();
        }
        return;
    }

    env->CallVoidMethod(callbackObject, method, event);

    if (isAttached) {
        gJavaVM->DetachCurrentThread();
    }
}

static void playerEventCallback(void *clientData, SuperpoweredAdvancedAudioPlayerEvent event,
                                void *__unused value) {

    SuperpoweredAdvancedAudioPlayer *player = *((SuperpoweredAdvancedAudioPlayer **) clientData);
    switch (event) {
        case SuperpoweredAdvancedAudioPlayerEvent_LoadSuccess:
            __android_log_print(ANDROID_LOG_DEBUG, "MediaCenter", "LOAD SUCCESS");
            handlingCallback(3);
            break;
        case SuperpoweredAdvancedAudioPlayerEvent_LoadError:
            __android_log_print(ANDROID_LOG_DEBUG, "MediaCenter", "Open error: %s", (char *) value);
            player->playing = false;
            handlingCallback(2);
            break;
        case SuperpoweredAdvancedAudioPlayerEvent_EOF:
            __android_log_print(ANDROID_LOG_DEBUG, "MediaCenter", "END SONG");
            player->seek(0);
            player->togglePlayback();
            handlingCallback(1);
            break;
        default:;
    };
}

static bool audioProcessing(void *clientdata, short int *audioIO, int numberOfSamples,
                            int __unused samplerate) {
    //  __android_log_print(ANDROID_LOG_VERBOSE, "MediaCenter", "audioProcessing");

    return ((SuperpoweredPlayer *) clientdata)->process(audioIO, (unsigned int) numberOfSamples);
}

SuperpoweredPlayer::SuperpoweredPlayer(unsigned int samplerate, unsigned int buffersize,
                                       const char *recorderTempPath) : volume(
        1.0f) {

    buffer = (float *) memalign(16, (buffersize + 16) * sizeof(float) * 2);
    bufferRecording = (float *) memalign(16, (buffersize + 16) * sizeof(float) * 2);

    player = new SuperpoweredAdvancedAudioPlayer(&player, playerEventCallback, samplerate, 0);
    player->syncMode = SuperpoweredAdvancedAudioPlayerSyncMode_TempoAndBeat;

    audioSystem = new SuperpoweredAndroidAudioIO(samplerate, buffersize, true, true,
                                                 audioProcessing, this, -1, SL_ANDROID_STREAM_MEDIA,
                                                 buffersize * 2);

    mixer = new SuperpoweredStereoMixer();

    left = 1.0f;
    right = 1.0f;

    bandEQ = new Superpowered3BandEQ(samplerate);
    echo = new SuperpoweredEcho(samplerate);
    reverb = new SuperpoweredReverb(samplerate);
    gate = new SuperpoweredGate(samplerate);
    whoosh = new SuperpoweredWhoosh(samplerate);

    recorder = new SuperpoweredRecorder(recorderTempPath, samplerate);
}

bool SuperpoweredPlayer::process(short int *inputOutput, unsigned int numberOfSamples) {

    bool silence = !player->process(buffer, false, numberOfSamples, volume);
    if (!silence) {
        if (bandEQ->enabled) {
            bandEQ->process(buffer, buffer, numberOfSamples);
        }

        if (reverb->enabled) {
            reverb->process(buffer, buffer, numberOfSamples);
        }

        if (echo->enabled) {
            echo->process(buffer, buffer, numberOfSamples);
        }

        if (whoosh->enabled) {
            whoosh->process(buffer, buffer, numberOfSamples);
        }

        SuperpoweredFloatToShortInt(buffer, inputOutput, numberOfSamples);
    }

    if (record) {
        __android_log_print(ANDROID_LOG_VERBOSE, "SuperpoweredExample", "recording....");

        SuperpoweredShortIntToFloat(inputOutput, bufferRecording, numberOfSamples);
        recorder->process(bufferRecording, NULL, numberOfSamples);
        return true;
    }

    return !silence;
}

void SuperpoweredPlayer::togglePlayback() {
    player->togglePlayback();

//    SuperpoweredCPU::setSustainedPerformanceMode(player->playing);
}


SuperpoweredPlayer::~SuperpoweredPlayer() {

    delete echo;
    delete reverb;
    delete gate;
    delete whoosh;
    delete bandEQ;
    delete mixer;
    delete recorder;
    delete audioSystem;
    delete player;
    free(buffer);
    free(bufferRecording);
}

void SuperpoweredPlayer::setVolume(float value) {
    volume = value * 0.01f;
}

void SuperpoweredPlayer::setSeek(int value) {
    player->seek(value * 0.01);
}

unsigned int SuperpoweredPlayer::getDuration() {
    return player->durationSeconds;
}

unsigned int SuperpoweredPlayer::getPosition() {
    return player->positionSeconds;
}

float SuperpoweredPlayer::getPositionPercent() {
    return player->positionPercent;
}


bool SuperpoweredPlayer::isPlaying() {
    return player->playing;
}

void SuperpoweredPlayer::setLooping(bool looping) {
    player->looping = looping;
}

void SuperpoweredPlayer::open(const char *path) {
    player->open(path);
}

void SuperpoweredPlayer::setEQBands(int index, int value) {
    if (index < 0 || index > 2) {
        return;
    }
    if (value == 0) {
        value = 49;
    }
    float bandF = 1.0f;
    if (value < 50) {
        bandF = (float) value / 50.0f;
    } else if (value > 50) {
        bandF = 1.0f + (float) (value - 50) / 7.0f;
    }
    __android_log_print(ANDROID_LOG_VERBOSE, "MediaCenter", "setEQBands index = %i bandF = %f",
                        index, bandF);
    bandEQ->bands[index] = bandF;
}

void SuperpoweredPlayer::enableEQ(bool enable) {
    bandEQ->enable(enable);
}

void SuperpoweredPlayer::onBackground() {
    audioSystem->onBackground();
}

void SuperpoweredPlayer::onForeground() {
    audioSystem->onForeground();
}

float SuperpoweredPlayer::getVolume() {
    return volume;
}

bool SuperpoweredPlayer::isLooping() {
    return player->looping;
}

bool SuperpoweredPlayer::isEnableEQ() {
    return bandEQ->enabled;
}

void SuperpoweredPlayer::echoValue(int value) {
    float mixValue = float(value) * 0.01f;
    echo->setMix(mixValue);

}

void SuperpoweredPlayer::enableEcho(bool enable) {
    echo->enable(enable);
}

bool SuperpoweredPlayer::isEnableEcho() {
    return echo->enabled;
}

void SuperpoweredPlayer::reverbValue(int mix, int width, int damp, int roomSize) {
    float mixF = float(mix) * 0.01f;
    float widthF = float(width) * 0.01f;
    float dampF = float(damp) * 0.01f;
    float roomSizeF = float(roomSize) * 0.01f;
    __android_log_print(ANDROID_LOG_VERBOSE, "MediaCenter",
                        "setReverbValue mixF = %f width = %f damp = %f, roomSize = %f",
                        mixF, widthF, dampF, roomSizeF);
    reverb->setMix(mixF);
    reverb->setWidth(widthF);
    reverb->setDamp(dampF);
    reverb->setRoomSize(roomSizeF);
}

void SuperpoweredPlayer::enableReverb(bool enable) {
    reverb->enable(enable);
}

bool SuperpoweredPlayer::isEnableReverb() {
    return reverb->enabled;
}

void SuperpoweredPlayer::whooshValue(int wet, int frequency) {
    float wetF = float(wet) * 0.01f;
    float frequencyF = float(frequency) * 0.01f;
    frequencyF = floatToFrequency(frequencyF);
    __android_log_print(ANDROID_LOG_VERBOSE, "SuperpoweredExample",
                        "setWhooshValue wet = %f frequency = %f",
                        wetF, frequencyF);
    whoosh->wet = wetF;
    whoosh->setFrequency(frequencyF);
}


void SuperpoweredPlayer::enableWhoosh(bool enable) {
    whoosh->enable(enable);
}

bool SuperpoweredPlayer::isEnableWhoosh() {
    return whoosh->enabled;
}

void SuperpoweredPlayer::startRecording(const char *destinationPath) {
    record = true;
    recorder->start(destinationPath);

}

void SuperpoweredPlayer::stopRecording() {
    record = false;
    recorder->stop();
}

void SuperpoweredPlayer::setTempo(double value) {

    double tempo = value / 50.0f;
    __android_log_print(ANDROID_LOG_VERBOSE, "MediaCenter", "setTempo: tempo = %f", tempo);

    player->setTempo(tempo, false);
}

void SuperpoweredPlayer::setPitchShift(int value) {

    double pitchShift = (value - 50) * (12 / 50.0f);
    __android_log_print(ANDROID_LOG_VERBOSE, "MediaCenter", "setPitchShift: pitch = %i",
                        (int) pitchShift);

    player->setPitchShift((int) pitchShift);
}

void SuperpoweredPlayer::setPosition(int value) {
    player->setPosition(value * 1000, false, false);
}

void SuperpoweredPlayer::loopExit() {
    player->exitLoop();

}

void SuperpoweredPlayer::loopBetween(double startMs, double endMs) {
    player->loopBetween(startMs, endMs, true, 255, false);
}


JNIEXPORT jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    gJavaVM = vm;
    return JNI_VERSION_1_6;
}


static inline void setFloatField(JNIEnv *javaEnvironment, jobject obj, jclass thisClass,
                                 const char *name, float value) {
    javaEnvironment->SetFloatField(obj, javaEnvironment->GetFieldID(thisClass, name, "F"), value);
}

static inline void setIntField(JNIEnv *javaEnvironment, jobject obj, jclass thisClass,
                               const char *name, unsigned int value) {
    javaEnvironment->SetIntField(obj, javaEnvironment->GetFieldID(thisClass, name, "I"), value);
}

static inline void setBoolField(JNIEnv *javaEnvironment, jobject obj, jclass thisClass,
                                const char *name, bool value) {
    javaEnvironment->SetBooleanField(obj, javaEnvironment->GetFieldID(thisClass, name, "Z"),
                                     (jboolean) value);
}

extern "C" JNIEXPORT void
Java_com_fesskiev_engine_SuperpoweredEngine_createAudioPlayer(JNIEnv *env,
                                                              jobject instance,
                                                              jint sampleRate,
                                                              jint bufferSize,
                                                              jstring recordTempPath) {
    const char *str = env->GetStringUTFChars(recordTempPath, 0);

    player = new SuperpoweredPlayer((unsigned int) sampleRate, (unsigned int) bufferSize, str);

    env->ReleaseStringUTFChars(recordTempPath, str);
}

extern "C" JNIEXPORT void
Java_com_fesskiev_engine_SuperpoweredEngine_onDestroyAudioPlayer(JNIEnv *env,
                                                                 jobject instance) {
    if (player != NULL) {
        player->~SuperpoweredPlayer();
    }
    __android_log_print(ANDROID_LOG_DEBUG, "MediaCenter", "DESTROY");
}

extern "C" JNIEXPORT void
Java_com_fesskiev_engine_SuperpoweredEngine_updatePlaybackState(JNIEnv *javaEnvironment,
                                                                jobject obj) {
    jclass thisClass = javaEnvironment->GetObjectClass(obj);

    setIntField(javaEnvironment, obj, thisClass, "duration", player->getDuration());
    setIntField(javaEnvironment, obj, thisClass, "position", player->getPosition());
    setFloatField(javaEnvironment, obj, thisClass, "volume", player->getVolume());
    setFloatField(javaEnvironment, obj, thisClass, "positionPercent", player->getPositionPercent());
    setBoolField(javaEnvironment, obj, thisClass, "playing", player->isPlaying());
    setBoolField(javaEnvironment, obj, thisClass, "looping", player->isLooping());
    setBoolField(javaEnvironment, obj, thisClass, "enableEQ", player->isEnableEQ());
    setBoolField(javaEnvironment, obj, thisClass, "enableReverb", player->isEnableReverb());
    setBoolField(javaEnvironment, obj, thisClass, "enableEcho", player->isEnableEcho());
    setBoolField(javaEnvironment, obj, thisClass, "enableWhoosh", player->isEnableWhoosh());
}

extern "C" JNIEXPORT void
Java_com_fesskiev_engine_SuperpoweredEngine_onBackground(JNIEnv *env, jobject instance) {
    if (player != NULL) {
        player->onBackground();
    }
}

extern "C" JNIEXPORT void
Java_com_fesskiev_engine_SuperpoweredEngine_onForeground(JNIEnv *env, jobject instance) {
    if (player != NULL) {
        player->onForeground();
    }
}


extern "C" JNIEXPORT void
Java_com_fesskiev_engine_SuperpoweredEngine_registerCallback(JNIEnv *env,
                                                             jobject instance) {
    callbackObject = env->NewGlobalRef(instance);
}


extern "C" JNIEXPORT void
Java_com_fesskiev_engine_SuperpoweredEngine_unregisterCallback(JNIEnv *env,
                                                               jobject instance) {
    env->DeleteGlobalRef(callbackObject);
    callbackObject = NULL;
}

extern "C" JNIEXPORT void
Java_com_fesskiev_engine_SuperpoweredEngine_enableEQ(JNIEnv *env, jobject instance,
                                                     jboolean enable) {
    if (player != NULL) {
        player->enableEQ(enable);
    }
}



extern "C" JNIEXPORT void
Java_com_fesskiev_engine_SuperpoweredEngine_setEQBands(JNIEnv *javaEnvironment,
                                                       jobject obj,
                                                       jint band, jint value) {
    if (player != NULL) {
        player->setEQBands(band, value);
    }
}

extern "C" JNIEXPORT void
Java_com_fesskiev_engine_SuperpoweredEngine_setLooping(JNIEnv *env,
                                                       jobject instance,
                                                       jboolean isLooping) {
    player->setLooping(isLooping);
}

extern "C" JNIEXPORT void
Java_com_fesskiev_engine_SuperpoweredEngine_openAudioFile(JNIEnv *env, jobject instance,
                                                          jstring path) {
    const char *str = env->GetStringUTFChars(path, 0);

    if (player != NULL) {
        player->open(str);
    }

    env->ReleaseStringUTFChars(path, str);
}


extern "C" JNIEXPORT void
Java_com_fesskiev_engine_SuperpoweredEngine_togglePlayback(JNIEnv *env, jobject instance) {
    if (player != NULL) {
        player->togglePlayback();
    }
}

extern "C" JNIEXPORT void
Java_com_fesskiev_engine_SuperpoweredEngine_setVolume(JNIEnv *env, jobject instance,
                                                      jfloat value) {
    if (player != NULL) {
        player->setVolume(value);
    }
}

extern "C" JNIEXPORT void
Java_com_fesskiev_engine_SuperpoweredEngine_setSeek(JNIEnv *env, jobject instance,
                                                    jint value) {
    if (player != NULL) {
        player->setSeek(value);
    }
}

extern "C" JNIEXPORT void
Java_com_fesskiev_engine_SuperpoweredEngine_setEchoValue(JNIEnv *env, jobject instance,
                                                         jint value) {
    if (player != NULL) {
        player->echoValue(value);
    }
}

extern "C" JNIEXPORT void
Java_com_fesskiev_engine_SuperpoweredEngine_enableEcho(JNIEnv *env, jobject instance,
                                                       jboolean enable) {
    if (player != NULL) {
        player->enableEcho(enable);
    }
}

extern "C" JNIEXPORT void
Java_com_fesskiev_engine_SuperpoweredEngine_setReverbValue(JNIEnv *env, jobject instance,
                                                           jint mix, jint width,
                                                           jint damp, jint roomSize) {
    if (player != NULL) {
        player->reverbValue(mix, width, damp, roomSize);
    }
}

extern "C" JNIEXPORT void
Java_com_fesskiev_engine_SuperpoweredEngine_enableReverb(JNIEnv *env, jobject instance,
                                                         jboolean enable) {
    if (player != NULL) {
        player->enableReverb(enable);
    }
}

extern "C" JNIEXPORT void
Java_com_fesskiev_engine_SuperpoweredEngine_setWhooshValue(JNIEnv *env, jobject instance,
                                                           jint wet, jint frequency) {
    if (player != NULL) {
        player->whooshValue(wet, frequency);
    }

}

extern "C" JNIEXPORT void
Java_com_fesskiev_engine_SuperpoweredEngine_enableWhoosh(JNIEnv *env, jobject instance,
                                                         jboolean enable) {
    if (player != NULL) {
        player->enableWhoosh(enable);
    }
}

extern "C" JNIEXPORT void
Java_com_fesskiev_engine_SuperpoweredEngine_startRecording(JNIEnv *env, jobject instance,
                                                           jstring destination) {
    const char *destinationPath = env->GetStringUTFChars(destination, 0);

    if (player != NULL) {
        player->startRecording(destinationPath);
    }

    env->ReleaseStringUTFChars(destination, destinationPath);
}

extern "C" JNIEXPORT void
Java_com_fesskiev_engine_SuperpoweredEngine_stopRecording(JNIEnv *env, jobject instance) {
    if (player != NULL) {
        player->stopRecording();
    }
}

extern "C" JNIEXPORT void
Java_com_fesskiev_engine_SuperpoweredEngine_setPitchShift(JNIEnv *env, jobject instance,
                                                          jint pitchShift) {
    if (player != NULL) {
        player->setPitchShift(pitchShift);
    }
}

extern "C" JNIEXPORT void
Java_com_fesskiev_engine_SuperpoweredEngine_AudioPlaybackService_setTempo(JNIEnv *env,
                                                                          jobject instance,
                                                                          jdouble tempo) {
    if (player != NULL) {
        player->setTempo(tempo);
    }
}

extern "C" JNIEXPORT void
Java_com_fesskiev_engine_SuperpoweredEngine_AudioPlaybackService_setPosition(JNIEnv *env,
                                                                             jobject instance,
                                                                             jint value) {
    if (player != NULL) {
        player->setPosition(value);
    }
}

extern "C" JNIEXPORT void
Java_com_fesskiev_engine_SuperpoweredEngine_loopBetween(JNIEnv *env, jobject instance,
                                                        jdouble startMs, jdouble endMs) {
    if (player != NULL) {
        player->loopBetween(startMs, endMs);
    }
}

extern "C" JNIEXPORT void
Java_com_fesskiev_engine_SuperpoweredEngine_loopExit(JNIEnv *env, jobject instance) {
    if (player != NULL) {
        player->loopExit();
    }
}

