/*
 *  useful tutorials about ffmpeg
 *  https://github.com/leandromoreira/ffmpeg-libav-tutorial
 *  https://github.com/mpenkov/ffmpeg-tutorial
 *  https://github.com/roman10/android-ffmpeg-tutorial
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
Java_com_fesskiev_engine_FFmpegEngine_extractFileMetadata(JNIEnv *env, jobject instance, jstring filePath) {
    AVFormatContext *pFormatContext = NULL;

    av_register_all();
    int code = avformat_open_input(&pFormatContext, jStr2str(env, filePath), NULL, NULL);
    if (code != 0) {
        throwOpenFileException(env, av_err2str(code));
        return;
    }
    if (avformat_find_stream_info(pFormatContext,  NULL) < 0) {
        return;
    }

    for (int i = 0; i < pFormatContext->nb_streams; i++) {
        AVCodecParameters *pLocalCodecParameters = pFormatContext->streams[i]->codecpar;
        __android_log_print(ANDROID_LOG_VERBOSE, "MediaCenter", "start_time %ld", pFormatContext->streams[i]->start_time);

        AVCodec *pLocalCodec = NULL;
        pLocalCodec = avcodec_find_decoder(pLocalCodecParameters->codec_id);
        if (pLocalCodec==NULL) {
            return;
        }
        if (pLocalCodecParameters->codec_type == AVMEDIA_TYPE_VIDEO) {
            __android_log_print(ANDROID_LOG_VERBOSE, "MediaCenter", "Video Codec: resolution %d x %d", pLocalCodecParameters->width,
                                pLocalCodecParameters->height);
        } else if (pLocalCodecParameters->codec_type == AVMEDIA_TYPE_AUDIO) {
            __android_log_print(ANDROID_LOG_VERBOSE, "MediaCenter",  "Audio Codec: %d channels, sample rate %d, bit rate %ld",
                                pLocalCodecParameters->channels, pLocalCodecParameters->sample_rate, (long)pLocalCodecParameters->bit_rate);
            AVDictionaryEntry *tag = NULL;
            while ((tag = av_dict_get(pFormatContext->metadata, "", tag, AV_DICT_IGNORE_SUFFIX))) {
                __android_log_print(ANDROID_LOG_VERBOSE, "MediaCenter", "metadata key = %s value = %s", tag->key, tag->value);
            }
        }
    }

    avformat_close_input(&pFormatContext);
    avformat_free_context(pFormatContext);
}

