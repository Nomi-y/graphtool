import java.util.*;

public class Graph {

    private final Set<Integer> vertices;
    private final Set<Edge> edges;

    public Graph() {
        vertices = new HashSet<>();
        edges = new HashSet<>();
    }

    public Graph(int vertexcount) {
        vertices = new HashSet<>(vertexcount);
        edges = new HashSet<>();
    }

    public Graph(double[][] adjMatrix) {
        if (MatrixUtils.invalid(adjMatrix))
            throw new IllegalArgumentException("Invalid Matrix");
        int vertexCount = adjMatrix.length;
        vertices = new HashSet<>(vertexCount);
        edges = new HashSet<>();
        for (int i = 0; i < vertexCount; i++) {
            vertices.add(i);
        }
        for (int i = 0; i < adjMatrix.length; i++) {
            for (int j = 0; j < i + 1; j++) {
                if (adjMatrix[i][j] != 0) {
                    edges.add(new Edge(adjMatrix[i][j], i, j));
                }
            }
        }
    }

    public int countVertices() {
        return vertices.size();
    }

    public int countEdges() {
        return edges.size();
    }

    public int countComponents() {
        return fulldfs().size();
    }

    public int countArticulations() {
        return getArticulations().size();
    }

    public int countBridges() {
        return getBridges().size();
    }

    public boolean isConnected() {
        return countComponents() == 1;
    }

    public boolean hasCycle() {
        int edgeCount = countEdges();
        int vertexCount = 0;
        List<Graph> trees = fulldfs();
        for (Graph tree : trees) {
            vertexCount += tree.countVertices();
        }
        return edgeCount > (vertexCount - trees.size());
    }

    public boolean hasEulerCycle() {
        return isConnected() && vertices
                .stream()
                .allMatch(v -> vertexdegree(v) % 2 == 0);
    }

    public boolean hasEulerLine() {
        long odd = vertices.stream()
                .filter(v -> vertexdegree(v) % 2 == 1)
                .count();
        return isConnected() && odd == 2;
    }

    Set<Integer> getVertices() {
        return vertices;
    }

    Set<Edge> getEdges() {
        return edges;
    }

    boolean contains(int v) {
        return vertices.contains(v);
    }

    boolean contains(Edge e) {
        return edges.contains(e);
    }

    int vertexdegree(int vertex) {
        int deg = 0;
        for (Edge edge : edges) {
            if (edge.v_from() == vertex || edge.v_to() == vertex) {
                deg++;
                if (edge.v_from() == edge.v_to())
                    deg++;
            }
        }
        return deg;
    }

    Edge getEdge(int v1, int v2) {
        for (Edge e : edges) {
            if ((e.v_from() == v1 && e.v_to() == v2) ||
                    e.v_from() == v2 && e.v_to() == v1) {
                return e;
            }
        }
        return null;
    }

    Set<Edge> getEdges(int vertex) {
        if (vertexdegree(vertex) == 0)
            return new HashSet<>();
        Set<Edge> edgeset = new HashSet<>();
        for (Edge edge : edges) {
            if (edge.v_from() == vertex || edge.v_to() == vertex) {
                edgeset.add(new Edge(edge.weight(), edge.v_from(), edge.v_to()));
            }
        }
        return edgeset;
    }

    void addVertex(int vertex) {
        if (vertices.contains(vertex))
            return;
        vertices.add(vertex);
    }

    void addEdge(Edge e) {
        if (e == null)
            return;
        edges.add(e);
    }

    void removeVertex(int vertex) {
        boolean s = vertices.remove(vertex);
        if (!s)
            return;
        edges.removeIf(e -> e.v_from() == vertex || e.v_to() == vertex);
    }

    void removeEdge(Edge edge) {
        edges.remove(edge);
    }

    List<Integer> getNeighbours(int vertex) {
        List<Integer> neighbours = new ArrayList<>();
        for (Edge edge : edges) {
            if (edge.v_from() == vertex) {
                int vTo = edge.v_to();
                neighbours.add(vTo);
            }
            if (edge.v_to() == vertex) {
                int vFrom = edge.v_from();
                neighbours.add(vFrom);
            }
        }
        return neighbours;
    }

    Set<Integer> getArticulations() {
        Set<Integer> art = new HashSet<>();
        int origComponents = countComponents();

        vertices.forEach(v -> {
            Graph graph = Graph.copy(this);
            graph.removeVertex(v);
            if (graph.countComponents() > origComponents) {
                art.add(v);
            }
        });
        return art;
    }

    Set<Edge> getBridges() {
        Set<Edge> bridges = new HashSet<>();
        int origComponents = countComponents();
        for (Edge edge : edges) {
            Graph graph = Graph.copy(this);
            graph.removeEdge(edge);
            if (graph.countComponents() > origComponents) {
                bridges.add(edge);
            }
        }
        return bridges;
    }

    Graph minimalSpanningTree() {
        List<Edge> edgelist = new ArrayList<>(edges.size());
        Graph tree = new Graph();
        Graph graph = copy(this);
        edgelist.addAll(graph.edges);
        Collections.sort(edgelist);
        graph.vertices.forEach(v -> {
            tree.addVertex(v);
        });
        for (Edge edge : edgelist) {
            tree.addEdge(edge);
            if (tree.hasCycle())
                tree.removeEdge(edge);
        }
        return tree;
    }

    List<Integer> getEulerCycle() {
        if (!hasEulerCycle())
            return null;
        for (Integer vertex : vertices) {
            if (vertexdegree(vertex) % 2 == 0) {
                return hierholzer(vertex);
            }
        }
        throw new RuntimeException("Euler");
    }

    List<Integer> getEulerCycle(int startvertex) {
        if (!hasEulerCycle())
            return null;
        return hierholzer(startvertex);
    }

    List<Integer> getEulerLine() {
        if (!hasEulerLine())
            return null;
        return hierholzer(vertices.stream()
                .filter(v -> vertexdegree(v) % 2 == 1).toList().getFirst());
    }

    List<Integer> hierholzer(int startvertex) {
        if (edges.isEmpty()) {
            return new ArrayList<>();
        }
        Graph graph = copy(this);
        List<Integer> result = new ArrayList<>();
        Deque<Integer> path = new ArrayDeque<>();
        Map<Integer, List<Edge>> edgeMap = new HashMap<>();

        for (int v : graph.getVertices()) {
            edgeMap.put(v, new ArrayList<>(graph.getEdges(v)));
        }

        int current = startvertex;
        path.push(current);

        while (!path.isEmpty()) {
            if (edgeMap.get(current) != null && !edgeMap.get(current).isEmpty()) {
                Edge next = edgeMap.get(current).removeFirst();
                int neighbor = next.v_from() == current
                        ? next.v_to()
                        : next.v_from();
                int finalCurrent = current;
                edgeMap.get(neighbor).removeIf(e -> (e.v_from() == finalCurrent && e.v_to() == neighbor) ||
                        (e.v_from() == neighbor && e.v_to() == finalCurrent));
                path.push(current);
                current = neighbor;
            } else {
                result.add(current);
                current = path.pop();
            }
        }
        Collections.reverse(result);
        return result;
    }

    List<Graph> getComponents() {
        List<Graph> list = new ArrayList<>();
        if (countComponents() == 1) {
            list.add(copy(this));
            return list;
        }
        List<Graph> tree = fulldfs();
        for (Graph graph : tree) {
            Set<Integer> vlist = graph.getVertices();
            Graph g = new Graph();
            vlist.forEach(v -> {
                g.addVertex(v);
            });
            vlist.forEach(v -> {
                for (Edge e : getEdges(v)) {
                    g.addEdge(new Edge(e.weight(), e.v_from(), e.v_to()));
                }
            });
            list.add(g);
        }
        return list;
    }

    Graph dfstree(int startvertex) {
        return dfstree(startvertex, new HashSet<>());
    }

    Graph dfstree(int startvertex, Set<Integer> visited) {
        Graph dfstree = new Graph(vertices.size());
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(startvertex);
        visited.add(startvertex);
        dfstree.addVertex(startvertex);

        while (!stack.isEmpty()) {
            int current = stack.pop();
            List<Integer> neighbours = getNeighbours(current);
            Collections.reverse(neighbours);
            neighbours.forEach(n -> {
                if (!visited.contains(n)) {
                    visited.add(n);
                    dfstree.addVertex(n);
                    Edge e = getEdge(current, n);
                    if (e != null) {
                        dfstree.addEdge(e);
                    }
                    stack.push(n);
                }
            });
        }
        return dfstree;
    }

    List<Graph> fulldfs() {
        List<Graph> fulldfs = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();
        vertices.forEach(v -> {
            if (!visited.contains(v)) {
                Graph dfstree = dfstree(v, visited);
                fulldfs.add(dfstree);
            }
        });
        return fulldfs;
    }

    Graph bfstree(int startvertex) {
        return bfstree(startvertex, new HashSet<>());
    }

    Graph bfstree(int startvertex, Set<Integer> visited) {
        Graph dfstree = new Graph(vertices.size());
        Deque<Integer> queue = new ArrayDeque<>();
        queue.offer(startvertex);
        visited.add(startvertex);
        dfstree.addVertex(startvertex);

        while (!queue.isEmpty()) {
            int current = queue.poll();
            getNeighbours(current).forEach(n -> {
                if (!visited.contains(n)) {
                    visited.add(n);
                    dfstree.addVertex(n);
                    Edge e = getEdge(current, n);
                    if (e != null) {
                        dfstree.addEdge(e);
                    }
                    queue.offer(n);
                }
            });

        }
        return dfstree;
    }

    List<Graph> fullbfs() {
        List<Graph> fulldfs = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();
        vertices.forEach(v -> {
            if (!visited.contains(v)) {
                Graph dfstree = this.dfstree(v, visited);
                fulldfs.add(dfstree);
            }
        });
        return fulldfs;
    }

    List<Edge> sortEdges() {
        List<Edge> list = new ArrayList<>(edges);
        Collections.sort(list);
        return list;
    }

    static Graph copy(Graph graph) {
        Graph gr = new Graph();
        graph.getVertices().forEach(v -> {
            gr.addVertex(v);
        });
        for (Edge edge : graph.edges) {
            gr.addEdge(edge);
        }
        return gr;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Graph))
            return false;
        Graph g = (Graph) o;
        return vertices.equals(g.vertices) &&
                edges.size() == g.edges.size() &&
                edges.containsAll(g.edges);
    }

    public String edgeInfo() {
        StringBuilder str = new StringBuilder();
        str.append("\t\tEdge Information\n")
                .append(countEdges())
                .append(" Edges\n")
                .append(countBridges())
                .append(" Bridges\n\n")
                .append(String.format("%-15s %-15s %-10s\n", "Edge", "Weight", "Bridge"))
                .append("-".repeat(50));
        List<Edge> edges = sortEdges();
        for (Edge e : edges) {
            str.append("\n")
                    .append(String.format("%-15s %-15.2f %-10s",
                            e.v_from() + " - " + e.v_to(),
                            e.weight(),
                            getBridges().contains(e) ? "x" : ""));
        }
        return str.toString();
    }

    public void printEdgeInfo() {
        System.out.println(edgeInfo());
    }

    public String advancedGraphInfo() {
        StringBuilder str = new StringBuilder();
        str.append("\n\t\tAdvanced Graph Information\n")
                .append("-".repeat(50))
                .append("\n")
                .append("The graph ")
                .append(hasCycle() ? "has" : "does not have")
                .append(" a Cycle.\n");
        if (hasEulerCycle()) {
            str.append("The graph has an Euler Cycle.\n");
        }
        if (hasEulerLine()) {
            str.append("The graph has an Euler Line.");
        }
        return str.toString();
    }

    public void printSubgraphs() {
        List<Graph> sub = getComponents();
        if (countComponents() > 1) {
            System.out.println("Components:\n\n");
            for (Graph sg : sub) {
                System.out.println(sg);
                System.out.println("\t" + "_".repeat(50));
            }
        }
    }

    public void printMST() {
        List<Graph> sub = getComponents();
        System.out.println("Minimal spanning tree(s):\n");
        for (Graph sg : sub) {
            System.out.println(sg.minimalSpanningTree());
            System.out.println("\t" + "_".repeat(50));
        }

    }

    public void printAdvancedInfo() {
        System.out.println(advancedGraphInfo());
    }

    public String eulerString() {
        if (!hasEulerCycle() && !hasEulerLine())
            return "";

        StringBuilder str = new StringBuilder();
        str.append("\tEuler ").append(hasEulerCycle() ? "cycle" : "line").append(":\n\t");
        str.append(hasEulerCycle() ? getEulerCycle(0) : getEulerLine());
        return str.toString();
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("\tCycle: " + hasCycle() + "\n")
                .append("\tEuler Line: " + hasEulerLine() + "\n")
                .append("\tEuler Cycle: " + hasEulerCycle() + "\n\n")
                .append("\tVertices:\n\t");

        int vCount = 0;
        for (int v : vertices) {
            String spacer = v < 10 ? "  [" : " [";
            String vertexLabel = v + spacer + vertexdegree(v) + "]" +
                    (getArticulations().contains(v) ? "a" : "");
            str.append(String.format("%-11s", vertexLabel));

            if (++vCount % 7 == 0)
                str.append("\n\t");
        }
        if (vCount % 7 != 0)
            str.append("\n");

        str.append("\n\tEdges:\n\t");
        int eCount = 0;
        if (edges.isEmpty()) {
            str.append("No Edges.");
        }
        for (Edge e : sortEdges()) {
            str.append(String.format("%-20s", e.toString() + (getBridges().contains(e) ? "b" : "")));
            if (++eCount % 4 == 0)
                str.append("\n\t");
        }
        str.append("\n\n");
        str.append(eulerString());
        return str.toString();
    }
}

// record Vertex(int label) {
//
// @Override
// public boolean equals(Object o) {
// if (o == null || getClass() != o.getClass())
// return false;
// Vertex vertex = (Vertex) o;
// return Objects.equals(label, vertex.label);
// }
//
// @Override
// public int hashCode() {
// return Objects.hashCode(label);
// }
//
// @Override
// public String toString() {
// return String.format("V%-2s", label());
// }
//
// public int compareTo(Vertex v) {
// return Integer.compare(this.label(), v.label());
// }
// }
//

record Edge(double weight, int v_from, int v_to) implements Comparable<Edge> {

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        Edge edge = (Edge) o;
        return Double.compare(weight, edge.weight) == 0 && (Objects.equals(v_from, edge.v_from)
                && Objects.equals(v_to, edge.v_to)
                || Objects.equals(v_to, edge.v_from)
                        && Objects.equals(v_from, edge.v_to));
    }

    @Override
    public int hashCode() {
        return Objects.hash(weight, v_from, v_to);
    }

    @Override
    public int compareTo(Edge edge) {
        int weight = Double.compare(this.weight, edge.weight());
        if (weight == 0) {
            int from = Integer.compare(v_from(), edge.v_from());
            if (from == 0) {
                return Integer.compare(v_to(), edge.v_to());
            } else {
                return from;
            }
        } else {
            return weight;
        }
    }

    @Override
    public String toString() {
        return String.format("%3s - %3s [%s]", v_from(), v_to(), weight());
    }
}
