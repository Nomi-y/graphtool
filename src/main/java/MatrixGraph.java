import java.util.*;

public class MatrixGraph implements Comparable<MatrixGraph> {

    public final double[][] matrix;

    public MatrixGraph(double[][] matrix) {
        this.matrix = matrix;
        // computeVertexExcentricity();
        // directionalGraph = computeDirectional();
        // weaklyConnectedGraph = computeWeakConnectivity();
        // stronglyConnectedGraph = computeStrongConnectivity();
        // computeRadius();
        // computeDiameter();
        // computeCenter();
    }

    public boolean computeConnectivity() {
        double[][] matrix = MatrixUtils.distanceMatrix(MatrixUtils.binaryMatrix(this.matrix));
        for (double[] doubles : matrix)
            for (int j = 0; j < matrix.length; j++)
                if (doubles[j] == Double.POSITIVE_INFINITY)
                    return false;
        return true;
    }

    // public int getDiameter() {
    // return diameter;
    // }
    //
    // public int getRadius() {
    // return radius;
    // }
    //
    // public ArrayList<String> getCenter() {
    // return center;
    // }

    // public boolean isStronglyConnectedGraph() {
    // return stronglyConnectedGraph;
    // }

    // public boolean isWeaklyConnectedGraph() {
    // return weaklyConnectedGraph;
    // }

    public ArrayList<Integer> computeVertexExcentricity() {
        // if (!stronglyConnectedGraph) { vertexExcentricity = new ArrayList<>();
        // return; }
        ArrayList<Integer> vertexExcentricity = new ArrayList<>();
        double[][] m = MatrixUtils.distanceMatrix(matrix);
        for (double[] doubles : m) {
            double max = Double.MIN_VALUE;
            for (int j = 0; j < m.length; j++) {
                if (doubles[j] > max)
                    max = doubles[j];
            }
            vertexExcentricity.add(max == Double.POSITIVE_INFINITY ? -1 : (int) max);
        }
        return vertexExcentricity;
    }

    public int getVertexDegree(int index) {
        if (index < 0 || index > computeVertexExcentricity().size())
            return -1;
        int count = 0;
        for (int i = 0; i < matrix.length; i++)
            if (matrix[index][i] != 0)
                count++;
        return count;
    }

    public int computeDiameter() {
        ArrayList<Integer> vertexExcentricity = computeVertexExcentricity();
        if (!computeConnectivity() || vertexExcentricity.isEmpty())
            return -1;
        int max = Integer.MIN_VALUE;
        for (int j : vertexExcentricity)
            if (j > max)
                max = j;
        return max;
    }

    public int computeRadius() {
        ArrayList<Integer> vertexExcentricity = computeVertexExcentricity();
        if (!computeConnectivity() || vertexExcentricity.isEmpty())
            return -1;
        int min = Integer.MAX_VALUE;
        for (int j : vertexExcentricity)
            if (j < min)
                min = j;
        return min;
    }

    public ArrayList<String> computeCenter() {
        if (!computeConnectivity()) {
            return new ArrayList<>();
        }
        ArrayList<Integer> vertexExcentricity = computeVertexExcentricity();
        ArrayList<String> centers = new ArrayList<>();
        int radius = computeRadius();
        for (int i = 0; i < vertexExcentricity.size(); i++) {
            if (vertexExcentricity.get(i) == radius) {
                centers.add("V" + i);
            }
        }
        return centers;
    }

    public int countVertices() {
        return matrix.length;
    }

    // public int countEdges() {
    // int count = 0;
    // for (double[] v : MatrixUtils.binaryMatrix(this.matrix))
    // for (double d : v)
    // if (d == 1)
    // count++;
    // return count / 2;
    // }

    // public ArrayList<MatrixGraph> getStrongComponents() {
    // ArrayList<MatrixGraph> subgraphs = new ArrayList<>();
    // if (computeConnectivity()) {
    // subgraphs.add(this);
    // return subgraphs;
    // }
    // double[][] matrix = MatrixUtils.computeSCCMatrix(this.matrix);
    // Map<String, List<Integer>> componentMap = new HashMap<>();
    // for (int i = 0; i < matrix.length; i++) {
    // for (int j = 0; j < matrix.length; j++) {
    // if (matrix[i][j] == 1) {
    // componentMap.computeIfAbsent(Arrays.toString(matrix[i]), _ -> new
    // ArrayList<>()).add(i);
    // break;
    // }
    // }
    // }
    // for (List<Integer> component : componentMap.values()) {
    // double[][] subMatrix = MatrixUtils.extractSubgraphMatrix(matrix, component);
    // if ((new MatrixGraph(subMatrix)).computeConnectivity()) {
    // subgraphs.add(new MatrixGraph(subMatrix));
    // }
    // }
    // int size = countComponents();
    // if (size != subgraphs.size()) {
    // for (int i = 0; i < size; i++) {
    // subgraphs.add(new MatrixGraph(new double[][] { { 0 } }));
    // }
    // }
    // subgraphs.sort(null);
    // return subgraphs;
    // }

    public int countComponents() {
        double[][] m = MatrixUtils.pathMatrix(this.matrix);
        Set<String> uniquePatterns = new HashSet<>();
        for (double[] row : m)
            uniquePatterns.add(Arrays.toString(row));
        return uniquePatterns.size();
    }

    public ArrayList<String> getArticulations() {
        ArrayList<String> art = new ArrayList<>();
        int old = countComponents();
        for (int i = 0; i < computeVertexExcentricity().size(); i++) {
            if (getVertexDegree(i) >= 2) {
                double[][] reducedMatrix = MatrixUtils.removeVertex(this.matrix, i);
                MatrixGraph g = new MatrixGraph(reducedMatrix);
                if (g.countComponents() > old)
                    art.add("V" + i);
            }
        }
        return art;
    }

    public boolean isArticulation(int vertexIndex) {
        for (String art : getArticulations())
            if (art.equals("V" + vertexIndex))
                return true;
        return false;
    }

    public ArrayList<String> getBridges() {
        ArrayList<String> bridges = new ArrayList<>();
        int old = countComponents();
        double[][] matrix = this.matrix;
        for (int i = 0; i < computeVertexExcentricity().size(); i++) {
            for (int j = i + 1; j < computeVertexExcentricity().size(); j++) {
                if (matrix[i][j] != 0) {
                    double temp = matrix[i][j];
                    matrix[i][j] = 0;
                    matrix[j][i] = 0;
                    MatrixGraph g = new MatrixGraph(matrix);
                    if (g.countComponents() > old)
                        bridges.add("V" + i + " - V" + j);
                    matrix[i][j] = temp;
                }
            }
        }
        return bridges;
    }

    public boolean isBridge(int vertexIndex1, int vertexIndex2) {
        for (String bri : getBridges())
            if (bri.equals("V" + vertexIndex1 + " - V" + vertexIndex2)
                    || bri.equals("V" + vertexIndex2 + " - V" + vertexIndex1))
                return true;
        return false;
    }

    public int countBridges() {
        return getBridges().size();
    }

    // public boolean hasCycle() {
    // double[][] pathMatrix = pathMatrix().toArray();
    // double[][] adjacencyMatrix = toBinaryAdjacencyMatrix().toArray();
    // for (int i = 0; i < pathMatrix.length; i++)
    // for (int j = 0; j < pathMatrix.length; j++)
    // if (i == j) pathMatrix[i][j] = adjacencyMatrix[i][j];
    // for (int i = 0; i < pathMatrix.length; i++) {
    // if (pathMatrix[i][i] == 1) return true;
    // }
    // return false;
    // }

    public void printGeneralInfo() {
        StringBuilder str = new StringBuilder();
        str.append("\t\tGraph Information\n")
                // .append("Vertecies:\t").append(countVertices()).append("\n")
                // .append("Edges:\t\t").append(countEdges()).append("\n")
                .append("Radius:\t\t").append(computeRadius()).append("\n")
                .append("Diameter:\t").append(computeDiameter()).append("\n")
                .append("Center:\t\t").append(computeCenter().toString()).append("\n")
                .append(computeConnectivity() ? "The graph is connected" : "The graph is not connected.")
                .append("\n")
                .append("The graph has ").append(countComponents())
                .append(" components.");
        System.out.println(str.toString());
    }

    public void printVertexInfo() {
        StringBuilder str = new StringBuilder();
        str.append("\n\t\tVertex Information\n");
        str.append(countVertices()).append(" Vertices\n");
        str.append(getArticulations().size()).append(" Articulation Points\n\n");
        str.append(String.format("%-10s %-15s %-10s %-15s\n", "Vertex", "Eccentricity", "Degree", "Articulation"));
        str.append("-------------------------------------------------------------\n");
        for (int i = 0; i < computeVertexExcentricity().size(); i++) {
            str.append(String.format("%-10s %-15d %-10d %-15s\n",
                    "V" + i, computeVertexExcentricity().get(i), getVertexDegree(i), isArticulation(i) ? "x" : ""));
        }
        str.append("\nArticulations: ").append(getArticulations()).append("\n");
        System.out.println(str.toString());
    }

    public void printEdgeInfo() {
        StringBuilder str = new StringBuilder();
        str.append("\n\t\tEdge Information\n");
        str.append(String.format("%-15s %-15s %-10s\n", "Edge", "Weight", "Bridge"));
        str.append("-----------------------------------------------------\n");
        double[][] matrix = this.matrix;
        for (int i = 0; i < computeVertexExcentricity().size(); i++) {
            for (int j = i + 1; j < computeVertexExcentricity().size(); j++) {
                if (matrix[i][j] > 0) {
                    str.append(String.format("%-15s %-15.2f %-10s\n",
                            "V" + i + " - V" + j, matrix[i][j], isBridge(i, j) ? "Yes" : "No"));
                }
            }
        }
        str.append("\nBridges: ").append(getBridges()).append("\n");
        System.out.println(str.toString());
    }

    public void printMatrix() {
        StringBuilder str = new StringBuilder();
        for (double[] row : matrix) {
            for (double num : row) {
                String formattedNum;
                if (num == 0.0) {
                    formattedNum = String.format("%4s", "0.0");
                    str.append("\033[90m").append(formattedNum).append("\033[0m ");
                } else if (Double.isInfinite(num)) {
                    formattedNum = String.format("%4s", "∞ ");
                    str.append("\033[31;1m").append(formattedNum).append("\033[0m ");
                } else {
                    formattedNum = String.format("%4.1f", num);
                    str.append(formattedNum).append(" ");
                }
            }
            str.append("\n\n");
        }
        System.out.print(str);
    }

    public void printMatrixPow(int pow) {
        if (pow < 1) {
            return;
        }
        if (pow == 1) {
            printMatrix();
            return;
        }
        double[][] powMatrix = MatrixUtils.deepCopy(matrix);
        powMatrix = MatrixUtils.pow(powMatrix, pow);
        new MatrixGraph(powMatrix).printMatrix();
    }

    public void printDistanceMatrix() {
        double[][] dm = MatrixUtils.distanceMatrix(matrix);
        new MatrixGraph(dm).printMatrix();
    }

    public void printPathMatrix() {
        double[][] pm = MatrixUtils.pathMatrix(matrix);
        new MatrixGraph(pm).printMatrix();
    }

    @Override
    public String toString() {
        double[][] matrix = this.matrix;
        StringBuilder str = new StringBuilder();
        for (double[] doubles : matrix)
            str.append(Arrays.toString(doubles)).append("\n");
        return str.toString();
    }

    @Override
    public int compareTo(MatrixGraph graph) {
        return graph.countVertices() - countVertices();
    }
}
