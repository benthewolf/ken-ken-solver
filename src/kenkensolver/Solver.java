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
    private Constraint currentConstraint;
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

        this.cap += Integer.toString(this.board.getSize() - 1);
        this.cap += Integer.toString(this.board.getSize() - 1);
    }

    private boolean hasNext(Constraint currentConstraint) {
        int[] front = increment(new int[]{currentConstraint.getRowNumber(), currentConstraint.getColoumnNumber()});
        String frontstr = String.format("%02d", Integer.parseInt(Integer.toString(front[0]) + front[1]));
        if (this.board.getConstraints().containsKey(frontstr)) {

            return true;
        } else {
            return false;
        }
    }

    public void solve() {
        this.currentConstraint = this.board.getConstraints().get("00");
        try {
            this.evaluate();
        } catch (NullPointerException e) {

        } catch (EmptyStackException e) {

        }
        this.render();
    }


    public void render() {
        int count = 0;
        int[] curr = {0, 0};
        for (int a = 0; a < Math.pow(this.board.getSize(), 2); ++a) {
            String frontstr = String.format("%02d", Integer.parseInt(Integer.toString(curr[0]) + curr[1]));
            System.out.print(this.board.getConstraints().get(frontstr).getCurrentVal() + "  ");
            if (count == this.board.getSize() - 1) {
                System.out.println();
                count = -1;
            }
            ++count;
            curr = increment(curr);

        }
    }

    private void clearCurrentValues() {
        this.rows.get(this.currentConstraint.getRow()).remove(this.currentConstraint.getCurrentVal());
        this.coloumns.get(this.currentConstraint.getColoumn()).remove(this.currentConstraint.getCurrentVal());
    }

    private void addCurrentValues() {
        this.rows.get(this.currentConstraint.getRow()).add(this.currentConstraint.getCurrentVal());
        this.coloumns.get(this.currentConstraint.getColoumn()).add(this.currentConstraint.getCurrentVal());
    }

    private void getPreviousConstraint() {
        int[] previous = {this.currentConstraint.getRowNumber(), this.currentConstraint.getColoumnNumber()};
        previous = decrement(previous);
        this.currentConstraint = this.board.getConstraints().get(Integer.toString(previous[0]) + previous[1]);
    }

    private void getNextConstraint() {
        int[] next = {this.currentConstraint.getRowNumber(), this.currentConstraint.getColoumnNumber()};
        next = increment(next);
        this.currentConstraint = this.board.getConstraints().get(Integer.toString(next[0]) + next[1]);
    }

    private void moveForward() {

            this.addCurrentValues();
            if (!this.stack.contains(this.currentConstraint)) {
                this.stack.push(this.currentConstraint);
            }
            this.getNextConstraint();
            this.evaluate();

    }

    private void backTrack() {
            if (this.stack.peek().equals(this.currentConstraint)) {
                this.stack.pop();
            }
            this.clearCurrentValues();
            if (!this.currentConstraint.getCage().getConstraints().isEmpty()
                    && this.currentConstraint.getCage().getConstraints().peek().equals(this.currentConstraint)) {
                this.currentConstraint.getCage().getConstraints().pop();
            }
            this.getPreviousConstraint();
            this.evaluate();

    }


    private void loop() {

        if (!currMax.equals(this.cap)) {
            this.clearCurrentValues();
            if (this.currentConstraint.getSearch().size() == 0) {
                this.backTrack();
            }
            this.currentConstraint.setCurrentVal(this.currentConstraint.getSearch().poll());
            if (this.currentConstraint.getCage().isFull()) {
                if (!this.currentConstraint.getCage().isCageSatisfied()) {
                    if (this.currentConstraint.getSearch().size() == 0) {
                        this.backTrack();
                    } else {
                        this.loop();
                    }
                }
            }
            this.moveForward();
        }
        if (currMax.equals(this.cap)) {

        }

    }

    private void setSearchableValues() {
        this.currentConstraint.getPossibleValues().stream().forEach(e -> {
            if (!this.rows.get(this.currentConstraint.getRow()).contains(e)
                    && !this.coloumns.get(this.currentConstraint.getColoumn()).contains(e)) {
                this.currentConstraint.getSearch().addLast(e);
            }
        });
    }

    private void evaluate() {
            if (!this.stack.isEmpty() && this.stack.peek().equals(this.currentConstraint)) {
                this.loop();
            }
            this.setSearchableValues();

            if (this.currentConstraint.getSearch().size() == 0) {
                this.backTrack();
            }
            this.currentConstraint.setCurrentVal(this.currentConstraint.getSearch().poll());
            this.currentConstraint.getCage().getConstraints().push(this.currentConstraint);
            if (this.currentConstraint.getCage().isFull()) {
                if (!this.currentConstraint.getCage().isCageSatisfied()) {
                    if (this.currentConstraint.getSearch().size() == 0) {
                        this.backTrack();
                    } else {
                        this.loop();
                    }
                }
            }

            this.moveForward();
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
