package example;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
 
/**   
 * A couple of notes about long running tasks and GUI updates:
 *   1) all GUI painting should be done in the event thread
 *   2) GUI painting is not done until the event thread processing is done
 *   This means that long running code (database access, file processing ...)
 *   should not be done in the event thread. A new thread can be created for
 *   these tasks.
 *   Most Swing methods are not thread safe. If the long running task needs
 *   to update the GUI for any reason then the SwingUtilities class
 *   should be used to add code to the event thread.
 */
public class InvokeLaterTest extends JFrame	implements  Runnable, ActionListener
{	
	JLabel status;	
	JButton newThread;	
	Thread thread;	
	int i;
			
	public InvokeLaterTest()	
	{
		status = new JLabel( "Ready to Process:" );
		status.setHorizontalAlignment( JLabel.CENTER );		
 
		newThread = new JButton( "Start in New Thread" );
		newThread.addActionListener( this );
 
 
		getContentPane().add(status, BorderLayout.NORTH);	
		getContentPane().add(newThread, BorderLayout.EAST);				
	}	
 
	public void actionPerformed(ActionEvent e)
	{
		thread = new Thread( this );	
		thread.start();	
	}
 
	public void run()	
	{		
		newThread.setEnabled( false );	
 
		for (i = 1; i < 4; i++)	
		{		
			System.out.println("ProcessingFile: " + i);		
			//  SwingUtilities makes sure code is executed in the event thread.		
			
			SwingUtilities.invokeLater(new Runnable()			
			{				
				public void run()		
				{			
					status.setText("Processing File: " + i);	
				}			
			});			
			
			// simulate log running task			
			try { Thread.sleep(1000); }		
			catch (Exception e) {}		
		}		
			
		SwingUtilities.invokeLater(new Runnable()	
		{		
			public void run()			
			{			
				status.setText("Finished Processing");		
			}		
		});		
		
		newThread.setEnabled( true );
	}	
 
		
	public static void main(String[] args)	
	{	
		JFrame frame = new InvokeLaterTest();	
		frame.setDefaultCloseOperation( EXIT_ON_CLOSE );	
		frame.pack();	
		frame.setLocationRelativeTo( null );
		frame.show();	
	}
}
