#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring

JNICALL
Java_com_liwinner_mylive_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
extern "C"
JNIEXPORT void JNICALL
Java_com_liwinner_mylive_jin_PusherNative_setVideoOptions(JNIEnv *env, jobject instance, jint width,
                                                          jint height, jint bitrate, jint fps) {

    // TODO

}