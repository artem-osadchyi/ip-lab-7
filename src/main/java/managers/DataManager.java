package managers;

import ij.plugin.DICOM;

import javax.media.opengl.awt.GLCanvas;
import javax.swing.JFrame;

import pojo.Mouse;

public class DataManager {

    private DataManager() {
    }

    private static DataManager instance = null;

    public static final String PATH = "lab7.dcm";
    public static final int INTENSITY_VARIATION = 60;
    public static int NEW_MAX_GREYSCALE = 255;

    private GLCanvas canvas = new GLCanvas();
    private final JFrame frame = new JFrame();

    private DICOM image = null;
    private Mouse mouse = null;

    public Mouse getMouse() {
        if (mouse == null) {
            mouse = new Mouse();
        }
        return mouse;
    }

    public boolean isMousedClicked() {
        return mouse == null;
    }

    public JFrame getFrame() {
        return frame;
    }

    public GLCanvas getCanvas() {
        return canvas;
    }

    public DICOM getImage() {
        return image;
    }

    public void setImage(DICOM image) {
        this.image = image;
    }

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }
}
