//
// Created by LYF on 2021/7/25.
//
#include <jni.h>
#include <string>
#include <android/log.h>
#include <pthread.h>

#define TAG "native-lib"
// __VA_ARGS__ 代表 ...的可变参数
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, TAG,  __VA_ARGS__);

/**
 * 对应java类的全路径名，.用/代替
 */
const char *classPathName = "com/example/ffmpeglearn/JniActivity";
extern "C" JNIEXPORT void JNICALL
Java_com_example_ffmpeglearn_JniActivity_pushData(JNIEnv *env, jobject thiz,
                                                  jboolean b,
                                                  jbyte b1,
                                                  jchar c,
                                                  jshort s,
                                                  jlong l,
                                                  jfloat f,
                                                  jdouble d,
                                                  jstring name,
                                                  jint age,
                                                  jintArray int_array,
                                                  jobjectArray str_array,
                                                  jobject person,
                                                  jbooleanArray b_array) {
    // 1 接受Java传递过来的Boolean类型
    unsigned char b_boolean = b;
    LOGE("boolean -> %d", b_boolean)
    // 2 接受一个byte 类型
    char c_byte = b1;
    LOGE("jbyte ->%d", c_byte)
    //3\. 接收 Java 传递过来的 char 值
    unsigned short c_char = c;
    LOGE("char-> %d", c_char)

    //4\. 接收 Java 传递过来的 short 值
    short s_short = s;
    LOGE("short-> %d", s_short)

    //5\. 接收 Java 传递过来的 long 值
    long l_long = l;
    LOGE("long-> %ld", l_long);

    //6\. 接收 Java 传递过来的 float 值
    float f_float = f;
    LOGE("float-> %f", f_float);

    //7\. 接收 Java 传递过来的 double 值
    double d_double = d;
    LOGE("double-> %f", d_double)
    //8\. 接收 Java 传递过来的 String 值
    const char *name_string = env->GetStringUTFChars(name, 0);
    LOGE("string ->%s", name_string)
    //9\. 接收 Java 传递过来的 int 值
    int age_java = age;
    LOGE("int:%d", age_java)
    //10\. 打印 Java 传递过来的 int []
    jint *intArray = env->GetIntArrayElements(int_array, NULL);
    // 获取数组长度
    jsize intArraySize = env->GetArrayLength(int_array);
    for (int i = 0; i < intArraySize; ++i) {
        LOGE("intArray ->%d", intArray[i])
    }
    // 释放数组
    env->ReleaseIntArrayElements(int_array, intArray, 0);
    //12\. 打印 Java 传递过来的 Object 对象
    //12.1 获取字节码
    const char *person_class_str = "com/example/ffmpeglearn/data/Person";
    // 获取class类
    jclass person_class = env->FindClass(person_class_str);
    // 拿到方法签名
    const char *sig = "()Ljava/lang/String;";
    jmethodID jmethodID1 = env->GetMethodID(person_class, "getName", sig);
    // 调用方法
    jobject obj_string = env->CallObjectMethod(person, jmethodID1);
    // 将方法返回值转换成jstring
    jstring nameStr = static_cast<jstring>(obj_string);
    // 将jstring 转换成char
    const char *itemStr2 = env->GetStringUTFChars(nameStr, NULL);
    LOGE("Person.Name:%s", itemStr2)
    env->DeleteLocalRef(person_class);
    env->DeleteLocalRef(person);

}
extern "C" JNIEXPORT jstring JNICALL
Java_com_example_ffmpeglearn_JniActivity_stringFromJNI(JNIEnv *env, jobject thiz) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C" JNIEXPORT jobject JNICALL
Java_com_example_ffmpeglearn_JniActivity_getPerson(JNIEnv *env, jobject thiz) {
    //1\. 拿到 Java 类的全路径
    const char *person_java = "com/example/ffmpeglearn/data/Person";
    const char *method = "<init>"; // Java构造方法的标识

    //2\. 找到需要处理的 Java 对象 class
    jclass j_person_class = env->FindClass(person_java);

    //3\. 拿到空参构造方法
    jmethodID person_constructor = env->GetMethodID(j_person_class, method, "()V");

    //4\. 创建对象
    jobject person_obj = env->NewObject(j_person_class, person_constructor);

    //5\. 拿到 setName 方法的签名，并拿到对应的 setName 方法
    const char *nameSig = "(Ljava/lang/String;)V";
    jmethodID nameMethodId = env->GetMethodID(j_person_class, "setName", nameSig);

    //6\. 拿到 setAge 方法的签名，并拿到 setAge 方法
    const char *ageSig = "(I)V";
    jmethodID ageMethodId = env->GetMethodID(j_person_class, "setAge", ageSig);

    //7\. 正在调用 Java 对象函数
    const char *name = "梁宝贝";
    jstring newStringName = env->NewStringUTF(name);
    env->CallVoidMethod(person_obj, nameMethodId, newStringName);
    env->CallVoidMethod(person_obj, ageMethodId, 28);

    const char *sig = "()Ljava/lang/String;";
    jmethodID jtoString = env->GetMethodID(j_person_class, "toString", sig);
    jobject obj_string = env->CallObjectMethod(person_obj, jtoString);
    jstring perStr = static_cast<jstring >(obj_string);
    const char *itemStr2 = env->GetStringUTFChars(perStr, NULL);
    LOGE("Person: %s", itemStr2)
    return person_obj;
}
extern "C"  //支持 C 语言
JNIEXPORT void JNICALL //告诉虚拟机，这是jni函数
native_dynamicRegister(JNIEnv *env, jobject instance, jstring name) {
    const char *j_name = env->GetStringUTFChars(name, NULL);
    LOGE("动态注册: %s", j_name)
    //释放
    env->ReleaseStringUTFChars(name, j_name);
}

extern "C"  //支持 C 语言
JNIEXPORT void JNICALL //告诉虚拟机，这是jni函数
native_dynamicRegister2(JNIEnv *env, jobject instance, jstring name) {
    const char *j_name = env->GetStringUTFChars(name, NULL);
    LOGE("动态注册: %s", j_name)
    // 拿到当前类的class
    jclass clazz = env->GetObjectClass(instance);
    // 找到当前类的产生异常的方法
    jmethodID mid = env->GetMethodID(clazz, "testException", "()V");
    // 调用该方法产生异常
    env->CallVoidMethod(instance, mid);
    // 检测异常信息
    jthrowable exc = env->ExceptionOccurred();
    if (exc) {
        // 打印异常
        env->ExceptionOccurred();
        env->ExceptionClear();
        jclass newExcCls = env->FindClass("java/lang/IllegalArgumentException");
        env->ThrowNew(newExcCls, "JNI 中发生了一个异常信息"); // 返回一个新的异常到 Java
    }
    //释放
    env->ReleaseStringUTFChars(name, j_name);
}

extern "C"  //支持 C 语言
JNIEXPORT void JNICALL //告诉虚拟机，这是jni函数
native_count(JNIEnv *env, jobject instance) {
    jclass claszz = env->GetObjectClass(instance);
    jfieldID fieldId = env->GetFieldID(claszz, "count", "I");
    // 监控锁定
    if (env->MonitorEnter(instance) != JNI_OK) {
        LOGE("%s: MonitorEnter() failed", __FUNCTION__)
    }
    // 获取Java变量count 数值
    int count = env->GetIntField(instance, fieldId);
    count++;
    LOGE("native里的数值count=%d", count)
    env->SetIntField(instance, fieldId, count);
    if (env->ExceptionOccurred()) {
        LOGE("ExceptionOccurred()...");
        if (env->MonitorExit(instance) != JNI_OK) {
            LOGE("%s: MonitorExit() failed", __FUNCTION__);
        }
    }
    if (env->MonitorExit(instance) != JNI_OK) {
        LOGE("%s: MonitorExit() failed", __FUNCTION__);
    }

}
JavaVM * jvm;
jobject instance;
void * customThread(void * pVoid) {
    // 调用的话，一定需要JNIEnv *env
    // JNIEnv *env 无法跨越线程，只有JavaVM才能跨越线程

    JNIEnv * env = NULL; // 全新的env
    int result = jvm->AttachCurrentThread(&env, 0); // 把native的线程，附加到JVM
    if (result != 0) {
        return 0;
    }

    jclass mainActivityClass = env->GetObjectClass(instance);

    // 拿到MainActivity的updateUI
    const char * sig = "()V";
    jmethodID updateUI = env->GetMethodID(mainActivityClass, "updateUI", sig);

    env->CallVoidMethod(instance, updateUI);

    // 解除 附加 到 JVM 的native线程
    jvm->DetachCurrentThread();

    return 0;
}

extern "C"  //支持 C 语言
JNIEXPORT void JNICALL //告诉虚拟机，这是jni函数
native_testThread(JNIEnv *env, jobject thiz) {
    instance = env->NewGlobalRef(thiz); // 全局的，就不会被释放，所以可以在线程里面用
    // 如果是非全局的，函数一结束，就被释放了
    pthread_t pthreadID;
    pthread_create(&pthreadID, 0, customThread, instance);
    pthread_join(pthreadID, 0);

}

extern "C"  //支持 C 语言
JNIEXPORT void JNICALL //告诉虚拟机，这是jni函数
native_unThread(JNIEnv *env, jobject thiz) {

    if (NULL != instance) {
        env->DeleteGlobalRef(instance);
        instance = NULL;
    }

}


static const JNINativeMethod jniNativeMethod[] = {
        {"dynamicRegister",  "(Ljava/lang/String;)V", (void *) (native_dynamicRegister)},
        {"dynamicRegister2", "(Ljava/lang/String;)V", (void *) (native_dynamicRegister2)},
        {"nativeCount",      "()V",                   (void *) (native_count)},
        {"testThread",      "()V",                   (void *) (native_testThread)},
        {"unThread",      "()V",                   (void *) (native_unThread)},
};
/**
 * 该函数定义在jni.h头文件中，System.loadLibrary()时会调用JNI_OnLoad()函数
 */
JNIEXPORT jint JNICALL
JNI_OnLoad(JavaVM *javaVm, void *pVoid) {
    //通过虚拟机 创建爱你全新的 evn
    JNIEnv *jniEnv = nullptr;
    jint result = javaVm->GetEnv(reinterpret_cast<void **>(&jniEnv), JNI_VERSION_1_6);
    if (result != JNI_OK) {
        return JNI_ERR; // 主动报错
    }
    jclass jniActivityClass = jniEnv->FindClass(classPathName);
    jniEnv->RegisterNatives(jniActivityClass, jniNativeMethod,
                            sizeof(jniNativeMethod) / sizeof(JNINativeMethod));//动态注册的数量

    return JNI_VERSION_1_6;
}

