/*
 *  useful tutorials about ffmpeg
 *  https://github.com/leandromoreira/ffmpeg-libav-tutorial
 *  https://github.com/mpenkov/ffmpeg-tutorial
 *  https://github.com/roman10/android-ffmpeg-tutorial
 *
 *    audio metadata
      metadata key = track value = 1/10
      metadata key = artist value = Fink
      metadata key = title value = Resurgam
      metadata key = genre value = Indie
      metadata key = album value = Resurgam
      metadata key = date value = 2017
 */

#include <stdio.h>

#include <jni.h>
#include <android/log.h>
#include "FFmpegEngine.h"

extern "C" {
#include <libavformat/avformat.h>
#include <libavutil/dict.h>
}

static char *jStr2str(JNIEnv *env, jstring source) {
    jsize inputLength = env->GetStringUTFLength(source);
    char *output = new char[inputLength + 1];
    env->GetStringUTFRegion(source, 0, inputLength, output);
    output[inputLength] = '\0';
    return output;
}

static void throwOpenFileException(JNIEnv *pEnv, char *pMessage) {
    jclass clazz = pEnv->FindClass("java/lang/IllegalArgumentException");
    if (clazz != NULL) {
        pEnv->ThrowNew(clazz, pMessage);
    }
    pEnv->DeleteLocalRef(clazz);
}

extern "C" JNIEXPORT void
Java_com_fesskiev_engine_FFmpegEngine_extractFileMetadata(JNIEnv *env, jobject instance,
                                                          jstring filePath) {
    AVFormatContext *formatContext = NULL;

    av_register_all();
    int code = avformat_open_input(&formatContext, jStr2str(env, filePath), NULL, NULL);
    if (code != 0) {
        throwOpenFileException(env, av_err2str(code));
        return;
    }

    __android_log_print(ANDROID_LOG_VERBOSE, "MediaCenter", "name= %s", formatContext->filename);


    for (int i = 0; i < formatContext->nb_streams; i++) {
        AVCodecParameters *localCodecParameters = formatContext->streams[i]->codecpar;

        AVCodec *localCodec = NULL;
        localCodec = avcodec_find_decoder(localCodecParameters->codec_id);
        if (localCodec == NULL) {
            return;
        }
        if (localCodecParameters->codec_type == AVMEDIA_TYPE_VIDEO) {

            __android_log_print(ANDROID_LOG_VERBOSE, "MediaCenter", "Video Codec: resolution %d x %d",
                                localCodecParameters->width, localCodecParameters->height);
        } else if (localCodecParameters->codec_type == AVMEDIA_TYPE_AUDIO) {

            AVDictionaryEntry *tag = NULL;
            while ((tag = av_dict_get(formatContext->metadata, "", tag, AV_DICT_IGNORE_SUFFIX))) {
                __android_log_print(ANDROID_LOG_VERBOSE, "MediaCenter", "metadata key = %s value = %s", tag->key, tag->value);
            }

            __android_log_print(ANDROID_LOG_VERBOSE, "MediaCenter", "duration = %ld", (long) formatContext->streams[i]->duration);

            __android_log_print(ANDROID_LOG_VERBOSE, "MediaCenter", "channels= %d sample rate= %d",
                                localCodecParameters->channels, localCodecParameters->sample_rate);
            __android_log_print(ANDROID_LOG_VERBOSE, "MediaCenter", "bitrate= %ld", (long) localCodecParameters->bit_rate);
        }
    }
    avformat_close_input(&formatContext);
    avformat_free_context(formatContext);
}

