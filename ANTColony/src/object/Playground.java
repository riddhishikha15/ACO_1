package object;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JPanel;


public class Playground extends JPanel implements Runnable  {

	private static final long serialVersionUID = 1L;
	
	private static int sizeOfThePlayground = 40; 	// 40
	private static int sizeOfCase = 14;				// 14
	private static int startX = 10, startY = 10;	//10,10
	private static int goalX = 25, goalY = 25;		// 25,25
	private static int timeOfRound = 150;			// in ms, 150
	private static int numberOfAnts = 40;			// 40
	
	
	private int sizeX, sizeY;
	private List antColony = new LinkedList();
	private Traces[][] traces;
	private Nodes[][] validCase;
	private boolean goalReached, iRun;
	private int numberOfSuccess, numberOfGoalReached;
	private int paintRefresh = 4, countRefresh=0;
	
	public Playground(){
		
	    this.sizeX = sizeOfThePlayground * sizeOfCase;
		this.sizeY = sizeOfThePlayground * sizeOfCase;
		this.goalReached = false; this.iRun = false;
		
		setPreferredSize(new Dimension(sizeX,sizeY));
		setBackground(Color.WHITE);		
		
		this.addMouseMotionListener(new PlaygroundMouseMotionListener(this));
		this.addMouseListener(new PlaygroundMouseListener(this));
		
	
		this.initPlayground();
		this.initTrace();
		this.initAnt();

	}
	
    public void initPlayground() {
        
		this.validCase = new Nodes[this.sizeX + sizeOfCase][this.sizeY + sizeOfCase];
		for(int i=0; i < this.sizeX + sizeOfCase; i++)
			for(int j=0; j < this.sizeY + sizeOfCase; j++)
			    this.validCase[i][j] = new Nodes();
    }
    
    public void resetPlayground(){
        for(int i=0; i < this.sizeX + sizeOfCase; i++)
			for(int j=0; j < this.sizeY + sizeOfCase; j++)
			    this.validCase[i][j].setValid();
    }
    
    public void initTrace() {
        this.traces = new Traces[this.sizeX + sizeOfCase][this.sizeY + sizeOfCase];
        for(int i=0; i < this.sizeX + sizeOfCase; i++)
			for(int j=0; j < this.sizeY + sizeOfCase; j++)
			    this.traces[i][j] = new Traces();
    }
    
    public void resetTrace(){
        for(int i=0; i < this.sizeX + sizeOfCase; i++)
			for(int j=0; j < this.sizeY + sizeOfCase; j++)
			    this.traces[i][j].reset();
    }
	
	public void initAnt() {
        
	    this.numberOfSuccess = 0;
	    this.numberOfGoalReached = 0;
	    
	    antColony.clear();
	    List antColony = new LinkedList();
        
	    for(int i=0; i < numberOfAnts; i++)
			antColony.add(new Ant());
        for (Iterator iter = antColony.iterator(); iter.hasNext();) {
		    Ant ant = (Ant) iter.next();
		    ant.setPosition(this.getStartX()*this.getSizeCase(), this.getStartY()*this.getSizeCase());
		    this.addAnt(ant);
		}
    }

    public void paint(Graphics g){

        //Background
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getSizeX()+getSizeCase(), getSizeY()+getSizeCase());
        
        
        //Traces and validCase
		    for(int i=0; i < getSizeX(); i++)
				for(int j=0; j < getSizeX(); j++){
				    if(!this.validCase[i][j].isCaseValid()){
				        g.setColor(Color.BLACK);
				        g.fillRect(i,j,getSizeCase(),getSizeCase());
				        g.setColor(Color.GRAY);
				        g.drawLine(i+1, j+1, i+getSizeCase()-2, j+1);
				        g.drawLine(i+1, j+1, i+1, j+getSizeCase()-2);
				    }
				    else
				        (this.traces[i][j]).draw(g,this,i,j);
				}
        
        
		//Ant (they draw themselves)
		for (Iterator iter = antColony.iterator(); iter.hasNext();) {
            ((Ant) iter.next()).draw(g, this);
        }
		
		//Start Point and Goal Point
		g.setColor(Color.CYAN);
		g.drawRect(startX * sizeOfCase, startY * sizeOfCase, sizeOfCase, sizeOfCase);
		g.fillRect(goalX * sizeOfCase, goalY * sizeOfCase, sizeOfCase, sizeOfCase);
		g.setColor(Color.BLACK);
		g.drawLine(goalX*sizeOfCase, goalY*sizeOfCase, sizeOfCase*(goalX+1), (1+goalY)*sizeOfCase);
		g.drawLine((goalX+1)*sizeOfCase, goalY*sizeOfCase, sizeOfCase*goalX, sizeOfCase*(goalY+1));
				
		//Number of times the goal has been reached
		g.setColor(Color.GRAY);
		String s2 = "Number of Success : " + String.valueOf(numberOfSuccess);
		String s1 = "Number of GoalReached : " + String.valueOf(numberOfGoalReached);
		g.drawString(s1, sizeX - 14 * sizeOfCase, sizeOfCase);
		g.drawString(s2, sizeX - 14 * sizeOfCase, 2 * sizeOfCase);
		
	}
	
	public void moveAnts(){
	    
	    ageTheTrace();
	    
	    int x,y;
	    Ant ant;
	    Traces t;
	    
		for (Iterator iter = antColony.iterator(); iter.hasNext();) {
		    ant = (Ant) iter.next();
			x = ant.getPosition()[0];
			y = ant.getPosition()[1];
			
			//If on goal or on start 
			treatmentStartGoal(x,y,ant);
			    
			// Add a trace, depending on goal
			t = this.traces[x][y];
			if(ant.getGoal())
			    t.addBack();
			else
			    t.addAway();
			
			// Add One move and check Endurance
			ant.addOneMove();
			if(ant.getMove() > ant.getEndurance())
			    ant.setTired();
			
			// Move
			if(ant.getTired()&&(!ant.getGoal())){
			    ant.moveTowardAway(x,y,this);
			}
			else if(!goalReached)
			    ant.moveStraightAwayFromAway(x,y,this);
			else {
			    if(ant.getGoal())
			        ant.moveFromFoodToHomeRepartition(x,y,this);
			    else
			        ant.moveFromHomeToFoodRepartition(x,y,this);
			}
		
		}
	}
	
    private void treatmentStartGoal(int x, int y, Ant ant) {
        
        // If on Goal
		if((x == goalX * sizeOfCase) && (y == goalY * sizeOfCase)){
		    if(!ant.getGoal()){
		        numberOfGoalReached++;
		        ant.setGoal();
		        this.goalReached = true;
		        ant.resetLastPosition();
		    }
		}
		// If on Start
		if((x == startX * sizeOfCase) && (y == startY * sizeOfCase)){
		    if(ant.getGoal()){
		        this.numberOfSuccess++;
		    	ant.unsetGoal();
		    	ant.resetLastPosition();
		    }
		    if(ant.getTired()){
		        ant.unsetTired();
		        ant.resetLastPosition();
		    }			    
		}
        
    }

    private void ageTheTrace() {
        // Age the traces
		for(int i=0; i < getSizeX(); i+=sizeOfCase){
			for(int j=0; j < getSizeX(); j+=sizeOfCase){
				Traces trace = this.traces[i][j];
				trace.toAge();
			}
		}
    }

    public void run() {
        while(iRun){
            timerWaiting(timeOfRound);
        	moveAnts();
        	repaint();
        }
    }

    public Traces[][] getTrace(){
		return this.traces;
	}
	public Nodes[][] getNodes(){
	    return this.validCase;
	}
	public void addAnt(Ant ant){
		antColony.add(ant);
		repaint();
	}
	public int getSizeCase(){
		return sizeOfCase;
	}
	public int getSizeOfThePlayground(){
		return sizeOfThePlayground;
	}
	public int getSizeX(){
		return sizeX;
	}
	public int getSizeY(){
		return sizeY;
	}
	public int getStartX(){
	    return startX;
	}
	public int getStartY(){
	    return startY;
	}
	public boolean getValidCase(int x, int y){
	    return this.validCase[x][y].isCaseValid();
	}
	public void invertCase(int x, int y){
	    this.validCase[x][y].changeCase();
	}
	public void setNodes(Nodes[][] newNodes) {
        this.validCase = newNodes;
    }
	public void setiRun(){
        this.iRun = true;
    }
    public void unsetiRun(){
        this.iRun = false;
    }
    public void timerWaiting(int TimeMilliseconds){
		long t0, t1;
		
		t0=System.currentTimeMillis( );
		t1=System.currentTimeMillis( )+(TimeMilliseconds);
		
		do {
			t0=System.currentTimeMillis( );

			} while (t0 < t1);
	}

}

