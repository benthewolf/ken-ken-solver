package kenkensolver;
import java.util.HashMap;

public class KenBoard {

    private int size;
    private Constraint[][] constraints;

    public KenBoard (int size , Constraint[][] map){
        this.size = size;
        this.constraints = map;
    }

    public int getSize() {
        return this.size;
    }

    public Constraint[][] getConstraints() {
        return constraints;
    }
}
