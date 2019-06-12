package kenkensolver;
import java.util.HashMap;
import java.util.Stack;

interface implement{
    boolean run(Constraint.METHOD a);
}

public class Cage {

    private Stack<Constraint> constraints = new Stack<>();
    private Constraint.METHOD method;
    private int result;
    private int size = 0;
    private HashMap<Constraint.METHOD, implement> checker = new HashMap<>();
    public Cage(int result, Constraint.METHOD method){
        this.method = method;
        this.result = result;
        this.size = size;

     this.setChecker();
    }

    private void setChecker (){
        this.checker.put(Constraint.METHOD.DIVIDE, e -> {
            if (this.constraints.get(0).getCurrentVal()
                    / this.constraints.get(1).getCurrentVal() == this.result){
                return true;
            }
            else if (this.constraints.get(1).getCurrentVal() /
                    this.constraints.get(0).getCurrentVal() == this.result){
                return true;
            }

            else {return false;}
        });

        this.checker.put(Constraint.METHOD.SUBTRACT, e ->{
          if (Math.abs(this.constraints.get(0).getCurrentVal()
                  - this.constraints.get(1).getCurrentVal()) == this.result){
              return true;}

          else
              return false;
        });

        this.checker.put(Constraint.METHOD.ADD, e ->{
            int  currResult = 0;
            for (Constraint a: this.constraints){
                currResult += a.getCurrentVal();
            }

            if (currResult == this.result){
                return true;
            }

            else
                return false;
        });

        this.checker.put(Constraint.METHOD.MULTIPLY, e ->{
            int currResult = 1;
            for (Constraint a: this.constraints){
                currResult *= a.getCurrentVal();
            }
            if (currResult == this.result){
                return true;
            }
            else
                return false;
        });

        this.checker.put(Constraint.METHOD.EQUALTO, e ->{
            if (this.constraints.get(0).getCurrentVal() == this.result){
                return true;
            }

            else
            {
                return false;
            }
        });
    }

    public boolean isCageSatisfied(){
        return this.checker.get(this.method).run(this.method);
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
