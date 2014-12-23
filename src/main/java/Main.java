import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.media.opengl.awt.GLCanvas;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import managers.DataManager;
import utils.ImageHelper;
import event.listeners.MouseEventListener;
import event.listeners.Rendering;

public class Main {

    public static void main(String[] args) {

        // Deploying BufferedImage from DICOM to managers.DataManager...
        DataManager.getInstance().setImage(ImageHelper.readDicom(DataManager.PATH));

        launchSwingWithOpenGL2(DataManager.getInstance().getImage().getWidth(), DataManager.getInstance().getImage().getHeight());
    }

    public static void launchSwingWithOpenGL2(int width, int height) {

        GLCanvas canvas = DataManager.getInstance().getCanvas();
        canvas.setPreferredSize(new Dimension(width, height));

        canvas.addGLEventListener(new Rendering());
        canvas.addMouseListener(new MouseEventListener());

        final JFrame frame = DataManager.getInstance().getFrame();

        // Set the GUI content
        frame.getContentPane().add(canvas, BorderLayout.CENTER);

        // Set default options ...
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);

        SwingUtilities.invokeLater(() -> frame.setVisible(true));
    }
}
