package org.multithreading;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static final String SOURCE_FILE = "./resources/many-flowers.jpg";
    public static final String DESTINATION_FILE = "./out/many-flowers.jpg";

    public static void main(String[] args) throws IOException {

        long start = System.currentTimeMillis();

        BufferedImage originalImage = ImageIO.read(new File(SOURCE_FILE));

        BufferedImage resultImage = new BufferedImage(
                originalImage.getWidth(),
                originalImage.getHeight(),
                BufferedImage.TYPE_INT_RGB
        );

        //recolorSingleThreaded(originalImage, resultImage);
        recolorMultiThreaded(originalImage, resultImage, 2);

        File file = new File(DESTINATION_FILE);
        ImageIO.write(resultImage, "jpg", file);

        long end = System.currentTimeMillis();
        long duration = end - start;

        System.out.println("Duration: " + duration + " ms");
    }

    public static void recolorMultiThreaded(BufferedImage originalImage, BufferedImage resultImage, int numberOfThreads) {
        List<Thread> threads = new ArrayList<>();
        int width = originalImage.getWidth();
        int height = originalImage.getHeight() / numberOfThreads;

        for(int i = 0; i < numberOfThreads; i++){
            final int threadMultiplier = i;
            Thread thread = new Thread(() -> {
                int topCorner = threadMultiplier * height;
                recolorImage(originalImage, resultImage, 0, topCorner, width, height);
            });
            threads.add(thread);
        }

        for(Thread thread : threads){
            thread.start();
        }

        for(Thread thread : threads){
            try {
                thread.join();
            } catch (InterruptedException e) {
            }
        }
    }

    public static void recolorSingleThreaded(BufferedImage originalImage, BufferedImage resultImage) {
        recolorImage(originalImage, resultImage, 0, 0, originalImage.getWidth(), originalImage.getHeight());
    }

    public static void recolorImage(BufferedImage originalImage, BufferedImage resultImage, int leftCorner,
                                    int topCorner, int width, int height
    ) {
        for (int x = leftCorner; x < leftCorner + width && x < originalImage.getWidth(); x++) {
            for (int y = topCorner; y < topCorner + height && y < originalImage.getHeight(); y++) {
                recolorPixel(originalImage, resultImage, x, y);
            }
        }
    }

    public static void recolorPixel(BufferedImage originalImage, BufferedImage resultImage, int x, int y) {
        int rgb = originalImage.getRGB(x, y);

        int red = getRed(rgb);
        int green = getGreen(rgb);
        int blue = getBlue(rgb);

        int newRed;
        int newGreen;
        int newBlue;

        if (isShadeOfGray(red, green, blue)) {
            newRed = Math.min(255, red + 10);
            newGreen = Math.max(0, green - 80);
            newBlue = Math.max(0, blue - 20);
        }else{
            newRed = red;
            newGreen = green;
            newBlue = blue;
        }

        int newRGB = createRGBFromColors(newRed, newGreen, newBlue);
        resultImage.setRGB(x, y, newRGB);
        setRGB(resultImage, x, y, newRGB);
    }

    public static void setRGB(BufferedImage image, int x, int y, int rgb) {
        image.getRaster().setDataElements(x, y, image.getColorModel().getDataElements(rgb, null));
    }

    public static boolean isShadeOfGray(int red, int green, int blue) {
        return Math.abs(red - green) < 30 && Math.abs(red - blue) < 30 && Math.abs(green - blue) < 30;
    }

    public static int createRGBFromColors(int red, int green, int blue) {
       var rgb = 0;

       rgb |= blue;
       rgb |= green << 8;
       rgb |= red << 16;

       rgb |= 0xFF000000; // Alpha channel set to 255 (opaque)
       return rgb;
    }

    public static int getRed(int rgb) {
        return (rgb & 0xFF0000) >> 16;
    }

    public static int getGreen(int rgb) {
        return (rgb & 0x00FF00) >> 8;
    }

    public static int getBlue(int rgb) {
        return rgb & 0x0000FF;
    }
}