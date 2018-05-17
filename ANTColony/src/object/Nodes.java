package object;

import java.io.Serializable;

public class Nodes implements Serializable{

    private boolean caseValid;
    
    public Nodes(){
        this.caseValid = true;
    }
    
    public void changeCase(){
        if(this.caseValid) caseValid = false;
        else caseValid = true;
    }
    
    public boolean isCaseValid(){
        return this.caseValid;
    }
    
    public void setValid(){
        this.caseValid = true;
    }
    
}