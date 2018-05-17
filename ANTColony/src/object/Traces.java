package object;

import java.awt.Color;
import java.awt.Graphics;

public class Traces{
    
    private static int timeOfLive = 12; //12
    private static double traceFactor = 0.9;
    
    private int away, back, age;
    
    public Traces(){
        this.away = 0;
        this.back = 0;
        this.age = 0;
    }
    
    /*
     * The Traces draw themselves
     */
    public void draw(Graphics g, Playground p, int i, int j) {
		
        if((this.away != 0)||(this.back != 0)){			
			g.setColor(this.getColor(away, back));
			g.fillRoundRect(i, j, p.getSizeCase(), p.getSizeCase(), 2, 2);
			
		}        
    }
    
    /*
     * Add one to age, and if too old, remove the trace
     */
    public void toAge(){
        this.age = this.age + 1;
        if(this.age > timeOfLive){
            if(--this.away < 0) this.away = 0; 
            if(--this.back < 0) this.back = 0;
            this.age = 0;
        }
    }
    
    
    public int getAway(){
        return this.away;
    }
    
    public int getBack(){
        return this.back;
    }
    
    public void addAway(){
        this.away = this.away + 1;
    }
    
    public void addBack(){
        this.back = this.back + 1;
    }
    
    public void displayTrace(){
        System.out.println("Away : " + this.away + " Back : " + this.back );
    }
    
    private Color getColor(int away, int back){
        int total, iColorAway, iColorBack, iColorTotal;
		Color myColor;
        if(away < 51) iColorAway = 255 - 5 * away;
		else iColorAway = 0;
		if(back < 20) iColorBack = 255 - 12 * back;
		else iColorBack = 0;
		total = away + back;
		if(total < 51 ) iColorTotal = 255 - 3 * total;
		else iColorTotal = 0;
		myColor = new Color(iColorBack, iColorTotal, iColorAway);
		return myColor;
    }
    
    public void reset(){
        this.away = 0;
        this.back = 0;
    }
}