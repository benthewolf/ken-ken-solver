package kenkensolver;
import java.util.Stack;
public class Cage {
    private Stack<Constraint> constraints = new Stack<>();
    private Constraint.METHOD method;
    private int result;
    private int size = 0;
    public Cage(int result, Constraint.METHOD method){
        this.method = method;
        this.result = result;
        this.size = size;


    }

    private boolean checker2(){
        if (this.method == Constraint.METHOD.EQUALTO){
            if (this.constraints.get(0).getCurrentVal() == this.result){
                return true;
            }

            else
            {
                return false;
            }
        }

        else if (this.method == Constraint.METHOD.ADD){
            int  currResult = 0;
            for (Constraint a: this.constraints){
                currResult += a.getCurrentVal();
            }

            if (currResult == this.result){
                return true;
            }

            else
                return false;

        }

        else if (this.method == Constraint.METHOD.DIVIDE){
            if (this.constraints.get(0).getCurrentVal()
                    / this.constraints.get(1).getCurrentVal() == this.result){
                return true;
            }
            if (this.constraints.get(1).getCurrentVal() /
                    this.constraints.get(0).getCurrentVal() == this.result){
                return true;
            }

            else {return false;}
        }

        else if (this.method == Constraint.METHOD.MULTIPLY){
            int currResult = 1;
            for (Constraint a: this.constraints){
                currResult *= a.getCurrentVal();
            }
            if (currResult == this.result){
                return true;
            }
            else
                return false;
        }

        else {
            if (Math.abs(this.constraints.get(0).getCurrentVal()
                    - this.constraints.get(1).getCurrentVal()) == this.result){
                return true;}

            else
                return false;
        }
    }

    public boolean isCageSatisfied(){

        return checker2();
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isFull(){
        if (this.getSize() == this.constraints.size()){
            return true;
        }

        else return false;
    }

    public Stack<Constraint> getConstraints() {
        return constraints;
    }

}
