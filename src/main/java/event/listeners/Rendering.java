package event.listeners;

import java.awt.image.BufferedImage;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;

import managers.DataManager;
import utils.ImageHelper;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

public class Rendering implements GLEventListener {

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        GL2 gl2 = glAutoDrawable.getGL().getGL2();
        gl2.glEnable(GL.GL_TEXTURE_2D);
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int width, int height) {
        GL2 gl2 = glAutoDrawable.getGL().getGL2();
        gl2.glOrtho(0, width, 0, height, -1, 1);
    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        GL2 gl2 = glAutoDrawable.getGL().getGL2();
        gl2.glClear(GL.GL_COLOR_BUFFER_BIT);

        Texture currentTexture = null;

        if (DataManager.getInstance().isMousedClicked()) {
            currentTexture = AWTTextureIO.newTexture(glAutoDrawable.getGLProfile(), DataManager.getInstance().getImage().getBufferedImage(), false);
        }
        else {
            currentTexture = AWTTextureIO.newTexture(glAutoDrawable.getGLProfile(),
                    ImageHelper.getSegmentationImage(DataManager.getInstance().getImage(), DataManager.getInstance().getMouse()),
                    false);
        }

        currentTexture.bind(gl2);
        BufferedImage image = DataManager.getInstance().getImage().getBufferedImage();
        /*
         * bf(0;0) -> gl(0;1) bf(1;0) -> gl(1;1)
         *
         *
         * bf(0;1) -> gl(0;0) bf(1;1) -> gl(1;0)
         */
        gl2.glBegin(GL2.GL_POLYGON);
        gl2.glTexCoord2i(0, 1);
        gl2.glVertex2i(0, 0);
        gl2.glTexCoord2i(0, 0);
        gl2.glVertex2i(0, image.getHeight());
        gl2.glTexCoord2i(1, 0);
        gl2.glVertex2i(image.getWidth(), image.getHeight());
        gl2.glTexCoord2i(1, 1);
        gl2.glVertex2i(image.getWidth(), 0);
        gl2.glEnd();

    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {
    }
}
