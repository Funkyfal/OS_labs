import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Vector;


public class ResultsWriter {
    public static void writeResults(Vector<Vector<Integer>> rects, String outputFileName) {
        try {
            PrintWriter writer = new PrintWriter(outputFileName);

            for (Vector<Integer> rectangle : rects) {
                for (int dimension : rectangle) {
                    writer.print(dimension + " ");
                }

                if (rectangle.size() == 12) {
                   // writer.println(ParallelepipedSolver.Solve(rectangle) ? "yes" : "no");
                } else {
                    writer.println("неверное количество");
                }
            }

            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(3);
        }
    }
}
