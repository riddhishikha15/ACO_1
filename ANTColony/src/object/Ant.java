package object;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;


public class Ant {
	
	private boolean goal, tired;
	private int posX, posY, move;
	private int[] position, lastPosition;
	
	private static int tryToMove = 7;			// Number of tries to find the best next position {8}
	private static int endurance = 80;			// Number of moves before the ant get tired {80}
	private static double coefFoodToHome = 1.2;	// Privileges the Back amount over the Away amount {2}
	private static double coefHomeToFood = 2;	// Privileges the Away amount over the Back amount {2}
	
	public Ant(){
		this.posX = 0;
		this.posY = 0;
		this.lastPosition = new int[2];
		this.goal = false;
		this.tired = false;
		this.move = 0;
	}
	
	/*
	 * Allow the ants to draw themselves
	 */
	public void draw(Graphics g, Playground p) {
		if(this.getGoal()){
		    if(this.getTired())
		        g.setColor(Color.CYAN);
		    else g.setColor(Color.BLUE);
		}
		else {
		    if(this.getTired())
		        g.setColor(Color.YELLOW);
		    else g.setColor(Color.ORANGE);
		}		
		int x = (int)(p.getSizeCase()/2);
		g.fillOval(posX, posY, p.getSizeCase(), p.getSizeCase());
		g.setColor(Color.BLACK);		
		g.fillOval(posX + x-1, posY + x-1, x, x);
	}
	
	/*
	 * Generate a new position inside the playground on a valid case
	 */
	public void moveStraightIn(int x, int y, Playground p){
	    int[] newPos = new int[2];
	    newPos = getRandomMoveInStraight(x,y,p);
	    this.setPosition(newPos[0], newPos[1]);
	}
	
	/*
	 * Set a position where there is no trace like the one I add
	 */
	public void moveStraightAwayFromMyTrace(int x, int y, Playground p){
		int [] newTry = new int[2];
		int count = 0;
		do{
			newTry = getRandomMoveInStraight(x,y,p);
			if(count++ > tryToMove) break;
		}while(isMyTrace(newTry[0], newTry[1], p));
		this.setPosition(newTry[0], newTry[1]);
	}
	
	/*
	 * Set a position where there is no Away trace
	 */
	public void moveStraightAwayFromAway(int x, int y, Playground p){
		int [] newTry = new int[2];
		int count = 0;
		do{
			newTry = getRandomMoveInStraight(x,y,p);
			if(count++ > tryToMove) break;
		} while(isTraceAway(newTry[0], newTry[1], p));		
		this.setPosition(newTry[0], newTry[1]);
	}
	
	/*
	 * Set a position where there is no Back trace
	 */
	public void moveStraightAwayFromBack(int x, int y, Playground p){
		int [] newTry = new int[2];
		int count = 0;
		boolean moveNotFound = false;
		do{
			newTry = getRandomMoveInStraight(x,y,p);
			if(count++ > tryToMove){
			    moveNotFound = true;
			    break;
			}
		}
		while(isTraceBack(newTry[0], newTry[1], p));
		if(moveNotFound)
		    this.moveStraightIn(x,y,p);
		this.setPosition(newTry[0], newTry[1]);
	}
	
	/*
	 * Set a new position where there is no trace
	 */
	public void moveStraightAwayFromAnyTrace(int x, int y, Playground p){
	    int [] newTry = new int[2];
		int count = 0;
		do{
			newTry = getRandomMoveInStraight(x,y,p);
			if(count++ > tryToMove) break;
		}
		while(isAnyTrace(newTry[0], newTry[1], p));
		this.setPosition(newTry[0], newTry[1]);
	}

	/*
	 * Set a new position according to the traces and coefFoodToHome's value
	 */
	public void moveFromFoodToHomeRepartition(int x, int y, Playground p){
	    int sizeCase = p.getSizeCase();
	    Traces t;
	    int count = 0;
	    double value = 0, max = 0;
	    int[] newPos = new int[2];
	    int[] tryNewPos = new int[2];
	    
	    do{
	        tryNewPos = getRandomMoveInStraight(x,y,p);
	        count++;
	        t = p.getTrace()[tryNewPos[0]][tryNewPos[1]];
            value = t.getAway() * coefFoodToHome - t.getBack();
            if((value > max)&&(!isLastPosition(tryNewPos[0], tryNewPos[1]))){
                max = value;
                newPos = tryNewPos;
            }
	    } while(count < tryToMove);
	    
	    if((newPos[0] == 0)&&(newPos[1] == 0))
	        this.moveStraightAwayFromBack(x,y,p);
	    else this.setPosition(newPos[0], newPos[1]);
	}
	
		
	/*
	 * Using random tries, get the best path from home to food according to coefHomeToFood's value
	 */
	public void moveFromHomeToFoodRepartition(int x, int y, Playground p){
	    int sizeCase = p.getSizeCase();
	    Traces t;
	    int count = 0;
	    double max = 0, value = 0;
	    int[] newPos = new int[2];
	    int[] tryNewPos = new int[2];
	    
	    do{
	        tryNewPos = getRandomMoveInStraight(x,y,p);
	        count++;
	        t = p.getTrace()[tryNewPos[0]][tryNewPos[1]];
	        value = t.getBack() * coefHomeToFood - t.getAway();
            if((value > max)&&(!isLastPosition(tryNewPos[0], tryNewPos[1]))){
                max = value;
                newPos = tryNewPos;
            }
	    } while(count < tryToMove);
	    
	    if((newPos[0] == 0)&&(newPos[1] == 0)){
	        //System.out.println("Failed to find the best path.");
	        this.moveStraightAwayFromAway(x,y,p);
	    }
	    else
	        this.setPosition(newPos[0], newPos[1]);
	}

	/*
	 * Set a new position where the combined traces are more
	 */
	public void moveTowardAllTrace(int x, int y, Playground p) {
        int sizeCase = p.getSizeCase();
	    Traces t;
	    int count = 0, max = 0, value = 0;
	    int[] newPos = new int[2];
	    for(int i=x-sizeCase; i<x+2*sizeCase; i+=sizeCase){
	        for(int j=y-sizeCase; j<y+2*sizeCase; j+=sizeCase){
	            if(isInside(i,j,p)){
		            if(Math.abs((i-x)/sizeCase) + Math.abs((j-y)/sizeCase) == 1){
		                count++;
		                t = p.getTrace()[i][j];
		                value = t.getBack() + t.getAway();
		                if(value > max){
		                    max = value;
		                    newPos[0] = i;
		                    newPos[1] = j;
		                }
		                
		            }
		            	
	            }
	        }
	    }
	    if((count == 2)||(isLastPosition(newPos[0], newPos[1])) || (newPos[0] == 0)&&(newPos[1] == 0)){
	        do{
	            newPos = getRandomMoveInStraight(x,y,p);
	            count++;
	            if(count>tryToMove) break;
	        } while (isLastPosition(newPos[0], newPos[1]));
	        this.setPosition(newPos[0], newPos[1]);
	    }
	    else{
	        this.setPosition(newPos[0], newPos[1]);
	    }
    }
    
	/*
	 * Set a new position where away traces are more
	 */
    public void moveTowardAway(int x, int y, Playground p) {
        int sizeCase = p.getSizeCase();
	    Traces t;
	    int count = 0, max = 0, value = 0;
	    int[] newPos = new int[2];
	    for(int i=x-sizeCase; i<x+2*sizeCase; i+=sizeCase){
	        for(int j=y-sizeCase; j<y+2*sizeCase; j+=sizeCase){
	            if(isInside(i,j,p)){
		            if(Math.abs((i-x)/sizeCase) + Math.abs((j-y)/sizeCase) == 1){
		                count++;
		                t = p.getTrace()[i][j];
		                value = t.getAway();
		                if(value > max){
		                    max = value;
		                    newPos[0] = i;
		                    newPos[1] = j;
		                }
		            }
	            }
	        }
	    }
	    if((isLastPosition(newPos[0], newPos[1])) || (newPos[0] == 0)&&(newPos[1] == 0)){
	        do{
	            newPos = getRandomMoveInStraight(x,y,p);
	            count++;
	            if(count>tryToMove) break;
	        } while (isLastPosition(newPos[0], newPos[1]));
	        this.setPosition(newPos[0], newPos[1]);
	    }
	    else{
	        this.setPosition(newPos[0], newPos[1]);
	    }
    }
    
    /*
     * Set a new position where back traces are more
     */
    public void moveTowardBack(int x, int y, Playground p) {
        int sizeCase = p.getSizeCase();
	    Traces t;
	    int count = 0, max = 0, value = 0;
	    int[] newPos = new int[2];
	    for(int i=x-sizeCase; i<x+2*sizeCase; i+=sizeCase){
	        for(int j=y-sizeCase; j<y+2*sizeCase; j+=sizeCase){
	            if(isInside(i,j,p)){
		            if(Math.abs((i-x)/sizeCase) + Math.abs((j-y)/sizeCase) == 1){
		                count++;
		                t = p.getTrace()[i][j];
		                value = t.getBack();
		                if(value > max){
		                    max = value;
		                    newPos[0] = i;
		                    newPos[1] = j;
		                }
		                
		            }
		            	
	            }
	        }
	    }
	    if((count == 2)||(isLastPosition(newPos[0], newPos[1])) || (newPos[0] == 0)&&(newPos[1] == 0)){
	        do{
	            newPos = getRandomMoveInStraight(x,y,p);
	            count++;
	            if(count>tryToMove) break;
	        } while (isLastPosition(newPos[0], newPos[1]));
	        this.setPosition(newPos[0], newPos[1]);
	    }
	    else{
	        this.setPosition(newPos[0], newPos[1]);
	    }
    }
	
    /*
     * Return the value of traces [away, back] of the position (x,y)
     */
    private int[] getDensity(int x, int y, Playground p){
	    int sizeCase;
	    int[] value = new int[2];
	    Traces t;
	    sizeCase = p.getSizeCase();
	    for(int i=x-sizeCase; i<x+2*sizeCase; i = i + sizeCase){
	        for(int j=y-sizeCase; j<y+2*sizeCase; j = j + sizeCase){
	            if(isInside(i,j,p)){
	                t = p.getTrace()[i][j];
	                value[0] = t.getAway();
	                value[1] = t.getBack();
	            }
	        }	        
	    }
	    return value;
	}
	
	private boolean isTraceAway(int x, int y, Playground p){
		boolean trace = true;
		Traces t = p.getTrace()[x][y];
		if(t.getAway()==0) trace = false;
		return trace;
	}
	private boolean isTraceBack(int x, int y, Playground p){
		boolean trace = true;
		Traces t = p.getTrace()[x][y];
		if(t.getBack()==0) trace = false;
		return trace;
	}
	private boolean isAnyTrace(int x, int y, Playground p){
		boolean trace = true;
		Traces t = p.getTrace()[x][y];
		if((t.getBack()==0)&&(t.getAway() == 0)) trace = false;
		return trace;
	}
	private boolean isMyTrace(int x, int y, Playground p){
	    boolean trace = true;
	    Traces t = p.getTrace()[x][y];
	    if((this.goal) && (t.getBack()==0)) trace = false;
	    if((!this.goal) && (t.getAway()==0)) trace = false;
	    return trace;
	}
	
	/*
	 * Return a new valid position [x,y]
	 */
	private int[] getRandomMoveInStraight(int x, int y, Playground p){
		int sizeCase = p.getSizeCase();
	    Random generator = new Random();
	    int[] newPos = new int[2];
		do{
		    newPos[0] = x + sizeCase * (generator.nextInt(3) - 1);
		    newPos[1] = y + sizeCase * (generator.nextInt(3) - 1);
		} while((!isInside(newPos[0], newPos[1], p) || (Math.abs((newPos[0]-x)/sizeCase)+Math.abs((newPos[1]-y)/sizeCase) != 1) || (isLastPosition(newPos[0],newPos[1]))));
	    return newPos;
	}
	
	/*
	 * Return true if the position (x,y) is equal to the last position
	 */
	private boolean isLastPosition(int x, int y){
        if((lastPosition[0]==x)&&(lastPosition[1]==y)) return true;
        else return false;
    }
	
	/*
	 * Return true if the position (x,y) is a valid position
	 */
	private boolean isInside(int x, int y, Playground p){
		boolean inside = true;
		int sizeCase = p.getSizeCase();
		int sizeX = p.getSizeX(), sizeY = p.getSizeY();
		if((x < sizeCase) || (y < sizeCase) || (x > sizeX - sizeCase) || (y > sizeY - sizeCase)) inside = false;
		else if(!p.getValidCase(x,y)) inside = false;
		return inside;
	}

	/*
	 * Give a random position
	 */
	public void setRandomPosition(Playground p){
	    Random generator = new Random();
	    int randX =0, randY=0;
	    randX = p.getSizeCase() * (1 + generator.nextInt(p.getSizeOfThePlayground()));
	    randY = p.getSizeCase() * (1 + generator.nextInt(p.getSizeOfThePlayground()));
	    this.setPosition(randX, randY);
	}
	
	/*
	 * Set the position (x,y)
	 */
	public void setPosition(int posX, int posY){
		this.lastPosition = getPosition();
	    this.posX = posX;
		this.posY = posY;
	}
	
	/*
	 * Return the position [x,y]
	 */
	public int getPosition()[]{
			this.position = new int[2];
			this.position[0] = this.posX;
			this.position[1] = this.posY;
			return this.position;
		}
	public void displayPosition(){
			System.out.print("[" + this.posX + "," + this.posY + "] ");
		}
	public void saveLastPosition(){
	    this.lastPosition = this.getPosition();
	}
	public void setGoal(){
		this.goal = true;
	}
	public void unsetGoal(){
		this.goal = false;
	}
	public boolean getGoal(){
		return goal;
	}
	public int getEndurance(){
	    return endurance;
	}
	public void addOneMove(){
	    this.move = this.move + 1;
	}
	public int getMove(){
	    return this.move;
	}
	public void setTired(){
	    this.tired = true;
	}
	public boolean getTired(){
	    return this.tired;
	}
    public void unsetTired() {
        this.tired = false; 
        this.move = 0;
    }
    public void resetLastPosition() {
        this.lastPosition[0] = 0;
        this.lastPosition[1] = 0;
    }
   }
