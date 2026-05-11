#include <jni.h>
#include <algorithm>

extern "C" {

JNIEXPORT jintArray JNICALL

Java_image_ConvolutionEngine_convolveNative(JNIEnv *env, jobject thisObj, jintArray pixelsArray, jint width, jint height,
                                           jfloatArray kernelArray, jint kernelSize) {
    jsize pixelsLen = env->GetArrayLength(pixelsArray);

    jint *pixels = env->GetIntArrayElements(pixelsArray, nullptr);
    jfloat *kernel = env->GetFloatArrayElements(kernelArray, nullptr);

    jintArray resultArray = env->NewIntArray(pixelsLen);

    jint *result = env->GetIntArrayElements(resultArray, nullptr);

    for (jsize i = 0; i < pixelsLen; i++)
    {
        result[i] = pixels[i];
    }

    int radius = kernelSize / 2;

    for (int y = radius; y < height - radius; y++) {
        for (int x = radius; x < width - radius; x++) {
            float rSum = 0.0f;
            float gSum = 0.0f;
            float bSum = 0.0f;

            for (int ky = -radius; ky <= radius; ky++) {
                for (int kx = -radius; kx <= radius; kx++) {
                    int pixelIndex = (y + ky) * width + (x + kx);
                    int kernelIndex = (ky + radius) * kernelSize + (kx + radius);

                    int pixel = pixels[pixelIndex];
                    float weight = kernel[kernelIndex];

                    int r = (pixel >> 16) & 0xFF;
                    int g = (pixel >> 8) & 0xFF;
                    int b = pixel & 0xFF;

                    rSum += r * weight;
                    gSum += g * weight;
                    bSum += b * weight;
                }
            }

            int finalR = std::min(std::max(static_cast<int>(rSum), 0), 255);
            int finalG = std::min(std::max(static_cast<int>(gSum), 0), 255);
            int finalB = std::min(std::max(static_cast<int>(bSum), 0), 255);

            int originalAlpha = pixels[y * width + x] & 0xFF000000;

            result[y * width + x] = originalAlpha | (finalR << 16) | (finalG << 8) | finalB;
        }
    }
    env->ReleaseIntArrayElements(pixelsArray, pixels, JNI_ABORT);
    env->ReleaseFloatArrayElements(kernelArray, kernel, JNI_ABORT);

    env->ReleaseIntArrayElements(resultArray, result, 0);

    return resultArray;
}

}
