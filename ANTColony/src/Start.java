import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JApplet;
import javax.swing.JFrame;

import object.Playground;
import view.JMenuPanel;

public class Start extends JApplet{
	
    
	private static final long serialVersionUID = 1L;
	
	
	public Start(){
		
		setLayout(new BorderLayout());
		
		//Instance the JPanel Playground and the JPanel JMenuPanel
		Playground p = new Playground();
		JMenuPanel jMenuPanel = new JMenuPanel(p);
		
		add(jMenuPanel, BorderLayout.NORTH);
		add(p, BorderLayout.CENTER);
		
		//Set the size
		setSize(p.getSizeX() + p.getSizeCase() * 3, p.getSizeY() + p.getSizeCase() * 5);
			    	
	}
	
	public void init(){
	    setLayout(new BorderLayout());
		
		//Instance the JPanel Playground and the JPanel JMenuPanel
		Playground p = new Playground();
		JMenuPanel jMenuPanel = new JMenuPanel(p);
		
		add(jMenuPanel, BorderLayout.NORTH);
		add(p, BorderLayout.CENTER);
		
		//Set the size
		setSize(p.getSizeX() + p.getSizeCase() * 3, p.getSizeY() + p.getSizeCase() * 5);
	}
	
	public void run(){
	    setLayout(new BorderLayout());
		
		//Instance the JPanel Playground and the JPanel JMenuPanel
		Playground p = new Playground();
		JMenuPanel jMenuPanel = new JMenuPanel(p);
		
		add(jMenuPanel, BorderLayout.NORTH);
		add(p, BorderLayout.CENTER);
		
		//Set the size
		setSize(p.getSizeX() + p.getSizeCase() * 3, p.getSizeY() + p.getSizeCase() * 5);
	}
	
	public static void main(String[] args) {
	    Component applet = new Start();
	    
	    JFrame jFrame = new JFrame("Ant Colony algorithm");
	    jFrame.getContentPane().add(applet);
	    jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	    jFrame.pack();
	    jFrame.setVisible(true);
	}
	    
}
