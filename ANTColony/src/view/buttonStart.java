/*
 * Created on 16 sept. 2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package view;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import object.Playground;

/**
 * @author x
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class buttonStart implements ItemListener {

    private Playground p;
    /**
     * 
     */
    public buttonStart(Playground p) {
        super();
        this.p = p;
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
     */
    public void itemStateChanged(ItemEvent e) {
        Thread t = new Thread(p);
        
        if (e.getStateChange() == ItemEvent.SELECTED) { 
            System.out.println("Start");
	        p.setiRun();
            t.start();
	    } else {
	        System.out.println("Stop");
	        p.unsetiRun();
        }

    }

}
