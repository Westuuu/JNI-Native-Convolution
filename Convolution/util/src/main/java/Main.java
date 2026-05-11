import image.ConvolutionEngine;
import image.ConvolutionFilter;
import image.RawImage;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            RawImage img = new RawImage(new File("../1_pwr.png"));

            int[] originalPixels = img.getPixels();

            float[] gaussianKernel = ConvolutionFilter.createGaussian();
            int gaussianKernelSize = 5;

            float[] sobelKernel = ConvolutionFilter.createSobelX();
            int sobelKerneLSize = 3;

            float[] blurBoxKernel = ConvolutionFilter.createBoxBlur3x3();
            int blurBoxKernelSize = 3;

            ConvolutionEngine engine = new ConvolutionEngine();

            int runs = 20;


            double totalJavaTime = 0;
            double totalNativeTime = 0;

            System.out.println("Java: ");
            for (int i = 0; i < runs; i ++) {
                long startTime = System.nanoTime();
                int[] blurredPixels = engine.convolveJava(originalPixels, img.getWidth(), img.getHeight(), blurBoxKernel, blurBoxKernelSize);
                long endTime = System.nanoTime();
                System.out.println("Czas wykonania: " + (endTime - startTime) / 1_000_000.0 + " ms");
                totalJavaTime += (endTime - startTime);
                img.updatePixels(blurredPixels);
                img.save("png", new File("wynik.png"));
            }
            System.out.println();
            System.out.println("Native: ");

            for (int i = 0; i < runs; i ++) {
                long startTime = System.nanoTime();
                int[] blurredPixelsNative = engine.convolveNative(originalPixels, img.getWidth(), img.getHeight(), blurBoxKernel, blurBoxKernelSize);
                long endTime = System.nanoTime();
                System.out.println("Czas wykonania: " + (endTime - startTime) / 1_000_000.0 + " ms");
                totalNativeTime += (endTime - startTime);
            }

            System.out.printf("Average java time after %d runs: %.2f ms%n", runs, (totalJavaTime / runs) / 1_000_000.0);
            System.out.printf("Average native time after %d runs: %.2f ms%n", runs, (totalNativeTime / runs) / 1_000_000.0);
//            img.updatePixels(blurredPixels);
//            img.save("png", new File("wynik_gauss.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
