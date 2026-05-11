package image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class RawImage {
    private BufferedImage image;
    private int[] pixels;
    private int width;
    private int height;

    public RawImage(File imageFile) throws IOException {
        this.image = ImageIO.read(imageFile);
        this.width = this.image.getWidth();
        this.height = this.image.getHeight();

        this.pixels = new int[width * height];

        this.image.getRGB(0, 0, width, height, this.pixels, 0, width);
    }

    public RawImage(int width, int height) {
        this.width = width;
        this.height = height;
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        this.pixels = new int[width * height];
    }

    public int[] getPixels() {
        return pixels;
    }

    public void updatePixels(int[] newPixels) {
        this.pixels = newPixels;

        this.image.setRGB(0, 0, width, height, this.pixels, 0, width);
    }

    public void save(String format, File outputFile) throws IOException {
        ImageIO.write(this.image, format, outputFile);
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }
}
