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

        assertEquals(3, mst.countEdges(), "MST should have v-1 edges");
        assertFalse(mst.hasCycle(), "MST must be acyclic");

        double totalWeight = mst.getEdges().stream().mapToDouble(Edge::weight).sum();
        assertEquals(6.0, totalWeight, 0.001, "MST should have minimal total weight");
    }

    @Test
    public void testEulerCycleAndLine() {
        Graph g = new Graph();

        g.addVertex(1);
        g.addVertex(2);
        g.addVertex(3);
        g.addVertex(4);

        g.addEdge(new Edge(1, 1, 2));
        g.addEdge(new Edge(1, 2, 3));
        g.addEdge(new Edge(1, 3, 4));
        g.addEdge(new Edge(1, 4, 1));
        g.addEdge(new Edge(1, 2, 4));

        assertFalse(g.hasEulerCycle(), "Graph should not have an Eulerian cycle");
        assertTrue(g.hasEulerLine(), "Graph should have an Eulerian trail");

        g.removeEdge(new Edge(1, 2, 4));

        assertTrue(g.hasEulerCycle(), "Graph should have an Eulerian cycle");
        assertFalse(g.hasEulerLine(), "Eulerian trail should be false when cycle is true");

        g.removeEdge(new Edge(1, 1, 2));

        assertFalse(g.hasEulerCycle(), "Graph should no longer have an Eulerian cycle");
        assertTrue(g.hasEulerLine(), "Graph should now have an Eulerian trail");
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

        List<Integer> path = g.getEulerCycle();

        assertNotNull(path, "Path should not be null");
        assertEquals(5, path.size(), "Eulerian cycle should contain v+1 vertices");

        assertEquals(path.getFirst(), path.getLast(), "Start and end must match");

        Set<String> traversed = new HashSet<>();
        for (int i = 0; i < path.size() - 1; i++) {
            int from = path.get(i);
            int to = path.get(i + 1);
            String edge = Math.min(from, to) + "-" + Math.max(from, to);
            traversed.add(edge);
        }

        Set<String> expected = Set.of("0-1", "1-2", "2-3", "0-3");
        assertEquals(expected, traversed, "Traversal must match all edges exactly");
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

        assertTrue(g.hasEulerLine(), "Graph should have an Eulerian trail");

        List<Integer> path = g.getEulerLine();

        assertNotNull(path, "Euler trail path should not be null");
        assertEquals(g.countEdges() + 1, path.size(), "Trail should use v+e vertices");

        assertEquals(1, path.getFirst(), "Start should be one of the odd-degree vertices");
        assertEquals(3, path.getLast(), "End should be the other odd-degree vertex");

        Set<String> traversed = new HashSet<>();
        for (int i = 0; i < path.size() - 1; i++) {
            int from = path.get(i);
            int to = path.get(i + 1);
            String edge = Math.min(from, to) + "-" + Math.max(from, to);
            traversed.add(edge);
        }

        Set<String> expected = Set.of("0-1", "1-2", "2-3", "0-3", "1-3");
        assertEquals(expected, traversed, "Trail must include all edges exactly once");
    }

    @Test
    void testGetComponents() {
        List<Graph> components = graph24n_01_split.getComponents();
        assertEquals(6, components.size());

        int totalEdges = 0;
        int totalVert = 0;
        for (Graph g : components) {
            totalVert += g.countVertices();
            totalEdges += g.countEdges();
        }
        assertEquals(graph24n_01_split.countVertices(), totalVert);
        assertEquals(graph24n_01_split.countEdges(), totalEdges);
        components.forEach(component -> {
            component.getVertices().forEach(v -> {
                assertTrue(graph24n_01_split.contains(v));
            });
            component.getEdges().forEach(e -> {
                assertTrue(graph24n_01_split.contains(e));
            });
        });
    }
}
