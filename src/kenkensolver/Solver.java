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
    this.actualSolve(new int[]{0,0});
    }


    private void actualSolve (int[] coords){
        //if(coords[0] == 3 && coords[1] == 3){return;}

        String current = String.format("%02d",Integer.parseInt(Integer.toString(coords[0]) + coords[1]));
        Constraint currentConstraint = this.board.getConstraints().get(current);
        String currentRow = currentConstraint.getRow();
        String currentColumn = currentConstraint.getColoumn();

        if (!this.stack.isEmpty() && this.stack.peek().equals(currentConstraint)){
            if (currentConstraint.getMethod() == Constraint.METHOD.EQUALTO ){
                this.stack.pop();
                currentConstraint.setCurrentVal(currentConstraint.getPossibleValues().get(0));
                System.out.printf("Moving Backwards \n Row %3d : Column %3d : CurrentVal %3d", currentConstraint.getRowNumber(),
                        currentConstraint.getColoumnNumber(), currentConstraint.getCurrentVal());
                System.out.println();
                actualSolve(decrement(coords));
            }

            this.rows.get(currentRow).remove(currentConstraint.getCurrentVal());
            this.coloumns.get(currentColumn).remove(currentConstraint.getCurrentVal());
            if (currentConstraint.getSearch().size() == 0){
                this.stack.pop();
                actualSolve(decrement(coords));
            }
            currentConstraint.setCurrentVal(currentConstraint.getSearch().poll());

            this.rows.get(currentRow).add(currentConstraint.getCurrentVal());
            this.coloumns.get(currentColumn).add(currentConstraint.getCurrentVal());
            System.out.printf("Moving Forward \n Row %3d : Column %3d : CurrentVal %3d", currentConstraint.getRowNumber(),
                    currentConstraint.getColoumnNumber(), currentConstraint.getCurrentVal());
            System.out.println();
            actualSolve(increment(coords));
        }
        if (currentConstraint.getMethod() == Constraint.METHOD.EQUALTO){
            this.stack.push(currentConstraint);
            currentConstraint.setCurrentVal(currentConstraint.getPossibleValues().get(0));
            System.out.printf("Moving Forwards \n Row %3d : Column %3d : CurrentVal %3d", currentConstraint.getRowNumber(),
                    currentConstraint.getColoumnNumber(), currentConstraint.getCurrentVal());
            System.out.println();
            actualSolve(increment(coords));
        }
        currentConstraint.getPossibleValues().stream().forEach( e ->{
            if(!this.rows.get(currentRow).contains(e)
                    && !this.coloumns.get(currentColumn).contains(e)){
                currentConstraint.getSearch().addLast(e);
            }});

        if (currentConstraint.getSearch().size() == 0){
            if (this.stack.peek().equals(currentConstraint)){
                this.stack.pop();
            }
            System.out.printf("Moving Backwards \n Row %3d : Column %3d : CurrentVal %3d", currentConstraint.getRowNumber(),
                    currentConstraint.getColoumnNumber(), currentConstraint.getCurrentVal());
            System.out.println();
            actualSolve(this.decrement(coords));
        }


        currentConstraint.setCurrentVal(currentConstraint.getSearch().poll());

        this.rows.get(currentRow).add(currentConstraint.getCurrentVal());
        this.coloumns.get(currentColumn).add(currentConstraint.getCurrentVal());
        System.out.println();
        this.stack.push(currentConstraint);

        currentConstraint.getCage().getConstraints().push(currentConstraint);
        if (currentConstraint.getCage().isFull()){
            if(!currentConstraint.getCage().isCageSatisfied()){
                if(currentConstraint.getSearch().size() == 0){
                    while(!stack.peek().equals(currentConstraint.getCage().getConstraints().peek()))
                        this.stack.pop();
                        actualSolve(new int[]{this.stack.peek().getRowNumber(), this.stack.peek().getColoumnNumber()});
                }
                else{
                    actualSolve(coords);
                }
            }
        }
        System.out.printf("Moving Forward \n Row %3d : Column %3d : CurrentVal %3d", currentConstraint.getRowNumber(),
                currentConstraint.getColoumnNumber(), currentConstraint.getCurrentVal());
        actualSolve(increment(coords));

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
