package kenkensolver;
import kenkensolver.KenBoard;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;
import java.util.stream.Stream;

public class Solver {

    Stack<Constraint> stack = new Stack<>();
    private int[][] solution;
    KenBoard board;
    private HashMap<String, HashSet<Integer>> rows = new HashMap<>();
    private HashMap<String, HashSet<Integer>> coloumns = new HashMap<>();
    //private Constraint currentConstraint;
    private int count = 0;

    public Solver(KenBoard board){
        this.board = board;
        this.solution =  new int[this.board.getSize()][this.board.getSize()];

        for (int a = 0; a < this.board.getSize(); ++a){
            this.rows.put("row" + a, new HashSet<>());
        }

        for (int a = 0; a < this.board.getSize(); ++a){
            this.coloumns.put("column" + a, new HashSet<>());
        }
    }

    public void solve(){
    Constraint freebie = this.board.getFreebie();
    this.rows.get(freebie.getRow()).add(freebie.getResult());
    this.coloumns.get(freebie.getColoumn()).add(freebie.getResult());
    this.evaluate("00");
    }

    private void moveForward (String current){
        Constraint currentConstraint = this.board.getConstraints().get(current);
        String currentRow = currentConstraint.getRow();
        String currentColumn = currentConstraint.getColoumn();
        this.rows.get(currentRow).add(currentConstraint.getCurrentVal());
        this.coloumns.get(currentColumn).add(currentConstraint.getCurrentVal());
        if (!this.stack.contains(currentConstraint)){
            this.stack.push(currentConstraint);
        }
        int[] front = increment(new int[]{currentConstraint.getRowNumber(),currentConstraint.getColoumnNumber()});
        if (this.board.getConstraints().get(
                String.format("%02d",Integer.parseInt(Integer.toString(front[0]) + front[1]))
        ).getMethod() == Constraint.METHOD.EQUALTO){
            front = increment(new int[]{currentConstraint.getRowNumber(),currentConstraint.getColoumnNumber()});
        }
        this.evaluate(String.format("%02d",Integer.parseInt(Integer.toString(front[0]) + front[1])));
    }

    private void backTrack(String current){
        Constraint currentConstraint = this.board.getConstraints().get(current);
        String currentRow = currentConstraint.getRow();
        String currentColumn = currentConstraint.getColoumn();
        if (this.stack.peek().equals(currentConstraint)){
            this.stack.pop();
            this.rows.get(currentRow).remove(currentConstraint.getCurrentVal());
            this.coloumns.get(currentColumn).remove(currentConstraint.getCurrentVal());
        }

        int[] back = decrement(new int[]{currentConstraint.getRowNumber(),currentConstraint.getColoumnNumber()});
        if (this.board.getConstraints().get(
                String.format("%02d",Integer.parseInt(Integer.toString(back[0]) + back[1]))
        ).getMethod() == Constraint.METHOD.EQUALTO){
            back = decrement(new int[]{currentConstraint.getRowNumber(),currentConstraint.getColoumnNumber()});
        }
        if (currentConstraint.getCage().getConstraints().peek().equals(currentConstraint)){
            currentConstraint.getCage().getConstraints().pop();
        }
        this.evaluate(String.format("%02d",Integer.parseInt(Integer.toString(back[0]) + back[1])));
    }

    private void loop(String current){

        Constraint currentConstraint = this.board.getConstraints().get(current);
        String currentRow = currentConstraint.getRow();
        String currentColumn = currentConstraint.getColoumn();
        this.rows.get(currentRow).remove(currentConstraint.getCurrentVal());
        this.coloumns.get(currentColumn).remove(currentConstraint.getCurrentVal());
        if (currentConstraint.getSearch().size() == 0){
            this.backTrack(current);
        }
        currentConstraint.setCurrentVal(currentConstraint.getSearch().poll());
        if (currentConstraint.getCage().isFull()){
            if(!currentConstraint.getCage().isCageSatisfied()){
                if(currentConstraint.getSearch().size() == 0){
                    this.backTrack(current);
                }
                else{
                    this.loop(current);
                }
            }
        }
        this.moveForward(current);

    }

    private void evaluate(String current){
        Constraint currentConstraint = this.board.getConstraints().get(current);
        String currentRow = currentConstraint.getRow();
        String currentColumn = currentConstraint.getColoumn();
        if(!this.stack.isEmpty() && this.stack.peek().equals(currentConstraint)){
            this.loop(current);
        }
        else
        {
            currentConstraint.getPossibleValues().stream().forEach( e ->{
                if(!this.rows.get(currentRow).contains(e)
                        && !this.coloumns.get(currentColumn).contains(e)){
                    currentConstraint.getSearch().addLast(e);
                }});

            if (currentConstraint.getSearch().size() == 0){
                this.backTrack(current);
            }
            currentConstraint.setCurrentVal(currentConstraint.getSearch().poll());
            currentConstraint.getCage().getConstraints().push(currentConstraint);
            if (currentConstraint.getCage().isFull()){
                if(!currentConstraint.getCage().isCageSatisfied()){
                    if(currentConstraint.getSearch().size() == 0){
                        this.backTrack(current);
                    }
                    else{
                        this.loop(current);
                    }
                }
            }
            this.moveForward(current);
        }

    }



private int[] decrement(int[] coords){
        if (coords[1] == 0){
            coords[0] -= 1;
            coords[1] = this.board.getSize() - 1;
            return coords;
        }

        else{
            coords[1] -= 1;
            return coords;
        }
}

private int[] increment(int[] coords){
        if (coords[1] == this.board.getSize() - 1){
            coords[0] += 1;
            coords[1] = 0;
            return coords;
        }

        else
        {
            coords[1] += 1;
            return coords;
        }
}

}
