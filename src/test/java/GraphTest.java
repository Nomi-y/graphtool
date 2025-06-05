import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GraphTest {

    Graph graphSmall;
    Graph graph24n_01;
    Graph graph24n_01_split;

    static final double[][] smallMatrix = {
            { 0, 1, 0, 0, 0 },
            { 1, 0, 0, 1, 0 },
            { 0, 0, 0, 0, 0 },
            { 0, 1, 0, 0, 1 },
            { 0, 0, 0, 1, 0 }
    };

    static final double[][] matrix24n_01 = {
            { 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 1, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 1, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0 },
    };

    static final double[][] matrix24n_01_split = {
            { 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0 }
    };

    @BeforeEach
    void setUp() {
        graphSmall = new Graph(smallMatrix);
        graph24n_01 = new Graph(matrix24n_01);
        graph24n_01_split = new Graph(matrix24n_01_split);
    }

    @Test
    void testCountComponents_shouldWork_returns1() {
        assertEquals(1, graph24n_01.countComponents());
    }

    @Test
    void testCountArticulations_shouldWork_returns11() {
        assertEquals(10, graph24n_01.countArticulations());
    }

    @Test
    void testCountBridges_shouldWork_returns4() {
        assertEquals(4, graph24n_01.countBridges());
    }

    @Test
    void testGetNeighbours() {
        Graph g = new Graph();
        g.addVertex(1);
        g.addVertex(2);
        g.addVertex(3);
        g.addEdge(new Edge(1.0, 1, 2));
        g.addEdge(new Edge(1.0, 1, 3));

        List<Integer> neighbours1 = g.getNeighbours(1);
        List<Integer> neighbours2 = g.getNeighbours(2);
        List<Integer> neighbours3 = g.getNeighbours(3);

        assertEquals(2, neighbours1.size());
        assertTrue(neighbours1.contains(2) && neighbours1.contains(3));
        assertEquals(1, neighbours2.size());
        assertTrue(neighbours2.contains(1));
        assertEquals(1, neighbours3.size());
        assertTrue(neighbours3.contains(1));

        assertTrue(g.getNeighbours(4).isEmpty());
    }

    @Test
    void testAdjacencyMatrixConstructor() {
        Graph g = new Graph(matrix24n_01_split);

        assertTrue(g.getEdge(0, 1) != null);
        assertTrue(g.getEdge(1, 2) != null);
        assertTrue(g.getEdge(1, 3) != null);
        assertTrue(g.getEdge(2, 3) != null);
        assertTrue(g.getEdge(4, 5) != null);
        assertTrue(g.getEdge(5, 10) != null);
        assertTrue(g.getEdge(18, 19) != null);
        assertTrue(g.getEdge(19, 20) != null);
        assertTrue(g.getEdge(20, 18) != null);

        assertTrue(g.getEdge(0, 2) == null);
        assertTrue(g.getEdge(0, 4) == null);
        assertTrue(g.getEdge(13, 14) == null);

        assertEquals(matrix24n_01_split.length, g.countVertices());

        assertTrue(g.getNeighbours(13).isEmpty());

        assertTrue(g.getEdge(1, 0) != null);
        assertTrue(g.getEdge(2, 1) != null);
        assertTrue(g.getEdge(5, 4) != null);
    }

    @Test
    void testMinimalSpanningTree() {
        Graph g = new Graph();
        g.addVertex(1);
        g.addVertex(2);
        g.addVertex(3);
        g.addVertex(4);

        g.addEdge(new Edge(4, 1, 2));
        g.addEdge(new Edge(1, 2, 3));
        g.addEdge(new Edge(3, 3, 4));
        g.addEdge(new Edge(2, 4, 1));
        g.addEdge(new Edge(5, 2, 4));

        Graph mst = g.minimalSpanningTree();

        assertEquals(3, mst.countEdges());
        assertFalse(mst.hasCycle());
        assertEquals(6.0, mst.getEdges().stream().mapToDouble(Edge::weight).sum(), 0.001);
    }

    @Test
    void testEulerCycleAndLine() {
        Graph g = new Graph();
        g.addVertex(1);
        g.addVertex(2);
        g.addVertex(3);
        g.addVertex(4);

        Edge e1 = new Edge(1, 1, 2);
        Edge e2 = new Edge(1, 2, 3);
        Edge e3 = new Edge(1, 3, 4);
        Edge e4 = new Edge(1, 4, 1);
        Edge e5 = new Edge(1, 2, 4);

        g.addEdge(e1);
        g.addEdge(e2);
        g.addEdge(e3);
        g.addEdge(e4);
        g.addEdge(e5);

        assertFalse(g.hasEulerCycle());
        assertTrue(g.hasEulerLine());

        g.removeEdge(g.getEdge(2, 4));
        assertTrue(g.hasEulerCycle());
        assertFalse(g.hasEulerLine());

        g.removeEdge(g.getEdge(1, 2));
        assertFalse(g.hasEulerCycle());
        assertTrue(g.hasEulerLine());
    }

    @Test
    void testHierholzerEulerCycle() {
        Graph g = new Graph();
        g.addVertex(0);
        g.addVertex(1);
        g.addVertex(2);
        g.addVertex(3);

        g.addEdge(new Edge(1, 0, 1));
        g.addEdge(new Edge(1, 1, 2));
        g.addEdge(new Edge(1, 2, 3));
        g.addEdge(new Edge(1, 3, 0));

        List<Integer> cycle = g.getEulerCycle();
        assertEquals(5, cycle.size());
        assertEquals(cycle.get(0), cycle.get(cycle.size() - 1));
    }

    @Test
    void testHierholzerEulerTrail() {
        Graph g = new Graph();
        g.addVertex(0);
        g.addVertex(1);
        g.addVertex(2);
        g.addVertex(3);

        g.addEdge(new Edge(1, 0, 1));
        g.addEdge(new Edge(1, 1, 2));
        g.addEdge(new Edge(1, 2, 3));
        g.addEdge(new Edge(1, 3, 0));
        g.addEdge(new Edge(1, 1, 3));

        List<Integer> trail = g.getEulerLine();
        assertEquals(6, trail.size());
        assertEquals(1, trail.get(0));
        assertEquals(3, trail.get(trail.size() - 1));
    }

    @Test
    void testGetComponents() {
        List<Graph> components = graph24n_01_split.getComponents();
        assertEquals(6, components.size());

        int totalVertices = components.stream()
                .mapToInt(Graph::countVertices)
                .sum();
        int totalEdges = components.stream()
                .mapToInt(Graph::countEdges)
                .sum();

        assertEquals(graph24n_01_split.countVertices(), totalVertices);
        assertEquals(graph24n_01_split.countEdges(), totalEdges);

        components.forEach(comp -> {
            comp.getVertices().forEach(v -> assertTrue(graph24n_01_split.contains(v)));
            comp.getEdges().forEach(e -> assertTrue(graph24n_01_split.contains(e)));
        });
    }
}
