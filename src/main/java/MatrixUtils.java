import java.util.Arrays;

public abstract class MatrixUtils {

    public static boolean invalid(double[][] m) {
        if (m == null || m.length == 0)
            return true;
        int expectedLength = m[0].length;
        if (m.length != expectedLength)
            return true;
        for (double[] row : m)
            if (row.length != expectedLength)
                return true;
        return false;
    }

    public static double[][] dotProduct(double[][] m1, double[][] m2) {
        int size = m1.length;
        if (invalid(m1) || invalid(m2) || !equalSize(m1, m2))
            throw new IllegalArgumentException(
                    "Invalid matrix dimensions: Can only multiply two square Matricies with identical dimensions!");
        double[][] result = new double[size][size];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                for (int k = 0; k < size; k++)
                    result[i][j] += m1[i][k] * m2[k][j];
        return result;
    }

    public static double[][] pow(double[][] m, int k) {
        if (k < 1)
            throw new IllegalArgumentException("Fuck you.");
        double[][] result = deepCopy(m);
        for (; k > 1; k--)
            result = dotProduct(result, m);
        return result;
    }

    public static boolean equalSize(double[][] m1, double[][] m2) {
        if (m1 == null || m2 == null || m1.length == 0 || m2.length == 0)
            return false;
        if (m1.length != m2.length)
            return false;
        for (int i = 0; i < m1.length; i++)
            if (m1[i].length != m2[i].length)
                return false;
        return true;
    }

    public static double[][] deepCopy(double[][] arr) {
        int size = arr.length;
        double[][] copied = new double[size][size];
        for (int i = 0; i < size; i++)
            copied[i] = Arrays.copyOf(arr[i], size);
        return copied;
    }

    public static boolean equals(double[][] m1, double[][] m2) {
        return Arrays.deepEquals(m1, m2);
    }

    public static double[][] binaryMatrix(double[][] m) {
        int s1 = m.length;
        if (s1 == 0)
            throw new IllegalArgumentException("AAAAAAAAAAAAAAAAA");
        int s2 = m[0].length;
        double[][] binaryMatrix = new double[s1][s2];
        for (int i = 0; i < s1; i++)
            for (int j = 0; j < s2; j++)
                binaryMatrix[i][j] = (m[i][j] != 0) ? 1 : 0;
        return binaryMatrix;
    }

    public static double[][] distanceMatrix(double[][] m) {
        if (invalid(m))
            throw new IllegalArgumentException("COME ON");
        int size = m.length;
        double[][] adjacencyMatrix = binaryMatrix(m);
        double[][] prev;
        double[][] curr = new double[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (adjacencyMatrix[i][j] == 0 && i != j)
                    curr[i][j] = Double.POSITIVE_INFINITY;
                else if (adjacencyMatrix[i][j] != 0 && i != j)
                    curr[i][j] = 1;
            }
        }
        double[][] expMatrix = dotProduct(adjacencyMatrix, adjacencyMatrix);
        for (int k = 2; k < size; k++) {
            prev = deepCopy(curr);
            for (int i = 0; i < size; i++)
                for (int j = 0; j < size; j++)
                    if (expMatrix[i][j] != 0 && curr[i][j] == Double.POSITIVE_INFINITY)
                        curr[i][j] = k;
            if (Arrays.deepEquals(prev, curr))
                return curr;
            expMatrix = dotProduct(expMatrix, adjacencyMatrix);
        }
        return curr;
    }

    public static double[][] pathMatrix(double[][] m) {
        if (invalid(m))
            throw new IllegalArgumentException("COME ON");
        int size = m.length;
        double[][] adjacencyMatrix = binaryMatrix(m);
        double[][] prev;
        double[][] curr = deepCopy(adjacencyMatrix);
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                if (i == j)
                    curr[i][j] = 1;
        double[][] expMatrix = dotProduct(adjacencyMatrix, adjacencyMatrix);
        for (int k = 2; k < size; k++) {
            prev = deepCopy(curr);
            for (int i = 0; i < size; i++)
                for (int j = 0; j < size; j++)
                    if (expMatrix[i][j] != 0)
                        curr[i][j] = 1;
            if (Arrays.deepEquals(prev, curr))
                return curr;
            expMatrix = dotProduct(expMatrix, adjacencyMatrix);
        }
        return curr;
    }
    //
    // public static double[][] computeSCCMatrix(double[][] m) {
    // double[][] matrix = distanceMatrix(m);
    // double[][] transposed = transposeMatrix(matrix);
    // int size = matrix.length;
    // double[][] result = new double[size][size];
    // for (int i = 0; i < size; i++) {
    // for (int j = 0; j < size; j++) {
    // if ((matrix[i][j] != Double.POSITIVE_INFINITY && transposed[i][j] !=
    // Double.POSITIVE_INFINITY)
    // || i == j) {
    // result[i][j] = 1;
    // }
    // }
    // }
    // return result;
    // }

    public static double[][] transposeMatrix(double[][] m) {
        int size = m.length;
        double[][] transposed = new double[size][size];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                transposed[j][i] = m[i][j];
        return transposed;
    }

    // public static double[][] extractSubgraphMatrix(double[][] matrix,
    // List<Integer> vertices) {
    // if (vertices.size() == matrix.length) return deepCopy(matrix);
    // double[][] newMatrix = deepCopy(matrix);
    // vertices.sort(Comparator.reverseOrder()); // 💀
    // for (int vertex : vertices) newMatrix = removeVertex(newMatrix, vertex);
    // return newMatrix;
    // }

    // public static double[][] extractSubgraphMatrix(double[][] matrix,
    // List<Integer> vertices) {
    // int n = vertices.size();
    // double[][] subMatrix = new double[n][n];
    // for (int i = 0; i < n; i++) {
    // int origI = vertices.get(i);
    // for (int j = 0; j < n; j++) {
    // int origJ = vertices.get(j);
    // subMatrix[i][j] = matrix[origI][origJ];
    // }
    // }
    // return subMatrix;
    // }
    //
    public static double[][] removeVertex(double[][] matrix, int vertexIndex) {
        int size = matrix.length;
        if (size <= 1)
            return new double[0][0];
        double[][] newMatrix = new double[size - 1][size - 1];
        for (int i = 0, newI = 0; i < size; i++) {
            if (i == vertexIndex)
                continue;
            for (int j = 0, newJ = 0; j < size; j++) {
                if (j == vertexIndex)
                    continue;
                newMatrix[newI][newJ] = matrix[i][j];
                newJ++;
            }
            newI++;
        }
        return newMatrix;
    }

}
