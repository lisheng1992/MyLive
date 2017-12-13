# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

add_library( # Sets the name of the library.
             my-live
             # Sets the library as a shared library.
             SHARED
             # Provides a relative path to your source file(s).
             src/main/cpp/dn_live.c
             src/main/cpp/queue.c)


find_library( # Sets the name of the path variable.
              log-lib
              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )
add_library(faac
            STATIC
            IMPORTED
            )
set_target_properties(faac
                      PROPERTIES IMPORTED_LOCATION
                      ${CMAKE_SOURCE_DIR}/src/main/jniLibs/armeabi/libfaac.a)
add_library(rtmp
            STATIC
            IMPORTED
            )
set_target_properties(rtmp
                      PROPERTIES IMPORTED_LOCATION
                      ${CMAKE_SOURCE_DIR}/src/main/jniLibs/armeabi/librtmp.a)
add_library(x264
            STATIC
            IMPORTED
            )
set_target_properties(x264
                      PROPERTIES IMPORTED_LOCATION
                      ${CMAKE_SOURCE_DIR}/src/main/jniLibs/armeabi/libx264.a)
include_directories(src/main/cpp/faac/include)
include_directories(src/main/cpp/rtmpdump/include)
include_directories(src/main/cpp/x264/include)
target_link_libraries( # Specifies the target library.
                       my-live faac rtmp x264

                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib} )