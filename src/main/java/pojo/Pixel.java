package pojo;

import java.awt.Color;

public class Pixel {
    public int color;
    public int x;
    public int y;

    public Pixel(int grayScale) {
        color = grayScale;
    }

    public Pixel(int grayScale, int x, int y) {
        color = grayScale;
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object pixel) {
        if (pixel instanceof Pixel) {
            return x == ((Pixel) pixel).x && y == ((Pixel) pixel).y;
        }
        else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "[ (" + x + "|" + y + ") grayScale = " + color + " ] ";
    }

    public void setColor(int grayScale) {
        color = grayScale;
    }

    public int getGrayScale() {
        return color;
    }

    public int getARGB() {
        return new Color(color, color, color).getRGB();
    }

    public int getColoredARGB() {
        return new Color(color, 0, color).getRGB();
    }
}
