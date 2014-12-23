package utils;

import ij.plugin.DICOM;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import managers.DataManager;
import pojo.Mouse;
import pojo.Pixel;

public class ImageHelper
{
    public static DICOM readDicom(String path) {
        DICOM image = new DICOM();
        image.open(path);
        return image;
    }

    public static int getMax(List<Pixel> imageData) {
        return Collections.max(imageData, (o1, o2) -> Integer.compare(o1.getGrayScale(), o2.getGrayScale())).getGrayScale();
    }

    public static int getMin(List<Pixel> imageData) {
        return Collections.min(imageData, (o1, o2) -> Integer.compare(o1.getGrayScale(), o2.getGrayScale())).getGrayScale();
    }

    public static List<Pixel> createPixelsListFromImage(DICOM image) {
        List<Pixel> imageData = new ArrayList<>(image.getWidth() * image.getHeight());

        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                imageData.add(new Pixel(image.getPixel(i, j)[0]));
            }
        }

        int minGrayScale = getMin(imageData);

        List<Pixel> resultSet = new ArrayList<>(image.getWidth() * image.getHeight());
        for (Pixel pixel : imageData) {
            resultSet.add(new Pixel(pixel.getGrayScale() - minGrayScale));
        }

        return resultSet.isEmpty() ? null : resultSet;
    }

    public static Pixel[][] make2DimsMatrix(List<Pixel> pixels, int width, int height) {
        Pixel[][] pixelsMatrix = new Pixel[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                pixelsMatrix[i][j] = pixels.get(i * width + j);
            }
        }

        return pixelsMatrix;
    }

    public static BufferedImage getSegmentationImage(DICOM origin, Mouse segmentationPoint) {

        List<Pixel> pixelsList = createPixelsListFromImage(origin);

        Pixel[][] pixels = make2DimsMatrix(pixelsList, origin.getWidth(), origin.getHeight());

        List<Pixel> toCheck = new ArrayList<>();
        List<Pixel> segmentationPoints = new ArrayList<>();

        Pixel startPoint = new Pixel(pixels[segmentationPoint.x][segmentationPoint.y].getGrayScale(), segmentationPoint.x, segmentationPoint.y);
        toCheck.add(startPoint);

        while (!toCheck.isEmpty()) {

            Pixel currentPoint = toCheck.get(0);

            List<Pixel> neighbors = getNeighbors(pixels, currentPoint);

            segmentationPoints.add(currentPoint);
            toCheck.remove(currentPoint);

            for (Pixel neighbor : neighbors) {
                if (isFitted(startPoint, neighbor) && !segmentationPoints.contains(neighbor) && !toCheck.contains(neighbor)) {
                    toCheck.add(neighbor);
                }
            }

        }

        double min = getMin(pixelsList);
        double max = getMax(pixelsList);

        pixelsList.forEach((pixel) -> pixel.setColor((int) ((pixel.getGrayScale() - min) / max * DataManager.NEW_MAX_GREYSCALE)));

        double minS = getMin(segmentationPoints);
        double maxS = getMax(segmentationPoints);

        segmentationPoints.forEach((pixel) -> pixel.setColor((int) ((pixel.getGrayScale() - minS) / maxS * (DataManager.NEW_MAX_GREYSCALE / 2) + DataManager.NEW_MAX_GREYSCALE / 2)));

        return makeColorImage(pixelsList, segmentationPoints, origin.getWidth(), origin.getHeight());
    }

    public static List<Pixel> getNeighbors(Pixel[][] allPixels, Pixel currentPixel) {
        List<Pixel> neighbors = new ArrayList<>(8);
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }

                int x = currentPixel.x + j;
                int y = currentPixel.y + i;
                if (x >= 0 && y >= 0 && x < 256 && y < 256) {
                    neighbors.add(new Pixel(allPixels[x][y].getGrayScale(), x, y));
                }
            }
        }

        return neighbors;
    }

    public static boolean isFitted(Pixel startPoint, Pixel currentPoint) {
        return startPoint.getGrayScale() - DataManager.INTENSITY_VARIATION <= currentPoint.getGrayScale()
                && currentPoint.getGrayScale() <= startPoint.getGrayScale() + DataManager.INTENSITY_VARIATION;
    }

    public static BufferedImage makeColorImage(List<Pixel> imagePixel, List<Pixel> coloredPixels, int width, int height) {
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {

                int indexOfColorPixel = coloredPixels.indexOf(new Pixel(0, i, j));
                if (indexOfColorPixel != -1) {
                    result.setRGB(i, j, coloredPixels.get(indexOfColorPixel).getColoredARGB());
                }
                else {
                    result.setRGB(i, j, imagePixel.get(i * width + j).getARGB());
                }
            }
        }

        return result;
    }
}