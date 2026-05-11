package image;

public class ConvolutionEngine {

    static {
        System.loadLibrary("convolution");
    }

    public native int[] convolveNative(int[] pixels, int width, int height, float[] kernel, int kernelSize);

    public int[] convolveJava(int[] pixels, int width, int height, float[] kernel, int kernelSize) {
        int[] result = pixels.clone();

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

                int finalR = Math.clamp((int) rSum, 0, 255);
                int finalG = Math.clamp((int) gSum, 0, 255);
                int finalB = Math.clamp((int) bSum, 0, 255);

                int originalAlpha = pixels[y * width + x] & 0xFF000000;

                result[y * width + x] = originalAlpha | (finalR << 16) | (finalG << 8) | finalB;
            }
        }

        return result;
    }
}
