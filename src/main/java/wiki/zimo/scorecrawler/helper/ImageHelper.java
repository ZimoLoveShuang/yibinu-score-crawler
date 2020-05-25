package wiki.zimo.scorecrawler.helper;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ImageHelper {

    public static void saveImageFile(BufferedImage image, String fileName) throws IOException {
        ImageIO.write(image, "jpg", new File(fileName));
    }

    public static void saveImageFile(InputStream inputStream, String fileName) throws IOException {
        BufferedImage image = ImageIO.read(inputStream);
        ImageIO.write(image, "jpg", new File(fileName));
    }

    public static BufferedImage binaryzation(InputStream inputStream) throws IOException {
        BufferedImage img = ImageIO.read(inputStream);
        return binaryzation(img);
    }

    public static BufferedImage binaryzation(BufferedImage image) {
        int grayValue = 115;
        BufferedImage img = image;
        int width = img.getWidth();
        int height = img.getHeight();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int p = img.getRGB(x, y);
                int a = (p >> 24) & 0xff;
                int r = (p >> 16) & 0xff;
                int g = (p >> 8) & 0xff;
                int b = p & 0xff;
                if (r * 0.299 + g * 0.578 + b * 0.114 >= grayValue) {
                    img.setRGB(x, y, Color.WHITE.getRGB());
                } else {
                    img.setRGB(x, y, Color.BLACK.getRGB());
                }
            }
        }
        return img;
    }
}
