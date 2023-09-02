// 包含刚才生成的.h文件
#include "jni.h"
#include "TestJNI.h"
#include <stdio.h>

JNIEXPORT void JNICALL Java_TestJNI_displayHelloWorld(JNIEnv *env, jclass thisClass) {
    printf("Hello, Native!!");
    return;
}