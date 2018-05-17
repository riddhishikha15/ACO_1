
package object;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;


public class PlaygroundMouseMotionListener implements MouseMotionListener {

    private Playground p;
    int posX, posY;

    public PlaygroundMouseMotionListener(Playground p) {
        super();
        this.p = p;
        this.posX = 0;
        this.posY = 0;
    }


    public void mouseDragged(MouseEvent e) {
       boolean doIt = false;
       if(e.getX()/p.getSizeCase() != posX){
           posX = e.getX()/p.getSizeCase();
           doIt = true;
       }
       if(e.getY()/p.getSizeCase() != posY){
           posY = e.getY()/p.getSizeCase();
           doIt = true;
       }
        if(doIt){
            //System.out.println("Invert Case: [" + posX + "," + posY + "]");
            p.invertCase(posX*p.getSizeCase(), posY*p.getSizeCase());
            p.repaint();
        }
    }

    public void mouseMoved(MouseEvent arg0) {
    }

}