package it.polimi.ingsw.Decorator;


import it.polimi.ingsw.Model.*;

import java.util.ArrayList;

//Hephaestus
public class SpecialBuild3 extends PlayerDecorator{

    // constructor
    public SpecialBuild3(PlayerInterface p){
        super(p);
    }

    public void decorate(){}
    
    public boolean build(int row, int col, Worker worker) {
        //normal build
    	
    	BoardCell b = worker.getBoard().getGrid()[row][col];  
        if(b.getLevel() == 3) {
            b.setDome(true);
        } else {
            b.setLevel(b.getLevel() + 1);
        }
        //build an additional block on the top of the first one
        if(b.getLevel() != 3) {
            b.setLevel(b.getLevel() + 1);
        }
        return false;
    }

}