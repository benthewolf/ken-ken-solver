import kenkensolver.Cage;
import kenkensolver.Parser;
import kenkensolver.Solver;

public class Main {
    public  static void main (String[] args){
        long current = System.currentTimeMillis();
        Solver solver = new Solver(new Parser("const.txt").parse());
        solver.solve();
        System.out.print(System.currentTimeMillis() - current);


    }
}
