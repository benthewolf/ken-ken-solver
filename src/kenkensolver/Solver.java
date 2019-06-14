package kenkensolver;
import java.util.List;
public class Solver {
    KenBoard board;
    Boolean stop = false;
    private Constraint currentConstraint;

    public Solver(KenBoard board) {
        this.board = board;
    }

    public void solve(){
        this.currentConstraint = this.board.getConstraints()[0][0];
            this.evaluate();
        this.render();
    }


    public void render() {


        for (int a = 0 ; a < this.board.getSize() ; ++a) {
            for (int b = 0; b < this.board.getSize() ; ++ b){
            System.out.print(this.board.getConstraints()[a][b].getCurrentVal() + "  ");
            }
            System.out.println();
        }

        System.out.println("\n");
    }

    private void clearCurrentValues() {
        this.currentConstraint.setCurrentVal(0);
    }

    private void addCurrentValue() {
        this.currentConstraint.setCurrentVal(this.currentConstraint.getSearch().poll());
    }

    private void getPreviousConstraint() {
        int[] previous = {this.currentConstraint.getRowNumber(), this.currentConstraint.getColoumnNumber()};
        previous = decrement(previous);
        this.currentConstraint = this.board.getConstraints()[previous[0]][previous[1]];
    }

    private void getNextConstraint() {
        int[] next = {this.currentConstraint.getRowNumber(), this.currentConstraint.getColoumnNumber()};
        next = increment(next);
        if(this.currentConstraint.getRowNumber() == this.board.getSize() - 1
                && this.currentConstraint.getColoumnNumber() == this.board.getSize() - 1 ){
            this.stop = true;
            return;
        }
        this.currentConstraint = this.board.getConstraints()[next[0]][next[1]];
    }

    private void moveForward() {
            this.getNextConstraint();
    }

    private void backTrack() {

    this.clearCurrentValues();
    if (!this.currentConstraint.getCage().getConstraints().isEmpty()
            && this.currentConstraint.getCage().getConstraints().peek().equals(this.currentConstraint)) {
        this.currentConstraint.getCage().getConstraints().pop();
    }
    this.getPreviousConstraint();

}

    private void loop() {

            this.clearCurrentValues();
            if (this.currentConstraint.getSearch().size() == 0) {
                this.backTrack();
            }else if(this.currentConstraint.getSearch().size() != 0) {
                this.addCurrentValue();
                if (this.currentConstraint.getCage().isFull()) {
                    if (!this.currentConstraint.getCage().isCageSatisfied()) {
                        if (this.currentConstraint.getSearch().size() == 0) {
                            this.backTrack();
                        } else if (this.currentConstraint.getSearch().size() != 0) {
                            this.loop();
                        }
                    } else if (this.currentConstraint.getCage().isCageSatisfied()) {
                        this.moveForward();
                    }
                } else if (!this.currentConstraint.getCage().isFull()) {
                    this.moveForward();
                }
            }
        }

private Boolean checkRow(int a){
        int row = this.currentConstraint.getRowNumber();
        int size = this.board.getSize();
        for (int b = 0; b < size; ++b ){
            if (this.board.getConstraints()[row][b].getCurrentVal() == a){
              return true;
            }

        }
        return false;
}

private Boolean checkColumn (int a){
    int column = this.currentConstraint.getColoumnNumber();
    int size = this.board.getSize();
    for (int b = 0; b < size; ++b ){
        if (this.board.getConstraints()[b][column].getCurrentVal() == a){
            return true;
        }

    }
    return false;
}
    private void setSearchableValues() {
        List<Integer> vals = this.currentConstraint.getPossibleValues();
        for (Integer a: vals){
            if (!this.checkColumn(a) && !this.checkRow(a)){
                this.currentConstraint.getSearch().addLast(a);
            }
        }

    }

    private void evaluate(){
        while(!this.stop) {
            if (this.currentConstraint.getCurrentVal() != 0) {
                this.loop();
                continue;
            }
            this.setSearchableValues();

            if (this.currentConstraint.getSearch().size() == 0) {
                this.backTrack();
                continue;
            }
            this.addCurrentValue();
            this.currentConstraint.getCage().getConstraints().push(this.currentConstraint);
            if (this.currentConstraint.getCage().isFull()) {
                if (!this.currentConstraint.getCage().isCageSatisfied()) {
                    if (this.currentConstraint.getSearch().size() == 0) {
                        this.backTrack();
                        continue;
                    } else if (this.currentConstraint.getSearch().size() != 0) {
                        this.loop();
                        continue;
                    }
                }else if(this.currentConstraint.getCage().isCageSatisfied()){
                    this.moveForward();
                    continue;
                }
            }else if(!this.currentConstraint.getCage().isFull()){
                this.moveForward();
                continue;
            }
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
