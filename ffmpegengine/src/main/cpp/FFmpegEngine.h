#include <cstdint>
#include "jni.h"

typedef union {

    float mFloat;
    int32_t mInteger;
    char* mString;
    jobject mObject;
    jboolean mBoolean;
    jshort mShort;
    jlong mLong;
    jdouble mDouble;
    jbyte mByte;

} Metadata;

