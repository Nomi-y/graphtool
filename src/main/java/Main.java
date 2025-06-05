import java.io.Console;

public class Main {
    public static void main(String[] args) {
        String filepath;
        if (args.length != 0) {
            filepath = args[0];
        } else {
            filepath = io();
        }
        char delimiter = args.length == 2 ? args[1].charAt(0) : CSVutils.detectDelimiter(filepath);
        double[][] input = CSVutils.readCSV(filepath, delimiter);
        Graph gr = new Graph(input);
        MatrixGraph g = new MatrixGraph(input);

        for (int i = 1; i <= 5; i++) {
            System.out.println("\n\tA^" + i + "\n");
            g.printMatrixPow(i);
        }

        System.out.println("\tDistance Matrix\n");
        g.printDistanceMatrix();
        System.out.println("\tPath Matrix\n");
        g.printPathMatrix();

        g.printGeneralInfo();
        g.printVertexInfo();

        gr.printEdgeInfo();
        gr.printAdvancedInfo();

        gr.printSubgraphs();
        gr.printMST();
    }

    public static String io() {
        Console console = System.console();
        String filepath = console.readLine("Enter path to csv: ");
        return filepath;
    }
}
