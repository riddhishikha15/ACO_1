
package object;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class PlaygroundMouseListener implements MouseListener {

    private Playground p;

    public PlaygroundMouseListener(Playground p) {
        super();
        this.p = p;
    }

    public void mouseClicked(MouseEvent e) {
        int posX, posY;
        posX = e.getX()/p.getSizeCase();
        posY = e.getY()/p.getSizeCase();
        //System.out.println("Invert Case: [" + posX + "," + posY + "]");
        p.invertCase(posX*p.getSizeCase(), posY*p.getSizeCase());
        p.repaint();

    }
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent arg0) {}
    public void mouseEntered(MouseEvent arg0) {}
    public void mouseExited(MouseEvent arg0) {}

}