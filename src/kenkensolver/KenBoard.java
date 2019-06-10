package kenkensolver;

import kenkensolver.Constraint;

import java.util.HashMap;
import java.util.List;

public class KenBoard {

    private int size;
    private HashMap<String,Constraint> constraints;
    private Constraint freebie;

    public KenBoard (int size , HashMap<String, Constraint> map){
        this.size = size;
        this.constraints = map;

        for (int a=0 ; a < size ; ++a){
            for(int b = 0; b < size ; ++b){
              Constraint temp = this.constraints.get(Integer.toString(a)+b);
              if (temp.getMethod() == Constraint.METHOD.EQUALTO){
                  this.freebie = temp;
              }
              temp.setColoumn("column" + b);
              temp.setColoumnNumber(b);
              temp.setRowNumber(a);
              temp.setRow("row" + a);
            }
        }
    }

    public int getSize() {
        return this.size;
    }

    public Constraint getFreebie() {
        return freebie;
    }

    public HashMap<String, Constraint> getConstraints() {
        return constraints;
    }
}
