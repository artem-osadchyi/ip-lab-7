package event.listeners;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.media.opengl.awt.GLCanvas;

import managers.DataManager;

public class MouseEventListener implements MouseListener {
    @Override
    public void mouseClicked(MouseEvent e) {
        GLCanvas canvas = DataManager.getInstance().getCanvas();
        DataManager.getInstance().getMouse().setPosition(e.getX(), e.getY());
        canvas.display();
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

}
