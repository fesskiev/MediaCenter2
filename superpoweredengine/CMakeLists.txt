cmake_minimum_required(VERSION 3.4.1)

set(superpowered_DIR ${CMAKE_SOURCE_DIR}/src/main/cpp/superpowered)

include_directories(${superpowered_DIR})

add_library(
	SuperpoweredEngine
	SHARED
 	${superpowered_DIR}/AndroidIO/SuperpoweredAndroidAudioIO.cpp
 	src/main/cpp/SuperpoweredEngine.cpp
)

include_directories(${superpowered_DIR})

target_link_libraries(
        SuperpoweredEngine
        log
        android
        OpenSLES
        ${superpowered_DIR}/libSuperpoweredAndroid${ANDROID_ABI}.a
)
