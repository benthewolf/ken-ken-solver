package kenkensolver;
import java.util.HashMap;

public class KenBoard {

    private int size;
    private HashMap<String,Constraint> constraints;
    private Constraint freebie = null;

    public KenBoard (int size , HashMap<String, Constraint> map){
        this.size = size;
        this.constraints = map;

        for (int a=0 ; a < size ; ++a){
            for(int b = 0; b < size ; ++b){
              Constraint temp = this.constraints.get(Integer.toString(a)+b);
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
