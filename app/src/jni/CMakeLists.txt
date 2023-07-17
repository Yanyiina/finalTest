project(movenetncnn)

cmake_minimum_required(VERSION 3.10.2)

set(OpenCV_DIR D:/Android_jiemian/ruilai（2）/app/src/main/jni/opencv-mobile-4.6.0-android/sdk/native/jni)
find_package(OpenCV REQUIRED core imgproc)

set(ncnn_DIR D:/Android_jiemian/ruilai（2）/app/src/main/jni/ncnn-20220216-android-vulkan/${ANDROID_ABI}/lib/cmake/ncnn)
find_package(ncnn REQUIRED)

add_library(movenetncnn SHARED movenetncnn.cpp movenet.cpp utils.cpp jitterfilter.cpp ndkcamera.cpp)

target_link_libraries(movenetncnn ncnn ${OpenCV_LIBS} camera2ndk mediandk)
