package kenkensolver;
import kenkensolver.KenBoard;

import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;
import java.util.stream.Stream;

public class Solver {

    Stack<Constraint> stack = new Stack<>();
    private int[][] solution;
    KenBoard board;
private String cap = "";
private String currMax = "";
    private HashMap<String, HashSet<Integer>> rows = new HashMap<>();
    private HashMap<String, HashSet<Integer>> coloumns = new HashMap<>();
    private int count = 0;

    public Solver(KenBoard board) {
        this.board = board;
        this.solution = new int[this.board.getSize()][this.board.getSize()];

        for (int a = 0; a < this.board.getSize(); ++a) {
            this.rows.put("row" + a, new HashSet<>());
        }

        for (int a = 0; a < this.board.getSize(); ++a) {
            this.coloumns.put("column" + a, new HashSet<>());
        }

        this.cap+= Integer.toString(this.board.getSize() - 1);
        this.cap+= Integer.toString(this.board.getSize() - 1);
    }

    private boolean hasNext(Constraint currentConstraint){
        int[] front = increment(new int[]{currentConstraint.getRowNumber(), currentConstraint.getColoumnNumber()});
        String frontstr = String.format("%02d", Integer.parseInt(Integer.toString(front[0]) + front[1]));
        if (this.board.getConstraints().containsKey(frontstr)){

            return true;
        }

        else {
            return false;
        }
    }

    public void solve() {

    try {
    this.evaluate("00");
    }

    catch (NullPointerException e){

    }

    catch (EmptyStackException e){

    }
    this.render();
    }



    public void render(){
        int count = 0;
        int[] curr = {0,0};
        for (int a = 0; a < Math.pow(this.board.getSize(),2); ++a){
            String frontstr = String.format("%02d", Integer.parseInt(Integer.toString(curr[0]) + curr[1]));
            System.out.print(this.board.getConstraints().get(frontstr).getCurrentVal() + "  ");
            if(count == this.board.getSize() - 1){
                System.out.println();
                count = -1;
            }
            ++count;
           curr = increment(curr);

        }
    }

    private int moveForward(String current) {
        if (!currMax.equals(this.cap)){
        Constraint currentConstraint = this.board.getConstraints().get(current);
        String currentRow = currentConstraint.getRow();
        String currentColumn = currentConstraint.getColoumn();
        int[] front = increment(new int[]{currentConstraint.getRowNumber(), currentConstraint.getColoumnNumber()});
        this.rows.get(currentRow).add(currentConstraint.getCurrentVal());
        this.coloumns.get(currentColumn).add(currentConstraint.getCurrentVal());
        if (!this.stack.contains(currentConstraint)) {
            this.stack.push(currentConstraint);
        }
        this.evaluate(String.format("%02d", Integer.parseInt(Integer.toString(front[0]) + front[1])));
        }
        if (currMax.equals(this.cap)){
            return 1;
        }

        return 1;
    }

    private int backTrack(String current) {
        if (!currMax.equals(this.cap)){
        Constraint currentConstraint = this.board.getConstraints().get(current);
        String currentRow = currentConstraint.getRow();
        String currentColumn = currentConstraint.getColoumn();
        if (this.stack.peek().equals(currentConstraint)) {
            this.stack.pop();
            this.rows.get(currentRow).remove(currentConstraint.getCurrentVal());
            this.coloumns.get(currentColumn).remove(currentConstraint.getCurrentVal());
        }

        int[] back = decrement(new int[]{currentConstraint.getRowNumber(), currentConstraint.getColoumnNumber()});
        if (!currentConstraint.getCage().getConstraints().isEmpty() && currentConstraint.getCage().getConstraints().peek().equals(currentConstraint)) {
            currentConstraint.getCage().getConstraints().pop();
        }
        this.evaluate(String.format("%02d", Integer.parseInt(Integer.toString(back[0]) + back[1])));
        }
        if (currMax.equals(this.cap)){
            return 1;
        }
        return 1;
    }


    private int loop(String current) {

        if(!currMax.equals(this.cap)) {
            Constraint currentConstraint = this.board.getConstraints().get(current);
            String currentRow = currentConstraint.getRow();
            String currentColumn = currentConstraint.getColoumn();
            this.rows.get(currentRow).remove(currentConstraint.getCurrentVal());
            this.coloumns.get(currentColumn).remove(currentConstraint.getCurrentVal());
            if (currentConstraint.getSearch().size() == 0) {
                 return this.backTrack(current);
            }
            currentConstraint.setCurrentVal(currentConstraint.getSearch().poll());
            if (currentConstraint.getCage().isFull()) {
                if (!currentConstraint.getCage().isCageSatisfied()) {
                    if (currentConstraint.getSearch().size() == 0) {
                        return this.backTrack(current);
                    } else {
                         return this.loop(current);
                    }
                }
            }
            this.moveForward(current);
        }
        if (currMax.equals(this.cap)){
            return 1;
        }
        return 1;
    }

    private int evaluate(String current) {
        if(!currMax.equals(this.cap)) {
            if (current.equals(this.cap)){
                currMax = current;
            }
            Constraint currentConstraint = this.board.getConstraints().get(current);
            String currentRow = currentConstraint.getRow();
            String currentColumn = currentConstraint.getColoumn();
            if (!this.stack.isEmpty() && this.stack.peek().equals(currentConstraint)) {
               return this.loop(current);
            }
                currentConstraint.getPossibleValues().stream().forEach(e -> {
                    if (!this.rows.get(currentRow).contains(e)
                            && !this.coloumns.get(currentColumn).contains(e)) {
                        currentConstraint.getSearch().addLast(e);
                    }
                });

                if (currentConstraint.getSearch().size() == 0) {
                   return this.backTrack(current);
                }
                currentConstraint.setCurrentVal(currentConstraint.getSearch().poll());
                currentConstraint.getCage().getConstraints().push(currentConstraint);
                if (currentConstraint.getCage().isFull()) {
                    if (!currentConstraint.getCage().isCageSatisfied()) {
                        if (currentConstraint.getSearch().size() == 0) {
                          return  this.backTrack(current);
                        } else {
                          return  this.loop(current);
                        }
                    }
                }

            if (currMax.equals(this.cap)){
                if (current.equals(currMax)) {
                    this.stack.push(currentConstraint);
                   return 1;
                }

                return 1;
            }
              return this.moveForward(current);
        }

return 1;

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
