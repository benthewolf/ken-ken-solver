import kenkensolver.Parser;
import kenkensolver.Solver;
import java.io.IOException;
public class Main {
    public  static void main (String[] args) throws IOException {
        long current = System.currentTimeMillis();
        Solver solver = new Solver(new Parser("const.txt").parse());
        solver.solve();
        System.out.print("\nCompleted in: "  + (System.currentTimeMillis() - current) + " ms");

    }
}
