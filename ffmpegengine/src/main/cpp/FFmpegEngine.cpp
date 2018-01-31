#include <stdio.h>

#include <jni.h>
#include <android/log.h>

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
Java_com_fesskiev_engine_FFmpegEngine_extractFileMetadata(JNIEnv *env,
                                                          jobject instance,
                                                          jstring filePath) {
    AVFormatContext *formatContext = NULL;
    AVDictionaryEntry *tag = NULL;

    av_register_all();
    int code = avformat_open_input(&formatContext, jStr2str(env, filePath), NULL, NULL);
    __android_log_print(ANDROID_LOG_VERBOSE, "MediaCenter", "metadata code = %i",
                        code);
    if (code != 0) {
        throwOpenFileException(env, av_err2str(code));
        return;
    }
    while ((tag = av_dict_get(formatContext->metadata, "", tag, AV_DICT_IGNORE_SUFFIX))) {
        __android_log_print(ANDROID_LOG_VERBOSE, "MediaCenter", "metadata key = %s value = %s",
                            tag->key, tag->value);
    }

    __android_log_print(ANDROID_LOG_VERBOSE, "MediaCenter", "duration = %ld name= %s",
                        (long)formatContext->duration,  formatContext->filename);

    avformat_close_input(&formatContext);
}

