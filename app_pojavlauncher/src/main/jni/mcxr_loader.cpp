#include <jni.h>
#include <thread>
#include <GLES3/gl3.h>
#include <dlfcn.h>

//
// Created by Judge on 12/23/2021.
//

static jobject* context;
static jobject* app;
static JavaVM* jvm;
static void* handle;

jint JNI_OnLoad(JavaVM* vm, void* reserved) {
    if (jvm == nullptr) {
        jvm = vm;
    }
    return JNI_VERSION_1_4;
}

JNIEXPORT JNICALL
extern "C" jlong
Java_net_sorenon_mcxr_play_MCXRNativeLoad_getJVMPtr(JNIEnv *env, jclass clazz) {
    return reinterpret_cast<jlong>(&jvm);
}

JNIEXPORT JNICALL
extern "C" void
Java_net_sorenon_mcxr_play_MCXRNativeLoad_renderImage(JNIEnv *env, jclass clazz, jint colorAttachment, jint index) {
    glFramebufferTextureLayer(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, colorAttachment, 0, index);
}

JNIEXPORT JNICALL
extern "C" jlong
Java_net_sorenon_mcxr_play_MCXRNativeLoad_getApplicationActivityPtr(JNIEnv *env, jclass clazz) {
    return reinterpret_cast<jlong>(&app);
}

JNIEXPORT JNICALL
extern "C" jlong
Java_net_sorenon_mcxr_play_MCXRNativeLoad_getOpenXRHandle(JNIEnv *env, jclass clazz) {
    return reinterpret_cast<jlong>(&handle);
}

extern "C"
JNIEXPORT void JNICALL
Java_net_kdt_pojavlaunch_MCXRLoader_launch(JNIEnv *env, jclass clazz, jobject main, jstring nativeDir) {
    // Load OpenXR
    std::string dir = env->GetStringUTFChars(nativeDir, JNI_FALSE);
    std::string fileext = "/libopenxr_loader.so";
    dir = dir + fileext;
    handle = dlopen(dir.c_str(), RTLD_LAZY|RTLD_LOCAL);
    if(!handle) {
        printf("OpenXR lib is null!");
    }

    main = (*env).NewGlobalRef(main);
    jclass clazz1 = (*env).GetObjectClass(main);
    jmethodID id = (*env).GetMethodID(clazz1, "runCraft", "()V");
    std::thread thread([=]() {
        JNIEnv* threadEnv;
        jvm->AttachCurrentThread(&threadEnv, nullptr);
        threadEnv->CallVoidMethod(main, id);
    });
    thread.detach();
}

extern "C"
JNIEXPORT void JNICALL
Java_net_kdt_pojavlaunch_MCXRLoader_setActivity(JNIEnv *env, jclass clazz, jobject ctx) {
    app = reinterpret_cast<jobject*>(env->NewGlobalRef(ctx));
}